package com.azavea.rf.database

import com.azavea.rf.database.meta.RFMeta._
import com.azavea.rf.datamodel.LayerAttribute

import doobie._, doobie.implicits._
import doobie.postgres._, doobie.postgres.implicits._
import cats._, cats.data._, cats.effect.IO, cats.implicits._


object LayerAttributeDao {
  object Statements {
    val select = sql"""
      SELECT
        layer_name, zoom, name, value
      FROM
        layer_attributes
    """
  }
}

