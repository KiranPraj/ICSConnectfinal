package org.icspl.icsconnect.callbacks


import io.reactivex.Observable
import org.icspl.icsconnect.models.*
import retrofit2.Call
import retrofit2.http.*


interface ApiService {


    @GET("Login")
    fun checkLogin(@Query("id") name: String, @Query("password") password: String): Call<EmployeeDetail>

    @GET("CountMessage")
    fun getCountMSg(@Query("id") name: String): Observable<CountMSGModel>

    @GET("Individualdetails")
    fun getCountDetails(@Query("toemp") toemp: String, @Query("fromemp") fromemp: String):
            Observable<CountMSGDetailsModel>

    @GET("Conversation")
    fun getConversation(@Query("queryid") name: String): Observable<ConversationModel>

    @FormUrlEncoded
    @POST("SendMessage")
    fun sendMessage(
        @Field("queryid") queryid: String,
        @Field("fromemp") fromemp: String,
        @Field("toemp") toemp: String,
        @Field("remarks") remarks: String,
        @Field("sendfrm") sendfrm: String,
        @Field("file") file: String?
    ): Observable<List<ServerResponseModel>>

    // get the search result
    @GET("Allcontacts")
    fun search(@Query("name") name: String): Observable<List<String>>

}