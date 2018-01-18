package com.azavea.rf.database


package object meta {
  object RFMeta extends GtVectorMeta
      with CirceJsonbMeta
      with VisibilityEnumMeta
      with ExportStatusEnumMeta
}
