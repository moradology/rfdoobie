package com.azavea.rf.database


package object meta {
  trait RFMeta extends JtsMeta
      with CirceJsonbMeta
      with VisibilityEnumMeta
      with ExportStatusEnumMeta

  object RFMeta extends RFMeta
}
