package org.icspl.icsconnect.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GrpConversationModel {


    @SerializedName("Group_Messages")
    @Expose
    var groupMessages: List<GroupMessage>? = null


    inner class GroupMessage {

        @SerializedName("Srno")
        @Expose
        var srno: Int? = null
        @SerializedName("Group_id")
        @Expose
        var groupId: String? = null
        @SerializedName("Q_Message")
        @Expose
        var qMessage: String? = null
        @SerializedName("Q_attachment")
        @Expose
        var qAttachment: String? = null
        @SerializedName("Q_date")
        @Expose
        var qDate: String? = null
    }

}
