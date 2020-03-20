package org.icspl.icsconnect.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



 class OpenMessage
{
    @SerializedName("OpenMessage")
    @Expose
    val openMessage: List<OpenMessage>? = null

    @SerializedName("RowIndex")
    @Expose
     val rowIndex: Int? = null
    @SerializedName("EmpCode")
    @Expose
     val empCode: String? = null
    @SerializedName("EmpDate")
    @Expose
     val empDate: String? = null
    @SerializedName("ExitStatus")
    @Expose
     val exitStatus: String? = null
    @SerializedName("SentSalaryStatus")
    @Expose
     val sentSalaryStatus: String? = null
    @SerializedName("ProtectDateStatus")
    @Expose
     val protectDateStatus: String? = null
    @SerializedName("SundayStatus")
    @Expose
     val sundayStatus: String? = null
    @SerializedName("SaturdayStatus")
    @Expose
     val saturdayStatus: String? = null
    @SerializedName("ICSHolidayStatus")
    @Expose
     val iCSHolidayStatus: String? = null
    @SerializedName("DocUploadStatus")
    @Expose
     val docUploadStatus: String? = null
    @SerializedName("PrevoiusFilled")
    @Expose
    val prevoiusFilled: String? = null


}