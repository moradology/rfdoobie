package com.azavea.rf.datamodel

import java.sql.Timestamp
import java.util.UUID

import com.azavea.rf.bridge._
import geotrellis.vector.MultiPolygon
import geotrellis.slick.Projected

import io.circe._
import io.circe.generic.JsonCodec


@JsonCodec
case class Scene(
  id: UUID,
  createdAt: java.sql.Timestamp,
  createdBy: String,
  modifiedAt: java.sql.Timestamp,
  modifiedBy: String,
  owner: String,
  organizationId: UUID,
  ingestSizeBytes: Int,
  visibility: Visibility,
  tags: List[String],
  datasource: UUID,
  sceneMetadata: Json,
  name: String,
  tileFootprint: Option[Projected[MultiPolygon]] = None,
  dataFootprint: Option[Projected[MultiPolygon]] = None,
  metadataFiles: List[String],
  ingestLocation: Option[String] = None,
  cloudCover: Option[Float] = None,
  acquisitionDate: Option[java.sql.Timestamp] = None,
  sunAzimuth: Option[Float] = None,
  sunElevation: Option[Float] = None,
  thumbnailStatus: JobStatus,
  boundaryStatus: JobStatus,
  ingestStatus: IngestStatus
)


