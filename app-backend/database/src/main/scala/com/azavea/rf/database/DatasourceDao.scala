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


object DatasourceDao {

  def select(id: UUID) =
    (Statements.select ++ fr"WHERE id = $id").query[Datasource].unique

  def create(
    user: User,
    organizationId: UUID,
    name: String,
    visibility: Visibility,
    owner: Option[String],
    composites: Json,
    extras: Json,
    bands: Json
  ): ConnectionIO[Datasource] = {
    val id = UUID.randomUUID
    val now = new Timestamp((new java.util.Date()).getTime())
    val ownerId = util.Ownership.checkOwner(user, owner)
    sql"""
      INSERT INTO datasources
        (id, created_at, created_by, modified_at, modified_by, owner,
        organization_id, name, visibility, composites, extras, bands)
      VALUES
        ($id, $now, ${user.id}, $now, ${user.id}, $ownerId,
        $organizationId, $name, $visibility, $composites, $extras, $bands)
    """.update.withUniqueGeneratedKeys[Datasource](
        "id", "created_at", "created_by", "modified_at", "modified_by", "owner",
        "organization_id", "name", "visibility", "composites", "extras", "bands"
    )
  }

  object Statements {
    val select = sql"""
      SELECT
        id, created_at, created_by, modified_at, modified_by, owner,
        organization_id, name, visibility, composites, extras, bands
      FROM
        datasources
    """
  }
}

