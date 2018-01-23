package com.azavea.rf.database


package object meta {
  trait RFMeta extends GtWktMeta
      with CirceJsonbMeta
      with VisibilityEnumMeta
      with ExportStatusEnumMeta
      with SingleBandOptionsMeta

  object RFMeta extends RFMeta
}
