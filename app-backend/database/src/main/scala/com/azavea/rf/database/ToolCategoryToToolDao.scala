package com.azavea.rf.database

import com.azavea.rf.database.meta.RFMeta._
import com.azavea.rf.datamodel.ToolCategoryToTool

import doobie._, doobie.implicits._
import doobie.postgres._, doobie.postgres.implicits._
import cats._, cats.data._, cats.effect.IO, cats.implicits._


object ToolCategoryToToolDao {
  object Statements {
    val select = sql"""
      SELECT
        tool_id, tool_category_slug
      FROM
        tool_categories_to_tools
    """
  }
}

