package com.azavea.rf.database

import doobie._, doobie.implicits._
import cats._, cats.data._, cats.effect.IO, cats.implicits._


object MapTokenDao {
  object Statements {
    val select: ConnectionIO[Band] = sql"""
      SELECT
        id, created_at, created_by, modified_at, modified_by,
        owner, organization_id, name, project, tool_run
      FROM
        map_tokens
    """.query[MapToken]
  }
}

