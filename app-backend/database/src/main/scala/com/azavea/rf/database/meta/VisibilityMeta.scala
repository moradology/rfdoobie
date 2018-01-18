package com.azavea.rf.database.meta

import com.azavea.rf.datamodel.Visibility

import doobie._, doobie.implicits._
import cats._, cats.data._, cats.effect.IO
import cats.syntax.either._
import doobie.postgres._, doobie.postgres.implicits._//, doobie.postgres.pgisimplicits._
import doobie.util.invariant.InvalidObjectMapping
import org.postgis.jts._
import com.vividsolutions.jts.geom
import doobie.scalatest.imports._
import org.scalatest._
import org.postgresql.util.PGobject

import scala.reflect.runtime.universe.TypeTag
import scala.reflect.ClassTag


trait VisibilityEnumMeta {
  implicit val visibilityEnumMeta: Meta[Visibility] =
  Meta.other[String]("Visibility").xmap[Visibility](
    str => Visibility.fromString(str),
    vis => vis.repr
  )
}

object VisibilityEnumMeta extends VisibilityEnumMeta
