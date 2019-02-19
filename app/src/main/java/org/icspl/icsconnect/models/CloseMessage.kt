package org.icspl.icsconnect.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

public class CloseMessage {


    @SerializedName("CloseMessage")
    @Expose
    var closeMessages: List<ClosedMSGDetails>? = null

    inner class ClosedMSGDetails {

        @SerializedName("Queryid")
        @Expose
        public var queryid: String? = null
        @SerializedName("Fromemp")
        @Expose
        public var fromemp: String? = null
        @SerializedName("Toemp")
        @Expose
        public var toemp: String? = null
        @SerializedName("Remarks")
        @Expose
        public var remarks: String? = null
        @SerializedName("sendfrm")
        @Expose
        public var sendfrm: String? = null
        @SerializedName("sendto")
        @Expose
        public var sendto: Any? = null
        @SerializedName("Status")
        @Expose
        public var status: String? = null
        @SerializedName("Closedate")
        @Expose
        public var closedate: String? = null
        @SerializedName("SrNo")
        @Expose
        public var srNo: Int? = null
        @SerializedName("File_att")
        @Expose
        public var fileAtt: Any? = null
        @SerializedName("id")
        @Expose
        public var id: String? = null
        @SerializedName("name")
        @Expose
        public var name: String? = null
        @SerializedName("role")
        @Expose
        public var role: String? = null
        @SerializedName("photo")
        @Expose
        public var photo: String? = null
    }

}
