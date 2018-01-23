package com.azavea.rf.database

import com.azavea.rf.database.meta.RFMeta._
import com.azavea.rf.datamodel.Export

import doobie._, doobie.implicits._
import doobie.postgres._, doobie.postgres.implicits._
import cats._, cats.data._, cats.effect.IO, cats.implicits._


object ExportDao {
  object Statements {
    val select = sql"""
      SELECT
        id, created_at, created_by, modified_at, modified_by, owner,
        organization_id, project_id, export_status, export_type,
        visibility, toolrun_id, export_options
      FROM
        exports
    """
  }
}

