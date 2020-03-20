package org.icspl.icsconnect.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class DtGetViewInspectionDetail
{

    @SerializedName("dt_GetInspectionDetails")
    @Expose
   val dtGetInspectionDetails: List<DtGetViewInspectionDetail>? = null
    @SerializedName("Emp_Dt")
    @Expose
     val empDt: String? = null
    @SerializedName("Type_Ol")
    @Expose
     val typeOl: String? = null
    @SerializedName("Ol_Name")
    @Expose
     val olName: String? = null
    @SerializedName("Ol_Region")
    @Expose
     val olRegion: String? = null
    @SerializedName("Ol_CustID")
    @Expose
     val olCustID: String? = null
    @SerializedName("Ol_Zone")
    @Expose
     val olZone: String? = null
    @SerializedName("Ol_Srgn")
    @Expose
     val olSrgn: String? = null
    @SerializedName("Range_Sts")
    @Expose
     val rangeSts: String? = null
    @SerializedName("Expenses")
    @Expose
     val expenses: String? = null
    @SerializedName("ReportNo")
    @Expose
     val reportNo: String? = null
    @SerializedName("Invoicetype")
    @Expose
     val invoicetype: String? = null
    @SerializedName("Quantity")
    @Expose
     val quantity: String? = null
}