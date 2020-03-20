package org.icspl.icsconnect.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class EmpNameModal {

    @SerializedName("Empname")
    @Expose
    val empname: List<Empname>? = null

    inner class Empname {
        @SerializedName("Emp_Name")
        @Expose
        val empName: String? = null
        @SerializedName("Emp_Code")
        @Expose
        val empCode: String? = null
    }
}
