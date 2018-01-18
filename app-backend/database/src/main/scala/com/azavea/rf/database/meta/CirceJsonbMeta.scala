package com.azavea.rf.database.meta

import com.azavea.rf.datamodel.AOI

import doobie._, doobie.implicits._
import cats._, cats.data._, cats.effect.IO
import cats.syntax.either._
import doobie.postgres._, doobie.postgres.implicits._//, doobie.postgres.pgisimplicits._
import doobie.util.invariant.InvalidObjectMapping
import org.postgis.jts._
import com.vividsolutions.jts.geom
import io.circe._
import doobie.scalatest.imports._
import org.scalatest._
import org.postgresql.util.PGobject

import scala.reflect.runtime.universe.TypeTag
import scala.reflect.ClassTag


trait CirceJsonbMeta {
  implicit val jsonbMeta: Meta[Json] =
  Meta.other[PGobject]("jsonb").xmap[Json](
    a => io.circe.parser.parse(a.getValue).leftMap[Json](e => throw e).merge, // failure raises an exception
    a => {
      val o = new PGobject
      o.setType("jsonb")
      o.setValue(a.noSpaces)
      o
    }
  )
}

object CirceJsonbMeta extends CirceJsonbMeta
