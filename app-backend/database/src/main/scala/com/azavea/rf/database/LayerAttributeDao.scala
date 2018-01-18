
package com.azavea.rf.database

import doobie._, doobie.implicits._
import cats._, cats.data._, cats.effect.IO, cats.implicits._


object LayerAttributeDao {
  object Statements {
    val select: ConnectionIO[Band] = sql"""
      SELECT
        layer_name, zoom, name, value
      FROM
        layer_attributes
    """.query[LayerAttribute]
  }
}

