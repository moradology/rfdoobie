package com.azavea.rf.database


object AoiDao {
  object Statements {
    val select = sql"""
      SELECT
        id, created_at, modified_at, created_by, modified_by,
        organization_id, owner, area, filters
      FROM
        aois
    """.query[AOI]
  }
}
