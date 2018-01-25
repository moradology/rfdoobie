package com.azavea.rf.database

import com.azavea.rf.database.meta.RFMeta._
import com.azavea.rf.datamodel._

import doobie._, doobie.implicits._
import doobie.postgres._, doobie.postgres.implicits._
import cats._, cats.data._, cats.effect.IO, cats.implicits._

import java.util.UUID
import java.sql.Timestamp


object OrganizationDao {

  def create(
    name: String
  ): ConnectionIO[Organization] = {
    val id = UUID.randomUUID()
    val now = new Timestamp((new java.util.Date()).getTime())
    sql"""
      INSERT INTO organizations
        (id, created_at, modified_at, name)
      VALUES
        ($id, $now, $now, $name)
    """.update.withUniqueGeneratedKeys[Organization](
      "id", "created_at", "modified_at", "name"
    )
  }

  def select(id: UUID) =
    (Statements.select ++ fr"WHERE id = $id").query[Organization].unique

  object Statements {
    val select = sql"""
      SELECT
        id, created_at, modified_at, name
      FROM
        organizations
    """
  }
}

