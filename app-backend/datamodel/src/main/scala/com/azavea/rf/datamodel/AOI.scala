package com.azavea.rf.datamodel

import java.sql.Timestamp
import java.util.{Date, UUID}

import com.azavea.rf.bridge._
import geotrellis.slick.Projected
import geotrellis.vector.MultiPolygon
import io.circe._
import io.circe.generic.JsonCodec

// --- //

/** A Project's Area of Interest.
  * This represents an area on the map (a `MultiPolygon`) which a user has
  * set filters for.  If a new Scene entering the system passes these filters,
  * the Scene will be added to the user's Project in a "pending" state. If the
  * user then accepts a "pending" Scene, it will be added to their project.
  */
case class AOI(
  /* Database fields */
  id: UUID,
  createdAt: Timestamp,
  createdBy: String,
  modifiedAt: Timestamp,
  modifiedBy: String,
  organizationId: UUID,
  owner: String,

  /* Unique fields */
  area: Projected[MultiPolygon],
  filters: Json
)

