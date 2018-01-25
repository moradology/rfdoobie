package com.azavea.rf.database

import com.azavea.rf.database.meta.RFMeta._
import com.azavea.rf.database.util._
import com.azavea.rf.datamodel._

import doobie._, doobie.implicits._
import doobie.postgres._, doobie.postgres.implicits._
import cats._, cats.data._, cats.effect.IO, cats.implicits._
import io.circe._
import geotrellis.slick.Projected
import geotrellis.vector.MultiPolygon

import java.sql.Timestamp
import java.util.{Date, UUID}


object AoiDao {

  def select(id: UUID) =
    (Statements.select ++ fr"WHERE id = $id").query[AOI].unique

  type PageRequest = Nothing
  def list(
    pageRequest: PageRequest,
    queryParams: AoiQueryParameters,
    user: User
  ): fs2.Stream[ConnectionIO, AOI] = {
    val filters =
      Filters.organization(queryParams.orgParams) ++
      Filters.user(queryParams.userParams) ++
      Filters.timestamp(queryParams.timestampParams)

    (Statements.select ++ Fragments.whereAndOpt(filters: _*))
      .query[AOI]
      .process
  }


  def create(
    user: User,
    owner: Option[String],
    organizationId: UUID,
    area: Projected[MultiPolygon],
    filters: Json
  ): ConnectionIO[AOI] = {
    val id = UUID.randomUUID
    val now = new Timestamp((new java.util.Date()).getTime())
    val ownerId = Ownership.checkOwner(user, owner)
    val userId = user.id
    sql"""
      INSERT INTO aois
        (id, created_at, created_by, modified_at, modified_by,
        organization_id, owner, area, filters)
      VALUES
        ($id, $now, $userId, $now, $userId,
        $organizationId, $ownerId, $area, $filters)
    """.update.withUniqueGeneratedKeys[AOI](
      "id", "created_at", "created_by", "modified_at", "modified_by",
      "organization_id", "owner", "area", "filters"
    )
  }

  object Statements {
    val select = sql"""
      SELECT
        id, created_at, created_by, modified_at, modified_by,
        organization_id, owner, area, filters
      FROM
        aois
    """
  }
}

object AoiJson {
  import io.circe._
  import scala.concurrent.Future
  // Potential strategy for replacement of `AOI.Create`
  def create(
    aoiJson: Json,
    user: User
  )(implicit xa: Transactor[IO]): Either[DecodingFailure, Future[AOI]] = {
    val c = aoiJson.hcursor
    (c.get[Option[String]]("owner"),
     c.get[UUID]("organizationId"),
     c.get[Projected[MultiPolygon]]("area"),
     c.get[Json]("filters"))
       .mapN({ case (owner, organizationId, area, filters) =>
         val creation = AoiDao.create(user, owner, organizationId, area, filters)
         creation.transact(xa).unsafeToFuture()
       })
  }
}

