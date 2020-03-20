package org.icspl.icsconnect.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class DtGetViewPunchDatastatus{

    @SerializedName("dt_GetViewPunchDataStatus")
    @Expose
     val dtGetViewPunchDataStatus: List<DtGetViewPunchDatastatus>? = null
    @SerializedName("RowIndex")
    @Expose
     val rowIndex: Int? = null
    @SerializedName("Date")
    @Expose
     val date: String? = null
    @SerializedName("EDay")
    @Expose
     val eDay: String? = null
    @SerializedName("InTime")
    @Expose
     val inTime: String? = null
    @SerializedName("OutTime")
    @Expose
     val outTime: String? = null
    @SerializedName("Workhours")
    @Expose
     val workhours: String? = null
}