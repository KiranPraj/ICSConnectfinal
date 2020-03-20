package org.icspl.icsconnect.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetLeaveStatus
{


    @SerializedName("LeaveStatus")
    @Expose
    val leaveStatus: List<GetLeaveStatus>? = null


    @SerializedName("LeaveCount")
    @Expose
    val leaveCount: Double? = null
}