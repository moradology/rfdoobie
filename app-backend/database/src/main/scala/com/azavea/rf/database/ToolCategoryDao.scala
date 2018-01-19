package com.azavea.rf.database

import com.azavea.rf.database.meta.RFMeta._
import com.azavea.rf.datamodel.ToolCategory

import doobie._, doobie.implicits._
import doobie.postgres._, doobie.postgres.implicits._
import cats._, cats.data._, cats.effect.IO, cats.implicits._


object ToolCategoryDao {
  object Statements {
    val select = sql"""
      SELECT
        slug_label, created_at, created_by, modified_at, modified_by, category
      FROM
        tool_categories
    """
  }
}
