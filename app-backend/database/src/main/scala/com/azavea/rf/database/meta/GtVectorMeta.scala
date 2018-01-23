package com.azavea.rf.database.meta

import com.azavea.rf.datamodel.AOI

import doobie._, doobie.implicits._
import cats._, cats.data._, cats.effect.IO
import cats.syntax.either._
import doobie.postgres._, doobie.postgres.implicits._, doobie.postgres.pgisimplicits._
import doobie.util.invariant.InvalidObjectMapping
import org.postgis.jts._
import com.vividsolutions.jts.geom
import io.circe._
import doobie.scalatest.imports._
import org.scalatest._
import org.postgresql.util.PGobject
import org.{postgis => pg}
import geotrellis.vector._
import geotrellis.slick.Projected
import geotrellis.vector.io.wkt.WKT

import scala.reflect.runtime.universe.TypeTag
import scala.reflect.ClassTag


trait GtVectorMeta {

  private def gtPoint2pgPoint(gtPoint: Point): pg.Point =
    new pg.Point(gtPoint.x, gtPoint.y)

  private def gtLine2pgLine(gtLine: Line): pg.LineString = {
    val pointArr = gtLine.points.map(gtPoint2pgPoint)
    new pg.LineString(pointArr)
  }

  private def gtPoly2pgPoly(gtPoly: Polygon): pg.Polygon = {
    val exterior = new pg.LinearRing(gtPoly.exterior.points.map(gtPoint2pgPoint))
    val holes: Array[pg.LinearRing] = gtPoly.holes.map({ hole =>
      new pg.LinearRing(hole.points.map(gtPoint2pgPoint))
    })
    new pg.Polygon(holes :+ exterior)
  }

  private def pgPoly2gtPoly(pgPoly: pg.Polygon): Polygon = {
    val rings = for {
      ring <- 0 to (pgPoly.numGeoms - 1)
    } yield {
      pgPoly.getSubGeometry(ring).asInstanceOf[pg.LinearRing]
    }

    val lines = rings.map({ ring =>
      val ringPoints = ring.getPoints.map({ pnt => Point(pnt.x, pnt.y) })
      Line(ringPoints)
    })

    Polygon(lines.head, lines.tail)
  }

  implicit val pointMeta = Meta[pg.Point].xmap[Projected[Point]](
    pgPoint => {
      Projected[Point](
        Point(pgPoint.x, pgPoint.y),
        pgPoint.getSrid
      )
    },
    gtPoint => {
      val point = gtPoint2pgPoint(gtPoint.geom)
      point.setSrid(gtPoint.srid)
      point
    }
  )

  implicit val lineMeta = Meta[pg.LineString].xmap[Projected[Line]](
    pgLine => {
      Projected[Line](
        Line(pgLine.getPoints.map({ pnt => Point(pnt.x, pnt.y) })),
        pgLine.getSrid
      )
    },
    gtLine => {
      val line = gtLine2pgLine(gtLine)
      line.setSrid(gtLine.srid)
      line
    }
  )

  implicit val polygonMeta = Meta[pg.Polygon].xmap[Projected[Polygon]](
    pgPoly => {
      Projected[Polygon](
        pgPoly2gtPoly(pgPoly),
        pgPoly.getSrid
      )
    },
    gtPoly => {
      val poly = gtPoly2pgPoly(gtPoly)
      poly.setSrid(gtPoly.srid)
      poly
    }
  )

  implicit val multipolygonMeta = Meta[pg.MultiPolygon].xmap[Projected[MultiPolygon]](
    pgMPoly => {
      val polygons = pgMPoly.getPolygons.map(pgPoly2gtPoly)
      Projected[MultiPolygon](
        MultiPolygon(polygons),
        pgMPoly.getSrid
      )
    },
    gtMPoly => {
      val multipoly = new pg.MultiPolygon(gtMPoly.polygons.map(gtPoly2pgPoly))
      multipoly.setSrid(gtMPoly.srid)
      multipoly
    }
  )

  implicit val geometryMeta = Meta[pg.Geometry].xmap[Projected[Geometry]](
    pgGeom => {
      Projected[Geometry](
        WKT.read(pgGeom.getValue),
        pgGeom.getSrid
      )
    },
    gtGeom => {
      val multipoly = new pg.MultiPolygon(gtMPoly.polygons.map(gtPoly2pgPoly))
      multipoly.setSrid(gtMPoly.srid)
      multipoly
    }
  )

}


