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
import geotrellis.vector._

import scala.reflect.runtime.universe.TypeTag
import scala.reflect.ClassTag


trait GtVectorMeta {

  private val jtsGeometryType = Meta.other[JtsGeometry]("geometry")

  // Constructor for geometry types via the `Geometry` member of JTSgeometry
  private def geometryType[A >: Null <: geom.Geometry: TypeTag, B >: Null <: Geometry: TypeTag](wrapperF: A => B)(implicit A: ClassTag[A]): Meta[B] =
    jtsGeometryType.xmap[B](
      { g =>
        try {
          val jtsGeom = A.runtimeClass.cast(g.getGeometry).asInstanceOf[A]
          wrapperF(jtsGeom)
        } catch {
          case _: ClassCastException => throw InvalidObjectMapping(A.runtimeClass, g.getGeometry.getClass)
        }
      },
      { gtGeom: Geometry => new JtsGeometry(gtGeom.jtsGeom) }
    )

  // PostGIS Geometry Types
  implicit val GeometryType           = geometryType[geom.Geometry, Geometry](Geometry.apply _)
  implicit val GeometryCollectionType = geometryType[geom.GeometryCollection, GeometryCollection](GeometryCollection.apply _)
  implicit val MultiLineStringType    = geometryType[geom.MultiLineString, MultiLine](MultiLine.apply _)
  implicit val MultiPolygonType       = geometryType[geom.MultiPolygon, MultiPolygon](MultiPolygon.apply _)
  implicit val LineStringType         = geometryType[geom.LineString, Line](Line.apply _)
  implicit val MultiPointType         = geometryType[geom.MultiPoint, MultiPoint](MultiPoint.apply _)
  implicit val PolygonType            = geometryType[geom.Polygon, Polygon](Polygon.apply _)
  implicit val PointType              = geometryType[geom.Point, Point](Point.apply _)

}

