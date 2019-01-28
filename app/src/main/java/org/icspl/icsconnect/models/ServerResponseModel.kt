package org.icspl.icsconnect.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ServerResponseModel {
    @SerializedName("ServerResponse")
    @Expose
    var response: Int? = null
}
