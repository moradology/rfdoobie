package com.azavea.rf.database

import com.azavea.rf.database.meta.RFMeta._
import com.azavea.rf.database.util._
import com.azavea.rf.datamodel._

import doobie._, doobie.implicits._
import doobie.postgres._, doobie.postgres.implicits._
import cats._, cats.data._, cats.effect.IO, cats.implicits._
import com.lonelyplanet.akka.http.extensions.PageRequest

import scala.concurrent.Future


abstract class Dao[Model: Composite](tableName: String) {

  def tableF = Fragment.const(tableName)

  def selectF: Fragment

  val countF: ConnectionIO[Int] = (sql"SELECT count(*) FROM" ++ tableF)

  def countQ = countF.query[Int].unique

  def listQ(
    filters: List[Option[Fragment]],
    pageRequest: Option[PageRequest]
  ): Query0[Model] = {

    (selectF ++ Fragments.whereAndOpt(filters: _*) ++ Page(pageRequest))
      .query[Model]
  }

  def list(
    filters: List[Option[Fragment]],
    pageRequest: Option[PageRequest]
  ): ConnectionIO[List[Model]] = listQ(filters, pageRequest).list

  def page(
    filters: List[Option[Fragment]],
    pageRequest: PageRequest
  )(implicit xa: Transactor[IO]): Future[PaginatedResponse[Model]] = {
    val transaction = for {
      page <- list(filters, Some(pageRequest))
      count <- (countF ++ Fragments.whereAndOpt(filters: _*)).query[Int]
    } yield {
      val hasPrevious = pageRequest.offset > 0
      val hasNext = (pageRequest.offset * pageRequest.limit) + 1 < count

      PaginatedResponse[Model](count, hasPrevious, hasNext, pageRequest.offset, pageRequest.limit, page)
    }
    transaction.transact(xa).unsafeToFuture
  }
}

