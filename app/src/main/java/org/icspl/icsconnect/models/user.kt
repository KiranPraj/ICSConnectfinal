/*
package org.icspl.icsconnect.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import java.io.Serializable
import java.time.LocalDateTime

*/
/*

@Entity
data class user {

    @PrimaryKey()
    var id: String?=null

    var password: String? = null
    var photo: String? = null
    var name: String? = null
    var masteradmin: String? = null
    var designation: String? = null
    var staion: String? = null
    var createddate:String?=null


}*//*

@Entity(tableName = "Usertable")
data class user(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    var password: String? = null,
    var photo: String? = null,
    var name: String? = null,
    var masteradmin: String? = null,
    var designation: String? = null,
    var staion: String? = null,
    var createddate:String?=null


)*/
