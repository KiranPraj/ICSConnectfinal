package org.icspl.icsconnect.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SearchModel {

    @SerializedName("Emp_Name")
    @Expose
     val name: String? = null
    @SerializedName("Emp_Code")
    @Expose
     val id: String? = null

}
