package org.icspl.icsconnect.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class EmpStatusNameModal {

    @SerializedName("Empstatus")
    @Expose
    public val empstatuss: List<Empstatus>? = null
    inner class Empstatus{
        @SerializedName("EmpStatus")
        @Expose
        public val empStatus: String? = null

    }
}