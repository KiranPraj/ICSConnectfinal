package org.icspl.icsconnect.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class DepartmentNameModal {
    @SerializedName("Department")
    @Expose
     val department: List<Department>? = null


    inner class Department
    {
        @SerializedName("Department")
        @Expose
         val departments: String? = null
    }

}