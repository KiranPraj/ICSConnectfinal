package org.icspl.icsconnect.models

data class GroupChat(
    var type: ArrayList<String>,
    var text: String,
    var time: String,
    var photoPath: String,
    var doccument: String?
)
