package org.icspl.icsconnect.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class CompOffstatus{
    @SerializedName("CompOffStatus")
    @Expose
    val compOffStatus: List<CompOffstatus>? = null
    @SerializedName("Emp_Code")
    @Expose
    private val empCode: String? = null
    @SerializedName("Emp_Name")
    @Expose
     val empName: String? = null
    @SerializedName("Station")
    @Expose
     val station: String? = null
    @SerializedName("Comp")
    @Expose
     val comp: Double? = null
    @SerializedName("compDate")
    @Expose
    val compDate: String? = null
}