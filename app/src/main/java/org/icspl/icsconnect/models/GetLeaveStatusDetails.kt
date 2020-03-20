package org.icspl.icsconnect.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class GetLeaveStatusDetails
{
    @SerializedName("Emp_Code")
    @Expose
     val empCode: String? = null
    @SerializedName("Emp_Name")
    @Expose
     val empName: String? = null
    @SerializedName("Station")
    @Expose
     val station: String? = null
    @SerializedName("CL")
    @Expose
     val cL: String? = null
    @SerializedName("SL")
    @Expose
     val sL: String? = null
    @SerializedName("AL")
    @Expose
     val aL: String? = null
    @SerializedName("PL")
    @Expose
     val pL: String? = null
    @SerializedName("Compoff")
    @Expose
     val compoff: String? = null
}