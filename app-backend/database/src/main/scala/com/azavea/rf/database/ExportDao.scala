package com.azavea.rf.database

import doobie._, doobie.implicits._
import cats._, cats.data._, cats.effect.IO, cats.implicits._


object ExportDao {
  object Statements {
    val select: ConnectionIO[Band] = sql"""
      SELECT
        id, created_at, created_by, modified_at, modified_by, owner,
        organization_id, project_id, export_status, export_type,
        visibility, tool_run_id, exportOptions
      FROM
        exports
    """.query[Export]
  }
}

