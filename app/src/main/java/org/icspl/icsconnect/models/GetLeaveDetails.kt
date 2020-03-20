package org.icspl.icsconnect.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class GetLeaveDetails
{
    @SerializedName("LeaveType")
    @Expose
   val leaveType: String? = null
    @SerializedName("FromDate")
    @Expose
   val fromDate: String? = null
    @SerializedName("ToDate")
    @Expose
   val toDate: String? = null
    @SerializedName("Days")
    @Expose
   val days: String? = null
    @SerializedName("DateOfApply")
    @Expose
   val dateOfApply: String? = null
    @SerializedName("Reason")
    @Expose
   val reason: String? = null
    @SerializedName("status")
    @Expose
   val status: String? = null
    @SerializedName("Approver")
    @Expose
   val approver: String? = null
    @SerializedName("Compagainst")
    @Expose
   val compagainst: String? = null
    @SerializedName("halfdayagainst")
    @Expose
   val halfdayagainst: String? = null
}