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


trait JtsMeta {

  implicit val jtsGeometryTypeAdapter = Meta.other[JtsGeometry]("geometry")

  // Constructor for geometry types via the `Geometry` member of JTSgeometry
  private def geometryType[A >: Null <: geom.Geometry: TypeTag](implicit A: ClassTag[A]): Meta[A] =
    jtsGeometryTypeAdapter.xmap[A](g =>
      try A.runtimeClass.cast(g.getGeometry).asInstanceOf[A]
      catch {
        case _: ClassCastException => throw InvalidObjectMapping(A.runtimeClass, g.getGeometry.getClass)
      },
      new JtsGeometry(_)
    )

  // PostGIS Geometry Types
  implicit val jtsGeometryType           = geometryType[geom.Geometry]
  implicit val jtsGeometryCollectionType = geometryType[geom.GeometryCollection]
  implicit val jtsMultiLineStringType    = geometryType[geom.MultiLineString]
  implicit val jtsMultiPolygonType       = geometryType[geom.MultiPolygon]
  implicit val jtsLineStringType         = geometryType[geom.LineString]
  implicit val jtsMultiPointType         = geometryType[geom.MultiPoint]
  implicit val jtsPolygonType            = geometryType[geom.Polygon]
  implicit val jtsPointType              = geometryType[geom.Point]

}

