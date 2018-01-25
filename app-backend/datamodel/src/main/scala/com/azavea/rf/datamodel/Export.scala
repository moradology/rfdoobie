package com.azavea.rf.datamodel

import io.circe._
import io.circe.generic.JsonCodec
import cats.implicits._

import java.util.UUID
import java.sql.Timestamp

@JsonCodec
case class Export(
  id: UUID,
  createdAt: Timestamp,
  createdBy: String,
  modifiedAt: Timestamp,
  modifiedBy: String,
  owner: String,
  organizationId: UUID,
  projectId: Option[UUID],
  exportStatus: ExportStatus,
  exportType: ExportType,
  visibility: Visibility,
  toolRunId: Option[UUID],
  exportOptions: Json
) {
  def getExportOptions: Option[ExportOptions] = exportOptions.as[ExportOptions].toOption
}

object Export {

  def tupled = (Export.apply _).tupled

}
