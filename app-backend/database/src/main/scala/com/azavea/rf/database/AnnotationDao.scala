package com.azavea.rf.database

import com.azavea.rf.database.meta.RFMeta._
import com.azavea.rf.datamodel.{Annotation, AnnotationQuality}

import doobie._, doobie.implicits._
import doobie.postgres._, doobie.postgres.implicits._
import cats._, cats.data._, cats.effect.IO, cats.implicits._

import java.util.UUID

object AnnotationDao {
  def create(
    owner: Option[String],
    organizationId: UUID,
    label: String,
    description: Option[String],
    machineGenerated: Option[Boolean],
    confidence: Option[Double],
    quality: Option[AnnotationQuality]
  ): Annotation = ???

  object Statements {
    val select = sql"""
      SELECT
        id, project_id, created_at, created_by, modified_at, modified_by, owner,
        organization_id, label, description, machine_generated, confidence,
        quality, geometry
      FROM
        annotations
    """
  }
}

