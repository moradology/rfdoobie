package com.azavea.rf.database

import com.azavea.rf.database.meta.RFMeta._
import com.azavea.rf.datamodel.Band

import doobie._, doobie.implicits._
import doobie.postgres._, doobie.postgres.implicits._
import cats._, cats.data._, cats.effect.IO, cats.implicits._


object BandDao {
  object Statements {
    val select = sql"""
      SELECT
        id, image_id, name, number, wavelength from bands
      FROM
        bands
    """.query[Band]
  }
}

