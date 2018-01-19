package com.azavea.rf.database

import com.azavea.rf.database.meta.RFMeta._
import com.azavea.rf.datamodel.Thumbnail

import doobie._, doobie.implicits._
import doobie.postgres._, doobie.postgres.implicits._
import cats._, cats.data._, cats.effect.IO, cats.implicits._


object ThumbnailDao {
  object Statements {
    val select = sql"""
      SELECT
        id, created_at, modified_at, organization_id, width_px, height_px,
        scene_id, url, thumbnail_size
      FROM
        thumbnails
    """
  }
}

