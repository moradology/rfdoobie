package com.azavea.rf.database

import doobie._, doobie.implicits._
import cats._, cats.data._, cats.effect.IO, cats.implicits._


object FeatureFlagtDao {
  object Statements {
    val select: ConnectionIO[Band] = sql"""
      SELECT
        id, key, active, name, description
      FROM
        feature_flags
    """.query[Export]
  }
}

