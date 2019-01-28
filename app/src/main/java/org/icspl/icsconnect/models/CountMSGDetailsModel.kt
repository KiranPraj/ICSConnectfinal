package org.icspl.icsconnect.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CountMSGDetailsModel {


    @SerializedName("IndividualDetails")
    @Expose
    var individualDetails: List<IndividualDetail>? = null


    public class IndividualDetail():Parcelable {

        @SerializedName("Queryid")
        @Expose
        var queryid: String? = null
        @SerializedName("Fromemp")
        @Expose
        var fromemp: String? = null
        @SerializedName("Toemp")
        @Expose
        var toemp: String? = null
        @SerializedName("Remarks")
        @Expose
        var remarks: String? = null
        @SerializedName("sendfrm")
        @Expose
        var sendfrm: String? = null
        @SerializedName("sendto")
        @Expose
        var sendto: Any? = null
        @SerializedName("Status")
        @Expose
        var status: String? = null
        @SerializedName("Closedate")
        @Expose
        var closedate: Any? = null
        @SerializedName("FromEmpName")
        @Expose
        var fromEmpName: String? = null
        @SerializedName("ToEmpName")
        @Expose
        var toEmpName: String? = null
        var photoPath: String? = null

        constructor(parcel: Parcel) : this() {
            queryid = parcel.readString()
            fromemp = parcel.readString()
            toemp = parcel.readString()
            remarks = parcel.readString()
            sendfrm = parcel.readString()
            status = parcel.readString()
            fromEmpName = parcel.readString()
            toEmpName = parcel.readString()
            photoPath = parcel.readString()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(queryid)
            parcel.writeString(fromemp)
            parcel.writeString(toemp)
            parcel.writeString(remarks)
            parcel.writeString(sendfrm)
            parcel.writeString(status)
            parcel.writeString(fromEmpName)
            parcel.writeString(toEmpName)
            parcel.writeString(photoPath)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<IndividualDetail> {
            override fun createFromParcel(parcel: Parcel): IndividualDetail {
                return IndividualDetail(parcel)
            }

            override fun newArray(size: Int): Array<IndividualDetail?> {
                return arrayOfNulls(size)
            }
        }

    }
}
