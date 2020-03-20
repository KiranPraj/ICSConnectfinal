package org.icspl.icsconnect.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class TimesheetMessage{
    @SerializedName("TimesheetMessage")
    @Expose
     val timesheetMessage: List<TimesheetMessage>? = null
    @SerializedName("MSG_Status")
    @Expose
     val mSGStatus: String? = null
}