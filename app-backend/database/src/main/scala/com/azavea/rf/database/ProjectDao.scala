package com.azavea.rf.database

import com.azavea.rf.database.meta.RFMeta._
import com.azavea.rf.datamodel.Project

import doobie._, doobie.implicits._
import doobie.postgres._, doobie.postgres.implicits._
import cats._, cats.data._, cats.effect.IO, cats.implicits._


object ProjectDao {
  object Statements {
    val select = sql"""
      SELECT
        id, created_at, modified_at, organization_id, created_by,
        modified_by, owner, name, slug_label, description,
        visibility, tile_visibility, is_aoi_project,
        aoi_cadence_millis, aois_last_checked, tags, extent,
        manual_order, is_single_band, single_band_options
      FROM
        projects
    """
  }
}

