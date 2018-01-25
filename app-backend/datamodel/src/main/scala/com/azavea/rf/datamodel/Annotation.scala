package com.azavea.rf.datamodel

import java.util.UUID
import java.sql.Timestamp

import io.circe._
import io.circe.generic.JsonCodec
import io.circe.generic.extras._

import geotrellis.slick.Projected
import geotrellis.vector.Geometry

@JsonCodec
case class Annotation(
  id: UUID,
  projectId: UUID,
  createdAt: Timestamp,
  createdBy: String,
  modifiedAt: Timestamp,
  modifiedBy: String,
  owner: String,
  organizationId: UUID,
  label: String,
  description: Option[String],
  machineGenerated: Option[Boolean],
  confidence: Option[Double],
  quality: Option[AnnotationQuality],
  geometry: Option[Projected[Geometry]]
) extends GeoJSONSerializable[Annotation.GeoJSON] {
    def toGeoJSONFeature: Annotation.GeoJSON = {
        Annotation.GeoJSON(
            this.id,
            this.geometry,
            AnnotationProperties(
                this.projectId,
                this.createdAt,
                this.createdBy,
                this.modifiedAt,
                this.modifiedBy,
                this.owner,
                this.organizationId,
                this.label,
                this.description,
                this.machineGenerated,
                this.confidence,
                this.quality
            ),
            "Feature"
        )
    }
}

@JsonCodec
case class AnnotationProperties(
    projectId: UUID,
    createdAt: Timestamp,
    createdBy: String,
    modifiedAt: Timestamp,
    modifiedBy: String,
    owner: String,
    organizationId: UUID,
    label: String,
    description: Option[String],
    machineGenerated: Option[Boolean],
    confidence: Option[Double],
    quality: Option[AnnotationQuality]
)

@JsonCodec
case class AnnotationPropertiesCreate(
    owner: Option[String],
    organizationId: UUID,
    label: String,
    description: Option[String],
    machineGenerated: Option[Boolean],
    confidence: Option[Double],
    quality: Option[AnnotationQuality]
)


object Annotation {

    //implicit val config: Configuration = Configuration.default.copy(
    //  transformKeys = {
    //    case "_type" => "type"
    //    case other => other
    //  }
    //)

    def tupled = (Annotation.apply _).tupled


    case class GeoJSON(
        id: UUID,
        geometry: Option[Projected[Geometry]],
        properties: AnnotationProperties,
        _type: String = "Feature"
    ) extends GeoJSONFeature

    @JsonCodec
    case class GeoJSONFeatureCreate(
        geometry: Option[Projected[Geometry]],
        properties: AnnotationPropertiesCreate
    ) extends OwnerCheck
}
