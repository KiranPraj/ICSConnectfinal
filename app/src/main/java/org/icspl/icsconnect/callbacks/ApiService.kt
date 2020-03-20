package org.icspl.icsconnect.callbacks


import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.icspl.icsconnect.activity.ViewTimesheet
import org.icspl.icsconnect.models.*
import retrofit2.Call
import retrofit2.http.*


interface ApiService {


    @GET("Home/Login")
    fun checkLogin(
        @Query("id") nambe: String,
        @Query("password") password: String, @Query("token") token: String
    ): Call<EmployeeDetail>

    @GET("Home/CountMessage")
    fun getCountMSg(@Query("id") name: String): Observable<CountMSGModel>

    @GET("Home/Individualdetails")
    fun getCountDetails(@Query("toemp") toemp: String, @Query("fromemp") fromemp: String):
            Observable<CountMSGDetailsModel>

    @GET("Home/Conversation")
    fun getConversation(@Query("queryid") name: String): Observable<ConversationModel>

    @Multipart
    @POST("Home/SendMessage")
    fun sendMessage(
        @Part("queryid") queryid: RequestBody,
        @Part("fromemp") fromemp: RequestBody,
        @Part("toemp") toemp: RequestBody,
        @Part("toempname")toempname: RequestBody,
        @Part("remarks") remarks: RequestBody,
        @Part("sendfrm") sendfrm: RequestBody,
        @Part file: MultipartBody.Part?
    ): Observable<List<ServerResponseModel>>

    // get the search result
    @GET("Home/Allcontacts")
    fun search(@Query("name") name: String): Observable<List<SearchModel>>

    // get the search result
    @GET("Home/AllClosedMessages")
    fun allCloseQuery(@Query("id") empCode: String): Observable<CloseMessage>

    // Close Individual Query
    @FormUrlEncoded
    @POST("Home/MessageClosed")
    fun closeQuery(@Field("queryid") name: String): Observable<List<ServerResponseModel>>
    //delete group
    @FormUrlEncoded
    @POST("Home/DeleteGroup")
    fun deletegroup(@Field("groupid") name: String): Observable<List<ServerResponseModel>>
    // Post Query
    @Multipart
    @POST("Home/QueryRaise")
    fun postQuery(
        @Part("fromemp") fromemp: RequestBody, @Part("toemp") toemp: RequestBody,
        @Part("remark") remarks: RequestBody,
        @Part("time") time: RequestBody,
        @Part("fromempname") fromempname: RequestBody,
        @Part("toempname") toempname: RequestBody,
        @Part file: MultipartBody.Part?
    ): Observable<List<ServerResponseModel>>

    //**** Grps API ******\\
    // create group
    @FormUrlEncoded
    @POST("Home/CreateGroup")
    fun createGroup(
        @Field("group_title") group_title: String,
        @Field("masteradmin") masteradmin: String,
        @Field("groupadmin") groupadmin: String,
        @Field("members") members: String,
        @Field("groupid") groupid:String
    ): Observable<List<ServerResponseModel>>

    @FormUrlEncoded
    @POST("Home/Get_Individual_groups")
    fun getIndividualGroups(
        @Field("memberid") memberId: String
    ): Observable<IndividualGrpNameModel>

    @Multipart
    @POST("Home/SendGroupMessage")
    fun sendGroupMessage(
        @Part("group_id") queryid: RequestBody,
        @Part("message") remarks: RequestBody,
        @Part file: MultipartBody.Part?
    ): Observable<List<ServerResponseModel>>


   // @FormUrlEncoded
    @GET("Home/GetGroupMessages")
    fun GetGroupMessage(
        @Query("group_id") groupId: String): Observable<GrpConversationModel>

    @Multipart
    @POST("Home/sendMasterGroupMessage")
    fun sendMasterGroupMessage(
        @Part("mastergroupid") String: ArrayList<String>,
        @Part("msg") remarks: RequestBody,
        @Part file: MultipartBody.Part?
    ): Observable<List<ServerResponseModel>>

    //timesheet getdatestatus
    @GET("Timesheet/GetDateStatus")
    fun getDateStatuss(
        @Query("empcode") empcode:String,
        @Query("execdate") execdate:String
    ):Call<OpenMessage>
    //timsesheet activity
    @GET("Timesheet/GetPlanActivity")
    fun getPlanActivity(
        @Query("empcode") empcode:String,
        @Query("execdate") execdate:String,
        @Query("empdept") empdept:String
    ):Call<GetTimesheetPlanActivity>
    //leave Status
    @GET("Timesheet/GetLeaveStatus")
    fun getLeaveStatus(
        @Query("empcode") empcode: String,
        @Query("execdate") execdate: String,
        @Query("LeaveType") leavetype:String
    ):Call<GetLeaveStatus>
    //compo off information
    @GET("Timesheet/GetCompOffStatus")
    fun getCompoOffDetails(
        @Query("empcode") empcode:String
    ):Call<CompOffstatus>
    //added 10oct19 get_leave year for leave status
    @GET("LeaveStatus/GetleaveYear")
    fun get_leave_year(
        @Query("empcode") empcode:String
    ):Call<List<GetleaveYear>>
// added 10oct19 getleave status details
    @GET("LeaveStatus/GetLeaveDetailsStatus")
    fun get_leave_status_details(
        @Query("empcode") empcode:String,
        @Query("Pfinyear") pfinyear:String
    ):Call<List<GetLeaveStatusDetails>>

    //added 11 oct19 leavedetails

    @GET("LeaveStatus/Getconsumedleave")
    fun get_leave_details(
        @Query("empcode") empcode:String
    ):Call<List<GetLeaveDetails>>

    //send timesheet data
    @GET("Timesheet/Set_TimesheetAdmin")
    fun sendtimesheetdetails(
        @Query("Activity") Activity:String,
        @Query("empcode") empcode:String,
        @Query("execdate") execdate:String,
        @Query("location") location:String,
        @Query("workhrs") workhrs:String,
        @Query("Extrahrs") Extrahrs:String,
        @Query("halfdayagainst") halfdayagainst:String,
        @Query("Compagainst") Compagainst:String,
        @Query("Remarks") Remarks:String
    ):Call<TimesheetMessage>
    //viewtimesheet
    @GET("Timesheet/GetViewTimesheetStatus")
    fun viewtimesheet(
        @Query("empcode")empcode:String,
        @Query("Month")Month:String,
        @Query("Year")Year:String
    ):Call<DtGetViewTimesheetstatus>
    //view getexpense details added 10oct19

    @GET("Timesheet/GetExpensesDetails")
    fun view_expenses_details(
        @Query("empcode")empcode:String,
        @Query("Month")Month:String,
        @Query("Year")Year:String
    ):Call<DtGetViewExpensesDetails>

    //view_getinspection_details added 10oct19
   @GET("Timesheet/GetInspectionDetails")
    fun view_inspection_details(
        @Query("empcode")empcode:String,
        @Query("Month")Month:String,
        @Query("Year")Year:String
    ):Call<DtGetViewInspectionDetail>

    //view_getmanday_details added 10oct19
    @GET("Timesheet/GetMandayDetails")
    fun view_manday_details(
        @Query("empcode")empcode:String,
        @Query("Month")Month:String,
        @Query("Year")Year:String
    ):Call<DtGetViewMandayDetail>

    //realtime punchdata
    @GET("Home/GetViewPunchDataStatus")
    fun getrealtimepunchdata(
        @Query("empcode")empcode:String,
        @Query("Month")Month:String,
        @Query("Year")Year:String
    ):Call<DtGetViewPunchDatastatus>
    //company name
    @GET("Home/groupCompany")
    fun getCompany():Call<CompanyNameModal>
    //Region name
    @GET("Home/groupoffice")
    fun getRegion(
        @Query("company")Company: String
    ):Call<RegionNameModal>
    //station name
    @GET("Home/groupStation")
    fun getStation(
        @Query("company")Company: String,
        @Query("region")Region: String
    ):Call<StationNameModal>

    //EmpStatus name
    @GET("Home/groupempstatus")
    fun getEmpStatus(
        @Query("company")Company: String,
        @Query("region")Region: String,
        @Query("station")Station: String
    ):Call<EmpStatusNameModal>
    //department name
    @GET("Home/groupdepartment")
    fun getDepartment(
        @Query("company")Company: String,
        @Query("region")Region: String,
        @Query("station")Station: String,
        @Query("empstatus")Empstatus: String

    ):Call<DepartmentNameModal>
    //Region name
    @GET("Home/groupempname")
    fun getEmpname(
        @Query("company")Company: String,
        @Query("region")Region: String,
        @Query("station")Station: String,
        @Query("empstatus")Empstatus: String,
        @Query("department")Department:String

    ):Call<EmpNameModal>

    @GET("Timesheet/GetViewTimesheetSentStatus")
    fun getdata(
        @Query("empcode") Empcode: String,
        @Query("Year") Year: String
        ):Call<DtGetViewTstatus>

}