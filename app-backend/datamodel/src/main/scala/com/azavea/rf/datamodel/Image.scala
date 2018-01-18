package com.azavea.rf.datamodel

import java.util.UUID
import java.sql.Timestamp

import io.circe._
import io.circe.generic.JsonCodec

@JsonCodec
case class Image(
  id: UUID,
  createdAt: Timestamp,
  createdBy: String,
  modifiedAt: Timestamp,
  modifiedBy: String,
  organizationId: UUID,
  owner: String,
  rawDataBytes: Long,
  visibility: Visibility,
  filename: String,
  sourceUri: String,
  scene: UUID,
  imageMetadata: Json,
  resolutionMeters: Float,
  metadataFiles: List[String]
) {
  def withRelatedFromComponents(bands: Seq[Band]): Image.WithRelated = Image.WithRelated(
    this.id,
    this.createdAt,
    this.createdBy,
    this.modifiedAt,
    this.modifiedBy,
    this.organizationId,
    this.owner,
    this.rawDataBytes,
    this.visibility,
    this.filename,
    this.sourceUri,
    this.scene,
    this.imageMetadata,
    this.resolutionMeters,
    this.metadataFiles,
    bands
  )
}

object Image {

  def create = Create.apply _

  def tupled = (Image.apply _).tupled

  @JsonCodec
  case class Create(
    organizationId: UUID,
    rawDataBytes: Long,
    visibility: Visibility,
    filename: String,
    sourceUri: String,
    scene: UUID,
    imageMetadata: Json,
    owner: Option[String],
    resolutionMeters: Float,
    metadataFiles: List[String]
  ) extends OwnerCheck {
    def toImage(user: User): Image = {
      val now = new Timestamp((new java.util.Date).getTime)

      val ownerId = checkOwner(user, this.owner)

      Image(
        UUID.randomUUID, // primary key
        now, // createdAt
        user.id, // createdBy: String,
        now, // modifiedAt
        user.id, // modifiedBy: String,
        organizationId,
        ownerId, // owner: String
        rawDataBytes,
        visibility,
        filename,
        sourceUri,
        scene,
        imageMetadata,
        resolutionMeters,
        metadataFiles
      )
    }
  }

  /** Image class when posted with bands */
  @JsonCodec
  case class Banded(
    organizationId: UUID,
    rawDataBytes: Long,
    visibility: Visibility,
    filename: String,
    sourceUri: String,
    owner: Option[String],
    scene: UUID,
    imageMetadata: Json,
    resolutionMeters: Float,
    metadataFiles: List[String],
    bands: Seq[Band.Create]
  ) {
    def toImage(user: User): Image = {
      Image.Create(
        organizationId,
        rawDataBytes,
        visibility,
        filename,
        sourceUri,
        scene,
        imageMetadata,
        owner,
        resolutionMeters,
        metadataFiles
      ).toImage(user)
    }
  }

  @JsonCodec
  case class WithRelated(
    id: UUID,
    createdAt: Timestamp,
    createdBy: String,
    modifiedAt: Timestamp,
    modifiedBy: String,
    organizationId: UUID,
    owner: String,
    rawDataBytes: Long,
    visibility: Visibility,
    filename: String,
    sourceUri: String,
    scene: UUID,
    imageMetadata: Json,
    resolutionMeters: Float,
    metadataFiles: List[String],
    bands: Seq[Band]
  ) {
    /** Helper method to extract the image component only for post requests */
    def toImage: Image = Image(
      id,
      createdAt,
      createdBy,
      modifiedAt,
      modifiedBy,
      organizationId,
      owner,
      rawDataBytes,
      visibility,
      filename,
      sourceUri,
      scene,
      imageMetadata,
      resolutionMeters,
      metadataFiles
    )

    def toDownloadable(downloadUri: String): Image.WithRelatedDownladable = Image.WithRelatedDownladable(
      this.id,
      this.createdAt,
      this.createdBy,
      this.modifiedAt,
      this.modifiedBy,
      this.organizationId,
      this.owner,
      this.rawDataBytes,
      this.visibility,
      this.filename,
      this.sourceUri,
      this.scene,
      this.imageMetadata,
      this.resolutionMeters,
      this.metadataFiles,
      this.bands,
      downloadUri
    )
  }


  object WithRelated {
    /** Helper function to create Iterable[Image.WithRelated] from join
      *
      * @param records result of join query to return image with related information
      */
    def fromRecords(records: Seq[(Image, Band)]): Iterable[Image.WithRelated] = {
      val distinctImages = records.map(_._1)
      val bands = records.map(_._2)
      distinctImages map { image =>
        image.withRelatedFromComponents(bands.filter(_.image == image.id))
      }
    }
  }

  @JsonCodec
  case class WithRelatedDownladable(
    id: UUID,
    createdAt: Timestamp,
    createdBy: String,
    modifiedAt: Timestamp,
    modifiedBy: String,
    organizationId: UUID,
    owner: String,
    rawDataBytes: Long,
    visibility: Visibility,
    filename: String,
    sourceUri: String,
    scene: UUID,
    imageMetadata: Json,
    resolutionMeters: Float,
    metadataFiles: List[String],
    bands: Seq[Band],
    downloadUri: String
  )
}

