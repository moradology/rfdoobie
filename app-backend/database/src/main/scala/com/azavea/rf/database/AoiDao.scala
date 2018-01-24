package com.azavea.rf.database

import com.azavea.rf.database.meta.RFMeta._
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

  def create(
    user: User,
    owner: Option[String],
    organizationId: UUID,
    area: Projected[MultiPolygon],
    filters: Json
  ): ConnectionIO[AOI] = {
    val id = UUID.randomUUID
    val now = new Timestamp((new java.util.Date()).getTime())
    val ownerId = Util.checkOwner(user, owner)
    sql"""
      INSERT INTO aois
        (id, created_at, created_by, modified_at, modified_by,
        organization_id, owner, area, filters)
      VALUES
        ($id, $now, ${user.id}, $now, ${user.id},
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

