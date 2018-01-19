package com.azavea.rf.database

import com.azavea.rf.database.meta.RFMeta._
import com.azavea.rf.datamodel.FeatureFlag

import doobie._, doobie.implicits._
import doobie.postgres._, doobie.postgres.implicits._
import cats._, cats.data._, cats.effect.IO, cats.implicits._

object FeatureFlagtDao {
  object Statements {
    val select = sql"""
      SELECT
        id, key, active, name, description
      FROM
        feature_flags
    """
  }
}

