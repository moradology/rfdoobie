package com.azavea.rf.database

import com.azavea.rf.database.meta.RFMeta._
import com.azavea.rf.datamodel.ToolTag

import doobie._, doobie.implicits._
import doobie.postgres._, doobie.postgres.implicits._
import cats._, cats.data._, cats.effect.IO, cats.implicits._

import java.util.UUID


object ToolTagDao {

  def select(id: UUID) =
    (Statements.select ++ fr"WHERE id = $id").query[ToolTag].unique

  object Statements {
    val select = sql"""
      SELECT
        id, created_at, modified_at, created_by, modified_by,
        organization_id, tag, owner
      FROM
        tool_tags
    """
  }
}

