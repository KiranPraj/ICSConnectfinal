package org.icspl.icsconnect.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SearchModel {

    @SerializedName("name")
    @Expose
     val name: String? = null
    @SerializedName("id")
    @Expose
     val id: String? = null

}
