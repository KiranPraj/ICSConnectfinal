package org.icspl.icsconnect.callbacks


import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.icspl.icsconnect.models.*
import retrofit2.Call
import retrofit2.http.*


interface ApiService {


    @GET("Login")
    fun checkLogin(
        @Query("id") name: String,
        @Query("password") password: String, @Query("token") token: String
    ): Call<EmployeeDetail>

    @GET("CountMessage")
    fun getCountMSg(@Query("id") name: String): Observable<CountMSGModel>

    @GET("Individualdetails")
    fun getCountDetails(@Query("toemp") toemp: String, @Query("fromemp") fromemp: String):
            Observable<CountMSGDetailsModel>

    @GET("Conversation")
    fun getConversation(@Query("queryid") name: String): Observable<ConversationModel>

    @Multipart
    @POST("SendMessage")
    fun sendMessage(
        @Part("queryid") queryid: RequestBody,
        @Part("fromemp") fromemp: RequestBody,
        @Part("toemp") toemp: RequestBody,
        @Part("remarks") remarks: RequestBody,
        @Part("sendfrm") sendfrm: RequestBody,
        @Part file: MultipartBody.Part?
    ): Observable<List<ServerResponseModel>>

    // get the search result
    @GET("Allcontacts")
    fun search(@Query("name") name: String): Observable<List<SearchModel>>

    // get the search result
    @GET("AllClosedMessages")
    fun allCloseQuery(@Query("id") empCode: String): Observable<CloseMessage>

    // Close Individual Query
    @FormUrlEncoded
    @POST("MessageClosed")
    fun closeQuery(@Field("queryid") name: String): Observable<List<ServerResponseModel>>

    // Post Query
    @Multipart
    @POST("QueryRaise")
    fun postQuery(
        @Part("fromemp") fromemp: RequestBody, @Part("toemp") toemp: RequestBody,
        @Part("remark") remarks: RequestBody,
        @Part("time") time: RequestBody, @Part("fromname") fromname: RequestBody,
        @Part("toname") toname: RequestBody, @Part file: MultipartBody.Part?
    ): Observable<List<ServerResponseModel>>

    //**** Grps API ******\\
    // create group
    @FormUrlEncoded
    @POST("CreateGroup")
    fun createGroup(
        @Field("group_title") group_title: String,
        @Field("masteradmin") masteradmin: String,
        @Field("groupadmin") groupadmin: String,
        @Field("members") members: String
    ): Observable<List<ServerResponseModel>>

    @FormUrlEncoded
    @POST("Get_Individual_groups")
    fun getIndividualGroups(
        @Field("memberid") memberId: String
    ): Observable<IndividualGrpNameModel>

    @Multipart
    @POST("SendGroupMessage")
    fun sendGroupMessage(
        @Part("group_id") queryid: RequestBody,
        @Part("message") remarks: RequestBody,
        @Part file: MultipartBody.Part?
    ): Observable<List<ServerResponseModel>>

    @FormUrlEncoded
    @POST("GetGroupMessages")
    fun sendGroupMessage(
        @Field("group_id") groupId: String): Observable<List<GrpConversationModel>>


}