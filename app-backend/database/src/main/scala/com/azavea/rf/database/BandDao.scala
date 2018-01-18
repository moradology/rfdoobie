package com.azavea.rf.database

import doobie._, doobie.implicits._
import cats._, cats.data._, cats.effect.IO, cats.implicits._


object BandDao {
  object Statements {
    val select: ConnectionIO[Band] = sql"""
      SELECT
        id, image_id, name, number, wavelength from bands
      FROM
        bands
    """
  }
}

