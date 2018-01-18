package com.azavea.rf.database

import doobie._, doobie.implicits._
import cats._, cats.data._, cats.effect.IO, cats.implicits._


object OrganizationDao {
  object Statements {
    val select = sql"""
      SELECT
        id, created_at, modified_at, name
      FROM
        organizations
    """.query[Organization]
  }
}

