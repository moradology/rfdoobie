package com.azavea.rf.datamodel

import akka.http.scaladsl.unmarshalling._
import java.util.UUID

import io.circe._
import io.circe.generic.JsonCodec

@JsonCodec
case class Band(
  id: UUID,
  image: UUID,
  name: String,
  number: Int,
  wavelength: Array[Int]
)

object Band {//extends RangeUnmarshaler{
  def tupled = (Band.apply _).tupled

  @JsonCodec
  case class Identified(
    id: Option[UUID],
    imageId: UUID,
    name: String,
    number: Int,
    wavelength: Array[Int]
  ) {
    def toBand: Band = {
      Band(
        id.getOrElse(UUID.randomUUID),
        imageId,
        name,
        number,
        wavelength
      )
    }
  }

  object Identified
}
