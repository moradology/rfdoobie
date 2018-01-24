package com.azavea.rf.database

import com.azavea.rf.database.meta.RFMeta._
import com.azavea.rf.datamodel.ToolTag

import doobie._, doobie.implicits._
import doobie.postgres._, doobie.postgres.implicits._
import cats._, cats.data._, cats.effect.IO, cats.implicits._


object ToolTagDao {
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

