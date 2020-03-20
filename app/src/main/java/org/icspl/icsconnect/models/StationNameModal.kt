package org.icspl.icsconnect.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.icspl.icsconnect.models.StationNameModal.Station





class StationNameModal {
    @SerializedName("Station")
    @Expose
   val stations: List<Station>? = null
    inner class Station {
        @SerializedName("station")
        @Expose
         val station: String? = null


    }}