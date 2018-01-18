package com.azavea.rf.database

import doobie._, doobie.implicits._
import cats._, cats.data._, cats.effect.IO, cats.implicits._


object ImageDao {
  object Statements {
    val select: ConnectionIO[Band] = sql"""
      SELECT
        id, created_at, created_by, modified_at, modified_by, organization_id,
        owner, raw_data_bytes, visibility, filename, source_uri, scene,
        image_metadata, resolution_meters, metadata_files
      FROM
        images
    """.query[Image]
  }
}

