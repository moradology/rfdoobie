package com.azavea.rf.database.meta

import com.azavea.rf.datamodel.AOI
import com.azavea.rf.database._
import com.azavea.rf.database.meta.RFMeta._

import doobie._, doobie.implicits._
import cats._, cats.data._, cats.effect.IO
import cats.syntax.either._
import doobie.postgres._, doobie.postgres.implicits._
import doobie.util.invariant.InvalidObjectMapping
import doobie.scalatest.imports._
import org.scalatest._
import com.vividsolutions.jts.geom
import geotrellis.vector._


class JtsGeometryMetaSpec extends FunSpec with Matchers with DBTestConfig {

  Meta[geom.Polygon]
  Meta[geom.Point]
  Meta[geom.LineString]
  Meta[geom.MultiPolygon]
  case class GeometryClass(
    id: Int,
    poly: geom.Polygon,
    point: geom.Point,
    line: geom.LineString,
    multipoly: geom.MultiPolygon
  )

  val drop: Update0 =
  sql"""
    DROP TABLE IF EXISTS geom_test_table
  """.update

  val createTable = sql"""
    CREATE TABLE jtsgeom_test_table (
      id          integer                      NOT NULL UNIQUE,
      poly        geometry(Polygon, 3857)      NOT NULL,
      point       geometry(Point, 3857)        NOT NULL,
      line        geometry(LineString, 3857)   NOT NULL,
      multipoly   geometry(MultiPolygon, 3857) NOT NULL
    )
  """.update

  def insert(geomClass: GeometryClass) = sql"""
    INSERT INTO jtsgeom_test_table (id, poly, point, line, multipoly)
    VALUES (${geomClass.id}, ${geomClass.poly}, ${geomClass.point}, ${geomClass.line}, ${geomClass.multipoly})
  """.update

  def select(id: Int) = sql"""
    SELECT id, poly, point, line, multipoly FROM jtsgeom_test_table WHERE id = $id
  """.query[GeometryClass].unique

  it("should be able to go in and then come back out") {
    val point = Point(1, 2).jtsGeom
    point.setSRID(3857)
    val gtPoly = Polygon(Point(0, 0), Point(0, 1), Point(1, 1), Point(1, 0), Point(0, 0))
    val poly = gtPoly.jtsGeom
    poly.setSRID(3857)
    val line = Line(Point(0, 0), Point(0, 1), Point(1, 1)).jtsGeom
    line.setSRID(3857)
    val multipoly = MultiPolygon(gtPoly).jtsGeom
    multipoly.setSRID(3857)

    val geomOut = for {
      _  <- createTable.run
      _  <- insert(GeometryClass(123, poly, point, line, multipoly)).run
      js <- select(123)
    } yield js

    val results = geomOut.transact(xa).unsafeRunSync
    results.poly shouldBe poly
    results.point shouldBe point
    results.line shouldBe line
    results.multipoly shouldBe multipoly
  }
}

