package com.azavea.rf.database

import com.azavea.rf.database.meta.RFMeta._
import com.azavea.rf.datamodel.SceneToProject

import doobie._, doobie.implicits._
import doobie.postgres._, doobie.postgres.implicits._
import cats._, cats.data._, cats.effect.IO, cats.implicits._


object SceneToProjectDao {
  object Statements {
    val select = sql"""
      SELECT
        scene_id, project_id, scene_order, mosaic_definition, accepted
      FROM
        scenes_to_projects
    """
  }
}

