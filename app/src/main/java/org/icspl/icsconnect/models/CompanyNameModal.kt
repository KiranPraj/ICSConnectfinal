package org.icspl.icsconnect.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CompanyNameModal {

    @SerializedName("Companies")
    @Expose
     var companies: List<Company>? = null
    inner class Company {
        @SerializedName("GroupCompanies")
        @Expose
        var groupCompanies: String? = null
    }
}

