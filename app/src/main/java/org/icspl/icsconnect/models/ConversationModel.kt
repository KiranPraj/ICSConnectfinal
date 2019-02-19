package org.icspl.icsconnect.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ConversationModel {
    @SerializedName("ChatMessages")
    @Expose
    var chatMessages: List<ChatMessage>? = null

    @SerializedName("CloseButton")
    @Expose
    var ShowCloseButton: List<ChatMessage>? = null

    inner class ChatMessage {

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
        @SerializedName("SrNo")
        @Expose
        var srNo: Int? = null
        @SerializedName("File_att")
        @Expose
        var fileAtt: String? = null
    }
}
