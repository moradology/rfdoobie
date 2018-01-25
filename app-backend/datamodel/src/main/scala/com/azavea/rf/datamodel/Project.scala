package com.azavea.rf.datamodel

import java.sql.Timestamp
import java.util.UUID

import com.azavea.rf.bridge._
import cats.implicits._
import cats.syntax.either._
import geotrellis.slick.Projected
import geotrellis.vector.Polygon
import geotrellis.vector.io.json.GeoJsonSupport
import io.circe._
import io.circe.generic.semiauto._
import io.circe.generic.JsonCodec

// --- //

case class Project(
  id: UUID,
  createdAt: Timestamp,
  modifiedAt: Timestamp,
  organizationId: UUID,
  createdBy: String,
  modifiedBy: String,
  owner: String,
  name: String,
  slugLabel: String,
  description: String,
  visibility: Visibility,
  tileVisibility: Visibility,
  isAOIProject: Boolean,
  aoiCadenceMillis: Long, /* Milliseconds */
  aoisLastChecked: Timestamp,
  tags: List[String] = List.empty,
  extent: Option[Projected[Polygon]] = None,
  manualOrder: Boolean = true,
  isSingleBand: Boolean = false,
  singleBandOptions: Option[SingleBandOptions.Params]
)

/** Case class for project creation */
object Project extends GeoJsonSupport {

  /* One week, in milliseconds */
  val DEFAULT_CADENCE: Long = 604800000

  def tupled = (Project.apply _).tupled

  def slugify(input: String): String = {
    import java.text.Normalizer
    Normalizer.normalize(input, Normalizer.Form.NFD)
      .replaceAll("[^\\w\\s-]", ""
        .replace('-', ' ')
        .trim
        .replaceAll("\\s+", "-")
        .toLowerCase)
  }
}
