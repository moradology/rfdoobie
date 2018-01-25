package com.azavea.rf.datamodel

import java.util.UUID
import java.sql.Timestamp

import io.circe.generic.JsonCodec

/**
  * Created by cbrown on 3/11/17.
  */

@JsonCodec
case class MapToken(
  id: UUID,
  createdAt: Timestamp,
  createdBy: String,
  modifiedAt: Timestamp,
  modifiedBy: String,
  owner: String,
  organizationId: UUID,
  name: String,
  project: Option[UUID],
  toolRun: Option[UUID]
)


object MapToken {
  def tupled = (MapToken.apply _).tupled
}
