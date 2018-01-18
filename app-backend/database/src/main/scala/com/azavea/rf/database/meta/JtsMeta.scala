package com.azavea.rf.database

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

  implicit val jtsGeometryType = Meta.other[JtsGeometry]("geometry")

  // Constructor for geometry types via the `Geometry` member of JTSgeometry
  private def geometryType[A >: Null <: geom.Geometry: TypeTag](implicit A: ClassTag[A]): Meta[A] =
    jtsGeometryType.xmap[A](g =>
      try A.runtimeClass.cast(g.getGeometry).asInstanceOf[A]
      catch {
        case _: ClassCastException => throw InvalidObjectMapping(A.runtimeClass, g.getGeometry.getClass)
      },
      new JtsGeometry(_)
    )

  // PostGIS Geometry Types
  implicit val GeometryType           = geometryType[geom.Geometry]
  implicit val GeometryCollectionType = geometryType[geom.GeometryCollection]
  implicit val MultiLineStringType    = geometryType[geom.MultiLineString]
  implicit val MultiPolygonType       = geometryType[geom.MultiPolygon]
  implicit val LineStringType         = geometryType[geom.LineString]
  implicit val MultiPointType         = geometryType[geom.MultiPoint]
  implicit val PolygonType            = geometryType[geom.Polygon]
  implicit val PointType              = geometryType[geom.Point]

}

object JtsMeta extends JtsMeta
