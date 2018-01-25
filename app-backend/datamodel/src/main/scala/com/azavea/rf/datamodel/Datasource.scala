package com.azavea.rf.datamodel

import io.circe._
import java.util.UUID
import java.sql.Timestamp

import io.circe.generic.JsonCodec

@JsonCodec
case class Datasource(
  id: UUID,
  createdAt: java.sql.Timestamp,
  createdBy: String,
  modifiedAt: java.sql.Timestamp,
  modifiedBy: String,
  owner: String,
  organizationId: UUID,
  name: String,
  visibility: Visibility,
  composites: Json,
  extras: Json,
  bands: Json
)

object Datasource {

  def tupled = (Datasource.apply _).tupled

}
