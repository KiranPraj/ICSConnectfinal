package org.icspl.icsconnect.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetleaveYear
{
    @SerializedName("Rowindex")
    @Expose
    val  rowindex: Int? =null
    @SerializedName("PfinYear")
    @Expose
   val pfinYear: String? =null


}