package com.azavea.rf.database

import com.azavea.rf.database.meta.RFMeta._
import com.azavea.rf.datamodel.Scene

import doobie._, doobie.implicits._
import doobie.postgres._, doobie.postgres.implicits._
import cats._, cats.data._, cats.effect.IO, cats.implicits._


object SceneDao {
  object Statements {
    val select = sql"""
      SELECT
        id, created_at, created_by, modified_at, modified_by, owner,
        organization_id, ingest_size_bytes, visibility, tags,
        datasource, scene_metadata, name, title_footprint,
        data_footprint, metadata_files, ingest_location, filter_fields,
        status_fields
      FROM
        scenes
    """.query[Scene]
  }
}

