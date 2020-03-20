package org.icspl.icsconnect.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class CountMSGModel {

  @SerializedName("Countmessage")
  @Expose
  public val countmessageList: List<Countmessage>? = null

  @SerializedName("Sendbyyou")
  @Expose
  public val sendbyyou: List<Sendbyyou>? = null

  inner class Sendbyyou {

    @SerializedName("Fromemp")
    @Expose
    public val fromemp: String? = null
    @SerializedName("FromEmpName")
    @Expose
    public val fromEmpName: String? = null
    @SerializedName("ToEmpName")
    @Expose
    public val toEmpName: String? = null
    @SerializedName("Toemp")
    @Expose
    public val toemp: String? = null
    @SerializedName("photo")
    @Expose
    public val photo: String? = null
    @SerializedName("countmessage")
    @Expose
    public val countmessage: Int? = null
    public val isIAm: Boolean = false
  }


  inner class Countmessage {

    @SerializedName("Fromemp")
    @Expose
    public var fromemp: String? = null
    @SerializedName("FromEmpName")
    @Expose
    public var fromEmpName: String? = null
    @SerializedName("ToEmpName")
    @Expose
    public var toEmpName: String? = null
    @SerializedName("Toemp")
    @Expose
    public var toemp: String? = null
    @SerializedName("photo")
    @Expose
    public var photo: String? = null
    @SerializedName("countmessage")
    @Expose
    public var msgCount: Int? = null
    public var isIAm: Boolean = false


  }
}
