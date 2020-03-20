package org.icspl.icsconnect.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.icspl.icsconnect.models.RegionNameModal.Office





class RegionNameModal {
    @SerializedName("Office")
    @Expose
     val office: List<Office>? = null

    inner class Office{
        @SerializedName("region")
        @Expose
         val region: String? = null
    }
}