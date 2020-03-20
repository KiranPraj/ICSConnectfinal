package org.icspl.icsconnect.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class GetTimesheetPlanActivity
{
    @SerializedName("OpenMessage")
    @Expose
    val openMessage: List<GetTimesheetPlanActivity>? = null
    @SerializedName("RowIndex")
    @Expose
    private val rowIndex: Int? = null
    @SerializedName("Activity")
    @Expose
    val activity: String? = null
}