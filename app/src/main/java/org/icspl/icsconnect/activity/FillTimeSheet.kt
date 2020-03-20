package org.icspl.icsconnect.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import com.labters.lottiealertdialoglibrary.DialogTypes
import com.labters.lottiealertdialoglibrary.LottieAlertDialog
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_time_sheet.*
import kotlinx.android.synthetic.main.activity_time_sheet.navigation_view
import org.icspl.icsconnect.R
import org.icspl.icsconnect.models.*
import org.icspl.icsconnect.preferences.LoginPreference
import org.icspl.icsconnect.utils.Common
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import com.labters.lottiealertdialoglibrary.ClickListener as ClickListener1

class FillTimeSheet : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val mService by lazy { Common.getAPI() }
    internal lateinit var picker: DatePickerDialog
    private lateinit var mToolbar:androidx.appcompat.widget.Toolbar
    private lateinit var toggle:ActionBarDrawerToggle
    lateinit var menu: Menu
    private var mLoginPreference: LoginPreference? = null
    private var item: MenuItem? = null
    private val mDisposable = CompositeDisposable()
    private lateinit var back:ImageView
    private lateinit var master_admin:String
    private lateinit var user:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_sheet)
        mToolbar = findViewById(R.id.toolbar)
        back= findViewById(R.id.back)
        back.setOnClickListener(View.OnClickListener {
            finish()
        })
        setSupportActionBar(mToolbar)
        toggle = ActionBarDrawerToggle(this@FillTimeSheet, drawer_layout1, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.isDrawerIndicatorEnabled=true
        drawer_layout1.addDrawerListener(toggle)
        toggle.syncState()
        navigation_view.setNavigationItemSelectedListener(this)
        mLoginPreference = LoginPreference(this@FillTimeSheet)
        master_admin= mLoginPreference!!.getStringData("masteradmin","").toString()
        user= mLoginPreference!!.getStringData("id","").toString()

        tv_empname.text = mLoginPreference!!.getStringData("name", "")
        tv_empcode.text = mLoginPreference!!.getStringData("id", "")
        tv_designation.text= mLoginPreference!!.getStringData("designation","Notavailble")
        empstation.text=mLoginPreference!!.getStringData("station","not available")
        Location.text=mLoginPreference!!.getStringData("station","not available")
        Picasso.get().load("http://icspl.org/data/Employeephoto/" + mLoginPreference!!.getStringData("photo", "")!!)
            .placeholder(R.drawable.ic_user)
            .error(R.drawable.ic_user)
            .into(imgemp)
        selectdate.setOnClickListener {
            val cldr = Calendar.getInstance()
            val day = cldr.get(Calendar.DAY_OF_MONTH)
            val month = cldr.get(Calendar.MONTH)
            val year = cldr.get(Calendar.YEAR)

            picker = DatePickerDialog(this@FillTimeSheet,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    selectdate.text = (monthOfYear + 1).toString() + "-" + dayOfMonth + "-" + year
                }, year, month, day
            )
            picker.datePicker.maxDate = cldr.timeInMillis
            picker.show()
        }
        val menu = navigation_view.menu

        val nav_dashboard = menu.findItem(R.id.deletegrp)
        if(master_admin==user)
        {
            nav_dashboard.setVisible(true)
        }

        function()
        workinghrschange()
        halfdayfunc()
        btn_submit.setOnClickListener(View.OnClickListener {

            //animation_view.
            if (selectdate.text == "") {
                Toast.makeText(this@FillTimeSheet, "Please Select date ", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if(workinghr.selectedItem.toString() != "AB"&&workinghr.selectedItem.toString() != "H") {
                if (workinghr.selectedItem.toString() == "SELECT") {
                    Toast.makeText(this@FillTimeSheet, "Please select Working hours", Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                }
                if(workinghr.selectedItem.toString()=="4")
                {
                    if(halfday_spin.selectedItem.toString().equals("SELECT"))
                    {
                        Toast.makeText(this@FillTimeSheet, "Please select half day against", Toast.LENGTH_SHORT).show()
                        return@OnClickListener
                    }
                    if(halfday_spin.selectedItem.toString().equals("Compoff"))
                    {
                        if(compoff_spin.selectedItem.toString().equals("SELECT")||compoff_spin.selectedItemPosition.equals(1))
                        {
                            Toast.makeText(this@FillTimeSheet, "Please select Compo off against", Toast.LENGTH_SHORT).show()
                            return@OnClickListener
                        }

                    }
                }
                if (Activitys.selectedItem.toString() == "SELECT") {
                    Toast.makeText(this@FillTimeSheet, "Please select Activity", Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                }
                if (summery.text.toString() == "") {
                    Toast.makeText(this@FillTimeSheet, "Please insert summery", Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                }
            }
            else
            {
                if (summery.text.toString() == "") {
                    Toast.makeText(this@FillTimeSheet, "Please insert summery", Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                }

            }
            AlertDialog.Builder(this@FillTimeSheet)
                .setIcon(R.drawable.timesheet)
                .setTitle("Fill time Sheet")
                .setMessage("Are you sure wants to submit the data")
                .setPositiveButton("YES") { dialog, which ->
                    dialog.dismiss()
                    var alertDialog : LottieAlertDialog = LottieAlertDialog.Builder(this@FillTimeSheet, DialogTypes.TYPE_CUSTOM,"data.json")
                        .setTitle("Loading")
                        .setDescription("Please Wait")
                        .build()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                    //  Toasty.success(this@FillTimeSheet, mLoginPreference!!.getStringData("id", "0")!!,Toast.LENGTH_SHORT).show()

                    var id= mLoginPreference!!.getStringData("id", "0")!!
                    var date=selectdate.text.toString()
                    var workinghrs=workinghr.selectedItem.toString()
                    var halfday:String
                    var compoff:String
                    if(halfday_spin.selectedItem.toString().equals("SELECT"))
                    {
                        halfday="NA"
                        compoff="NA"
                    }
                    else
                    {
                        halfday=halfday_spin.selectedItem.toString()
                        if(halfday_spin.selectedItem.toString().equals("Compoff"))
                        {
                            if(compoff_spin.selectedItem.toString().equals(""))
                            {
                                compoff="NA"
                            }
                            else
                            {
                                var selectdate=compoff_spin.selectedItem.toString().substring(0,11)
                                compoff=actualformatdate(selectdate)

                            }


                        }
                        else
                        {
                            compoff="NA"
                        }



                    }
                    var extraworkinghrs:String
                    if(extraworkinghr.text.toString().equals(""))
                    {
                        extraworkinghrs="0"
                    }
                    else
                    {
                        extraworkinghrs=extraworkinghr.text.toString()
                    }
                    var finalactivity=Activitys.selectedItem.toString()
                    var location=Location.text.toString()
                    var summery=summery.text.toString()
                    mService.sendtimesheetdetails(finalactivity,id,date,location,workinghrs,extraworkinghrs,halfday,compoff,summery).enqueue(object :Callback<TimesheetMessage>
                        {
                            override fun onFailure(call: Call<TimesheetMessage>, t: Throwable) {
                               alertDialog.dismiss()
                                var alertDialog : LottieAlertDialog = LottieAlertDialog.Builder(this@FillTimeSheet, DialogTypes.TYPE_CUSTOM,"sentfail.json")
                                    .setTitle("Timesheet")
                                    .setDescription("Failed to send data please try again")
                                    .setPositiveButtonColor(resources.getColor(R.color.colorAccent))
                                    .setPositiveTextColor(resources.getColor(R.color.white))
                                    .setPositiveText("ok")
                                    .setPositiveListener(object : ClickListener1 {
                                        override fun onClick(dialog: LottieAlertDialog) {
                                            dialog.dismiss()

                                        }

                                    })
                                    .build()
                                alertDialog.setCancelable(false)
                                alertDialog.show()
                            }

                            override fun onResponse(call: Call<TimesheetMessage>, response: Response<TimesheetMessage>) {
                                alertDialog.dismiss()
                                if(response.isSuccessful&& !response.body()!!.timesheetMessage!!.size.equals(0))
                                {
                                    if(response.body()!!.timesheetMessage!!.get(0).mSGStatus.toString().equals("Done"))
                                    {
                                        var alertDialog : LottieAlertDialog = LottieAlertDialog.Builder(this@FillTimeSheet, DialogTypes.TYPE_CUSTOM,"sentsuccess.json")
                                            .setTitle("Data submitted Successfully")
                                            .setPositiveButtonColor(resources.getColor(R.color.colorAccent))
                                            .setPositiveTextColor(resources.getColor(R.color.white))
                                            .setPositiveText("Ok")
                                            .setPositiveListener(object :ClickListener1{
                                                override fun onClick(dialog: LottieAlertDialog) {
                                                    dialog.dismiss()
                                                    finish();
                                                    startActivity(getIntent());


                                                }

                                            })
                                            .build()
                                        alertDialog.setCancelable(false)
                                        alertDialog.show()
                                    }
                                }
                                else
                                {
                                    var alertDialog : LottieAlertDialog = LottieAlertDialog.Builder(this@FillTimeSheet, DialogTypes.TYPE_CUSTOM,"sentfail.json")
                                        .setTitle("Failed to send data")
                                        .setDescription(" please try again")
                                        .setPositiveButtonColor(resources.getColor(R.color.colorAccent))
                                        .setPositiveTextColor(resources.getColor(R.color.white))
                                        .setPositiveText("ok")
                                        .setPositiveListener(object : ClickListener1 {
                                            override fun onClick(dialog: LottieAlertDialog) {
                                                dialog.dismiss()
                                            }

                                        })
                                        .build()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()
                                }
                            }

                        })
                }.setNegativeButton(android.R.string.cancel, { dialog, which ->
                    dialog.dismiss()
                })
                .show()
        })

    }

    private fun halfdayfunc() {
        halfday_spin.onItemSelectedListener=object :AdapterView.OnItemSelectedListener
        {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mLoginPreference = LoginPreference(this@FillTimeSheet)
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                var userid= mLoginPreference!!.getStringData("id", "0")!!
                compoff.visibility=View.GONE
                var halfdaya=halfday_spin.selectedItem.toString()
                if(!halfdaya.equals("Leave Without Pay")&&!halfdaya.equals("SELECT"))
                {
                    //static day
                    mService.getLeaveStatus(userid,selectdate.text.toString(),halfday_spin.selectedItem.toString())
                        .enqueue(object :Callback<GetLeaveStatus>
                        {
                            override fun onFailure(call: Call<GetLeaveStatus>, t: Throwable) {
                                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                Toasty.error(this@FillTimeSheet,"Network Error",Toast.LENGTH_SHORT).show()
                            }

                            override fun onResponse(call: Call<GetLeaveStatus>, response: Response<GetLeaveStatus>) {
                                if(response.isSuccessful&& response.body()!!.leaveStatus!!.size!=0)
                                {
                                    if(response.body()!!.leaveStatus!!.get(0).leaveCount!!.equals(0))
                                    {
                                        var alertDialog : LottieAlertDialog = LottieAlertDialog.Builder(this@FillTimeSheet, DialogTypes.TYPE_CUSTOM,"Error.json")
                                            .setTitle("Error")
                                            .setPositiveListener(object : ClickListener1 {
                                                override fun onClick(dialog: LottieAlertDialog) {
                                                    halfday_layout.visibility=View.GONE
                                                    workinghr.setSelection(0)
                                                    dialog.dismiss()
                                                }

                                            })
                                            .setPositiveButtonColor(resources.getColor(R.color.red_app))
                                            .setPositiveText("Ok")
                                            .setDescription("You Don't have sufficient Leave for half day")
                                            .build()
                                        alertDialog.setCancelable(false)
                                        alertDialog.show()

                                    }
                                    else
                                    {
                                        if(halfday_spin.selectedItem.toString().equals("Compoff"))
                                        {
                                            compoff.visibility=View.VISIBLE
                                            compoOffDetails(userid);

                                        }
                                    }
                                }

                            }

                        })

                }
                else
                {
                }
            }

        }
    }

    private fun compoOffDetails(userid: String) {
        val compoffdate:MutableList<String> = ArrayList()
        mService!!.getCompoOffDetails(userid)
            .enqueue(object : Callback<CompOffstatus> {
                override fun onFailure(call: Call<CompOffstatus>, t: Throwable) {

                }

                override fun onResponse(call: Call<CompOffstatus>, response: Response<CompOffstatus>) {
                    if(response.isSuccessful&&!response.body()!!.compOffStatus!!.size.equals(0))
                    {
                        compoffdate.add("SELECT")
                        compoffdate.add("Days \t\t\t\t\t Count")
                        response.body()!!.compOffStatus!!.forEach { ss ->
                            compoffdate.add(dateformat(ss.compDate!!)+"\t\t\t\t"+ss.comp)
                        }
                        val adapter =  ArrayAdapter<String>(this@FillTimeSheet, android.R.layout.simple_spinner_item, compoffdate)
                        compoff_spin.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }
                }
            })
    }
    private  fun dateformat(date:String):String
    {
        val dateFormat = java.text.SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)

        val sdate = dateFormat.parse(date)
        val formatter = java.text.SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
        val newDate = formatter.format(sdate)
        return newDate
    }
    private  fun actualformatdate(date:String):String
    {
        val dateFormat = java.text.SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)

        val sdate = dateFormat.parse(date)
        val formatter = java.text.SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH)
        val newDate = formatter.format(sdate)
        return newDate
    }

    private fun workinghrschange() {
        workinghr.onItemSelectedListener=object :AdapterView.OnItemSelectedListener
        {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var halfday_against:MutableList<String> = ArrayList()
                halfday_against.add("SELECT")
                halfday_against.add("CL")
                halfday_against.add("SL")
                halfday_against.add("Compoff")
                halfday_against.add("Leave Without Pay")
                val adapter1 =  ArrayAdapter<String>(this@FillTimeSheet, android.R.layout.simple_spinner_item, halfday_against)
                halfday_spin.adapter = adapter1
                adapter1.notifyDataSetChanged()
                halfday_layout.visibility=View.GONE
                if (workinghr.selectedItem.toString().equals("H")||workinghr.selectedItem.toString().equals("AB"))
                {
                    holidayrestrict.visibility=View.GONE
                    return
                }
                else if(workinghr.selectedItem.toString().equals("4"))
                {
                    halfday_layout.visibility= View.VISIBLE
                }
                    holidayrestrict.visibility=View.VISIBLE
                    var alertDialog : LottieAlertDialog = LottieAlertDialog.Builder(this@FillTimeSheet, DialogTypes.TYPE_CUSTOM,"data.json")
                        .setTitle("Loading")
                        .setDescription("Please Wait")
                        .build()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                    mLoginPreference = LoginPreference(this@FillTimeSheet)
                    var userid= mLoginPreference!!.getStringData("id", "0")!!
                    if(selectdate.text == "")
                    {
                        alertDialog.dismiss()
                        Toasty.error(this@FillTimeSheet,"Please select Date first",Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        getplanactivity(alertDialog,userid,selectdate.text.toString())
                    }

            }

        }
    }

    private fun function() {

        mLoginPreference = LoginPreference(this@FillTimeSheet)
        selectdate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            @SuppressLint("CheckResult")
            override fun afterTextChanged(s: Editable?) {
                var time:MutableList<String> = ArrayList()
                time.add("SELECT")
                time.add("8")
                time.add("6")
                time.add("4")
                time.add("AB")
                val adapter =  ArrayAdapter<String>(this@FillTimeSheet, android.R.layout.simple_spinner_item, time)
                workinghr.adapter = adapter
                adapter.notifyDataSetChanged()
                restrict.visibility=View.VISIBLE
                holidayrestrict.visibility=View.VISIBLE
                var alertDialog : LottieAlertDialog = LottieAlertDialog.Builder(this@FillTimeSheet, DialogTypes.TYPE_CUSTOM,"data.json")
                    .setTitle("Loading")
                    .setDescription("Please Wait")
                    .build()
                alertDialog.setCancelable(false)
                alertDialog.show()
              //  Toasty.success(this@FillTimeSheet, mLoginPreference!!.getStringData("id", "0")!!,Toast.LENGTH_SHORT).show()

               var id= mLoginPreference!!.getStringData("id", "0")!!
                var date=s?.toString()!!
                mService!!.getDateStatuss(id,date)
                    .enqueue(object: Callback<OpenMessage> {
                        override fun onFailure(call: Call<OpenMessage>, t: Throwable) {
                            alertDialog.dismiss()
                            var alertDialog : LottieAlertDialog = LottieAlertDialog.Builder(this@FillTimeSheet, DialogTypes.TYPE_CUSTOM,"Error.json")
                                .setTitle("Error")
                                .setPositiveListener(object : ClickListener1 {
                                    override fun onClick(dialog: LottieAlertDialog) {
                                        selectdate.setText("SELECT")
                                        dialog.dismiss()
                                    }

                                })
                                .setPositiveButtonColor(resources.getColor(R.color.red_app))
                                .setPositiveText("Ok")
                                .setDescription("Please Check internet connection or try again")
                                .build()
                            alertDialog.setCancelable(false)
                                alertDialog.show()
                        }

                        override fun onResponse(call: Call<OpenMessage>, response: Response<OpenMessage>) {
                            if(response.isSuccessful&& response.body()!!.openMessage!!.size!=0)
                            {
                                var exitstatus=response.body()!!.openMessage!!.get(0).exitStatus!!
                                if(exitstatus.equals("Y"))
                                {
                                    alertDialog.dismiss()
                                    AlertDialog.Builder(this@FillTimeSheet)
                                        .setIcon(R.drawable.timesheet)
                                        .setTitle("ICS")
                                        .setMessage("You are no longer the member of the ICS Thanks for choosing ICS ")
                                        .setPositiveButton("OK") { dialog, which ->
                                            dialog.dismiss()
                                            val sharedPreferences = getSharedPreferences(getString(R.string.login), Context.MODE_PRIVATE)
                                            val edit = sharedPreferences.edit()

                                            edit.remove("id")
                                            //edit.clear()
                                            edit.apply()
                                            startActivity(Intent(this@FillTimeSheet,LoginActivity::class.java))

                                        }.show()

                                }
                                else if(response.body()!!.openMessage!!.get(0).sentSalaryStatus!!.equals("Y"))
                                {
                                    alertDialog.dismiss()
                                    AlertDialog.Builder(this@FillTimeSheet)
                                        .setIcon(R.drawable.timesheet)
                                        .setTitle("Salary Released")
                                        .setMessage("choosen date salary has been released")
                                        .setPositiveButton("OK") { dialog, which ->
                                            dialog.dismiss()
                                            restrict.visibility=View.GONE
                                        }.show()

                                }
                                else if(response.body()!!.openMessage!!.get(0).protectDateStatus!!.equals("Y"))
                                {
                                    alertDialog.dismiss()
                                    AlertDialog.Builder(this@FillTimeSheet)
                                        .setIcon(R.drawable.timesheet)
                                        .setTitle("Salary Released")
                                        .setMessage("choosen date already filled with holiday")
                                        .setPositiveButton("OK") { dialog, which ->
                                            dialog.dismiss()
                                            restrict.visibility=View.GONE
                                        }.show()

                                }
                                else if(response.body()!!.openMessage!!.get(0).docUploadStatus!!.equals("Y"))
                                {
                                    alertDialog.dismiss()
                                    AlertDialog.Builder(this@FillTimeSheet)
                                        .setIcon(R.drawable.timesheet)
                                        .setTitle("Upload Documents")
                                        .setMessage("Kindly Upload all the necessary documents to fill the timesheet")
                                        .setPositiveButton("OK") { dialog, which ->
                                            dialog.dismiss()
                                            restrict.visibility=View.GONE
                                        }.show()
                                   restrict.visibility=View.GONE

                                }
                                else if(response.body()!!.openMessage!!.get(0).prevoiusFilled!!.trim().length>2)
                                {
                                    alertDialog.dismiss()
                                    AlertDialog.Builder(this@FillTimeSheet)
                                        .setIcon(R.drawable.timesheet)
                                        .setTitle("Pending Previous Date Timesheet")
                                        .setMessage("Kindly select the date "+response.body()!!.openMessage!!.get(0).prevoiusFilled!!.trim())
                                        .setPositiveButton("OK") { dialog, which ->
                                            dialog.dismiss()
                                            restrict.visibility=View.GONE
                                        }.show()
                                    restrict.visibility=View.GONE
                                }
                                else if(response.body()!!.openMessage!!.get(0).sundayStatus!!.equals("Y")||response.body()!!.openMessage!!.get(0).saturdayStatus!!.equals("Y")||response.body()!!.openMessage!!.get(0).iCSHolidayStatus!!.equals("Y"))
                                {
                                    var time:MutableList<String> = ArrayList()
                                    time.add("H")
                                    time.add("8")
                                    time.add("6")
                                    time.add("4")
                                    val adapter =  ArrayAdapter<String>(this@FillTimeSheet, android.R.layout.simple_spinner_item, time)
                                    workinghr.adapter = adapter
                                    adapter.notifyDataSetChanged()
                                    alertDialog.dismiss()
                                    holidayrestrict.visibility=View.GONE
                                }
                                else
                                {
                                    alertDialog.dismiss()
                                    restrict.visibility=View.VISIBLE

                                }

                            }
                            else
                            {
                                alertDialog.dismiss()
                                var alertDialog : LottieAlertDialog = LottieAlertDialog.Builder(this@FillTimeSheet, DialogTypes.TYPE_CUSTOM,"Error.json")
                                    .setTitle("Error")
                                    .setDescription("Response Error try again")
                                     .build()
                                    alertDialog.show()

                            }
                        }

                    })
            }
            })
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.home->
            {
                startActivity(Intent(this,DashBoardActivity::class.java))
            }
            R.id.query ->{
                startActivity(Intent(this, RaiseQueryActivity::class.java))
            }
            R.id.menu_close -> {
                startActivity(Intent(this, CLosedQueryActivity::class.java))
            }
            R.id.menu_group -> {
                startActivity(Intent(this, GroupActivity::class.java))
            }
            R.id.Logout -> {
                var edit = mLoginPreference!!.sharedPreferences!!.edit()
                edit.remove("id")
                //edit.clear()
                edit.apply()
                var i = Intent(this, LoginActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

                startActivity(i)
//                this@MainActivity.finish()
            }
            R.id.timesheet -> {
                startActivity(Intent(this, FillTimeSheet::class.java))
            }
        }
        return true
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getplanactivity(
        alertDialog: LottieAlertDialog,
        id: String,
        date: String
    ) {
        var activity:MutableList<String> = ArrayList()

        mService!!.getPlanActivity(id,date,"").enqueue(object : Callback<GetTimesheetPlanActivity> {
            override fun onFailure(call: Call<GetTimesheetPlanActivity>, t: Throwable) {
                alertDialog.dismiss()
                Toasty.error(this@FillTimeSheet,"Network Error",Toast.LENGTH_SHORT).show()
                //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<GetTimesheetPlanActivity>, response: Response<GetTimesheetPlanActivity>) {
                alertDialog.dismiss()
                if(response.isSuccessful&& response.body()!!.openMessage!!.size!=0)
                {
                    activity.add("SELECT")
                    response.body()!!.openMessage!!.forEach { ss ->
                        activity.add(ss.activity!!)
                    }
                    val adapter =  ArrayAdapter<String>(this@FillTimeSheet, android.R.layout.simple_spinner_item, activity)
                    Activitys.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
                else
                {


                }
              //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
      //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun setSupportActionBar(toolbar: Toolbar) {}

}

