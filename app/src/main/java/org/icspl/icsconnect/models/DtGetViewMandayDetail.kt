package org.icspl.icsconnect.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class DtGetViewMandayDetail
{

    @SerializedName("dt_GetMandayDetails")
    @Expose
    val dtGetMandayDetails: List<DtGetViewMandayDetail>? = null
    @SerializedName("Emp_Code")
    @Expose
     val empCode: String? = null
    @SerializedName("Emp_Name")
    @Expose
     val empName: String? = null
    @SerializedName("Station")
    @Expose
     val station: String? = null
    @SerializedName("CFR_Date")
    @Expose
     val cFRDate: String? = null
    @SerializedName("ClientName")
    @Expose
     val clientName: String? = null
    @SerializedName("RegNo")
    @Expose
     val regNo: String? = null
    @SerializedName("TOC")
    @Expose
     val tOC: String? = null
    @SerializedName("Typeofactivity")
    @Expose
     val typeofactivity: String? = null
    @SerializedName("MrktInc_Status")
    @Expose
     val mrktIncStatus: String? = null
    @SerializedName("Mrktr_Name")
    @Expose
     val mrktrName: String? = null
    @SerializedName("Manday")
    @Expose
     val manday: String? = null
}