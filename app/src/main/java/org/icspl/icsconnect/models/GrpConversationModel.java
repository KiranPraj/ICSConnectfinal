package org.icspl.icsconnect.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GrpConversationModel {


    @SerializedName("Group_Messages")
    @Expose
    public List<GroupMessage> groupMessages = null;


    public class GroupMessage {

        @SerializedName("Srno")
        @Expose
        public Integer srno;
        @SerializedName("Group_id")
        @Expose
        public String groupId;
        @SerializedName("Q_Message")
        @Expose
        public String qMessage;
        @SerializedName("Q_attachment")
        @Expose
        public String qAttachment;
        @SerializedName("Q_date")
        @Expose
        public String qDate;
    }

}
