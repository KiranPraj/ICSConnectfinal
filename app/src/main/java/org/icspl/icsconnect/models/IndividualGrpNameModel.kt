package org.icspl.icsconnect.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class IndividualGrpNameModel {

    @SerializedName("IndividualGroups")
    @Expose
    var individualGroupsList: List<IndividualGroup>? = null


    inner class IndividualGroup {

        @SerializedName("Srno")
        @Expose
        var srno: Int? = null
        @SerializedName("Group_id")
        @Expose
        var groupId: String? = null
        @SerializedName("Group_title")
        @Expose
        var groupTitle: String? = null
        @SerializedName("Group_admin")
        @Expose
        var groupAdmin: String? = null
        @SerializedName("Master_admin")
        @Expose
        var masterAdmin: String? = null
        @SerializedName("Create_date")
        @Expose
        var createDate: String? = null
        @SerializedName("Members")
        @Expose
        var members: String? = null

        var schecked:Boolean?=null

    }


}
