package org.icspl.icsconnect.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class DtGetViewTstatus {
    @SerializedName("dt_GetViewTStatus")
    @Expose
     val dtGetViewTStatus: List<DtGetViewTstatus>? = null
    @SerializedName("Month")
    @Expose
     val month: String? = null
    @SerializedName("SentDate")
    @Expose
     val sentDate: String? = null
    @SerializedName("Status")
    @Expose
     val status: String? = null

}