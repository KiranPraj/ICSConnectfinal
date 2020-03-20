package org.icspl.icsconnect.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class DtGetViewExpensesDetails
{
    @SerializedName("dt_GetExpensesDetails")
    @Expose
     val dtGetExpensesDetails: List<DtGetViewExpensesDetails>? = null
    @SerializedName("Emp_Dt")
    @Expose
    val empDt: String? = null
    @SerializedName("totalamt")
    @Expose
     val totalamt: String? = null
    @SerializedName("Per_Expns")
    @Expose
     val perExpns: String? = null
    @SerializedName("Filename")
    @Expose
     val filename: String? = null
    @SerializedName("Expns_brows")
    @Expose
     val expnsBrows: String? = null
    @SerializedName("Status")
    @Expose
     val status: String? = null
    @SerializedName("Remarks")
    @Expose
     val remarks: String? = null
    @SerializedName("TransferRemark")
    @Expose
     val transferRemark: String? = null
}