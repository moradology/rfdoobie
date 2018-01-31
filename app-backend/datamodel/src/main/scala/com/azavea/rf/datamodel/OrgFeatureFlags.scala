package com.azavea.rf.datamodel

import java.util.UUID

case class OrgFeature(
  organization: UUID,
  featureFlag: UUID,
  active: Boolean
)

