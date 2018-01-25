package com.azavea.rf.database

import com.azavea.rf.database.meta.RFMeta._
import com.azavea.rf.datamodel._

import doobie._, doobie.implicits._
import doobie.postgres._, doobie.postgres.implicits._
import cats._, cats.data._, cats.effect.IO, cats.implicits._

import java.util.UUID


object BandDao {

  def select(id: UUID) =
    (Statements.select ++ fr"WHERE id = $id").query[Band].unique

  def create(
    imageId: UUID,
    name: String,
    number: Int,
    wavelength: Array[Int]
  ): ConnectionIO[Band] = {
    val id = UUID.randomUUID
    sql"""
      INSERT INTO bands
        (id, image_id, name, number, wavelength)
      VALUES
        ($id, $imageId, $name, $number, $wavelength)
    """.update.withUniqueGeneratedKeys[Band](
      "id", "image_id", "name", "number", "wavelength"
    )
  }

  object Statements {
    val select = sql"""
      SELECT
        id, image_id, name, number, wavelength
      FROM
        bands
    """
  }
}

