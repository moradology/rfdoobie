package com.azavea.rf.database

import com.azavea.rf.datamodel.AOI
import com.azavea.rf.database.meta.RFMeta._

import doobie._, doobie.implicits._
import cats._, cats.data._, cats.effect.IO
import cats.syntax.either._
import doobie.postgres._, doobie.postgres.implicits._
import doobie.util.invariant.InvalidObjectMapping
import doobie.scalatest.imports._
import org.scalatest._


class AoiDaoSpec extends FunSuite with Matchers with IOChecker {

  val transactor = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver", "jdbc:postgresql://database.service.rasterfoundry.internal/", "rasterfoundry", "rasterfoundry"
  )
  val trivial = sql"select 42".query[Int]

  val uuid = sql"SELECT md5(random()::text || clock_timestamp()::text)::uuid".query[java.util.UUID]

  val timestamp = sql"SELECT clock_timestamp()".query[java.sql.Timestamp]

  test("trivial")    { check(trivial) }

  test("uuid")       { check(uuid) }

  test("timestamp")  { check(timestamp) }

  //test("aoi")        { check(AoiDao.Statements.select.query[AOI]) }

}

