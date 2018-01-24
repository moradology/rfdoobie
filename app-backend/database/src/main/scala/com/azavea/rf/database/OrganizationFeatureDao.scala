package com.azavea.rf.database

import com.azavea.rf.database.meta.RFMeta._

import doobie._, doobie.implicits._
import doobie.postgres._, doobie.postgres.implicits._
import cats._, cats.data._, cats.effect.IO, cats.implicits._


object OrganizationFeatureDao {
  object Statements {
    val select = sql"""
      SELECT
        organization, feature_flag, active
      FROM
        organization_features
    """
  }
}

