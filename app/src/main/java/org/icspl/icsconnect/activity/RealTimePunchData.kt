package org.icspl.icsconnect.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_real_time_punch_data.*
import android.content.Intent
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.labters.lottiealertdialoglibrary.DialogTypes
import com.labters.lottiealertdialoglibrary.LottieAlertDialog
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.activity_real_time_punch_data.btn_submit
import kotlinx.android.synthetic.main.activity_real_time_punch_data.drawer_layout_VT
import kotlinx.android.synthetic.main.activity_real_time_punch_data.navigation_view1
import kotlinx.android.synthetic.main.activity_view_timesheet.*
import org.icspl.icsconnect.MainActivity
import org.icspl.icsconnect.R
import org.icspl.icsconnect.adapters.ViewRealTimePunchDataAdaptor
import org.icspl.icsconnect.adapters.ViewTimesheetAdaptor
import org.icspl.icsconnect.models.DtGetViewPunchDatastatus
import org.icspl.icsconnect.models.HomescreenModel
import org.icspl.icsconnect.preferences.LoginPreference
import org.icspl.icsconnect.utils.Common
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class RealTimePunchData : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var mLoginPreference: LoginPreference? = null
    private val mService by lazy { Common.getAPI() }
    private lateinit var mToolbar:androidx.appcompat.widget.Toolbar
    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var punchdatalist: MutableList<DtGetViewPunchDatastatus>
    lateinit var list: MutableList<HomescreenModel>
    private lateinit var back: ImageView
    private lateinit var customAdapter:ViewRealTimePunchDataAdaptor
    private lateinit var master_admin:String
    private lateinit var user:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_real_time_punch_data)
        mLoginPreference = LoginPreference(this@RealTimePunchData)
        mToolbar = findViewById(org.icspl.icsconnect.R.id.toolbar_VT)
        back= findViewById(org.icspl.icsconnect.R.id.back)
        master_admin= mLoginPreference!!.getStringData("masteradmin","").toString()
        user= mLoginPreference!!.getStringData("id","").toString()
        back.setOnClickListener(View.OnClickListener {
            finish()
        })
        setSupportActionBar(mToolbar)
        toggle = ActionBarDrawerToggle(this@RealTimePunchData, drawer_layout_VT, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.isDrawerIndicatorEnabled=true
        drawer_layout_VT.addDrawerListener(toggle)
        toggle.syncState()
        initview()
        navigation_view1.setNavigationItemSelectedListener(this)

    }
    private fun initview() {

        val years:MutableList<String> = ArrayList()
        val year = Calendar.getInstance().get(Calendar.YEAR)
        years.add("SELECT YEAR".toString())
        for ( i in year downTo year-10)
        {
            years.add(i.toString())
        }
        val adapter =  ArrayAdapter<String>(this@RealTimePunchData, android.R.layout.simple_spinner_item, years)
        sp_year_rt.adapter = adapter
        adapter.notifyDataSetChanged()
        btn_submit.setOnClickListener(View.OnClickListener {
            if(sp_month_rt.selectedItem.toString().equals("SELECT MONTH"))
            {
                Toasty.error(this@RealTimePunchData,"Please Select Month").show()
                return@OnClickListener
            }
            if(sp_year_rt.selectedItem.toString().equals("SELECT YEAR"))
            {
                Toasty.error(this@RealTimePunchData,"Please Select Year").show()
                return@OnClickListener
            }
            loadrealtimepunchdata(sp_year_rt.selectedItem.toString(),sp_month_rt.selectedItemPosition.toString())
        })

        val menu = navigation_view1.menu

        val nav_dashboard = menu.findItem(R.id.deletegrp)
        if(master_admin==user)
        {
            nav_dashboard.setVisible(true)
        }

    }

    private fun loadrealtimepunchdata(year: String, month: String) {
        var alertDialog : LottieAlertDialog = LottieAlertDialog.Builder(this@RealTimePunchData, DialogTypes.TYPE_CUSTOM,"data.json")
            .setTitle("Fetching Data")
            .setDescription("Please Wait")
            .build()
        alertDialog.setCancelable(false)
        alertDialog.show()
        punchdatalist=ArrayList()
        var empcode=mLoginPreference!!.getStringData("id", "").toString()
        mService.getrealtimepunchdata(empcode,month,year)
            .enqueue(object :Callback<DtGetViewPunchDatastatus> {
                override fun onFailure(call: Call<DtGetViewPunchDatastatus>, t: Throwable) {
                    alertDialog.dismiss()
                    table_for_timesheet.visibility=View.GONE

                }

                override fun onResponse(
                    call: Call<DtGetViewPunchDatastatus>,
                    response: Response<DtGetViewPunchDatastatus>
                ) {
                    alertDialog.dismiss()
                    if(response.isSuccessful&& response.body()!!.dtGetViewPunchDataStatus!!.size>0)
                    {
                        table_for_timesheet.visibility=View.VISIBLE
                        punchdatalist= response.body()!!.dtGetViewPunchDataStatus as MutableList<DtGetViewPunchDatastatus>
                        customAdapter = ViewRealTimePunchDataAdaptor(this@RealTimePunchData, punchdatalist)
                        val layoutManager = LinearLayoutManager(this@RealTimePunchData)
                        rv_viewpunchdata.setLayoutManager(layoutManager)
                        rv_viewpunchdata.setAdapter(customAdapter)
                    }
                    else
                    {
                        Toasty.error(this@RealTimePunchData,"Data not Available",Toast.LENGTH_SHORT).show()
                        table_for_timesheet.visibility=View.GONE
                    }

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
                startActivity(Intent(this, MainActivity::class.java))
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
            R.id.ViewTimesheetfill ->
            {
                startActivity(Intent(this,ViewTimesheet::class.java))
            }
            R.id.LeaveStatus ->
            {
                startActivity(Intent(this,Leave::class.java))
            }
        }
        return true
    }
}
