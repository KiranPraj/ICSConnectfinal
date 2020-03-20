package org.icspl.icsconnect.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.drawer_layout
import kotlinx.android.synthetic.main.activity_main.navigation_view
import kotlinx.android.synthetic.main.activity_timesheet_status.*
import kotlinx.android.synthetic.main.activity_timesheet_status.btn_submit
import kotlinx.android.synthetic.main.activity_view_timesheet.*
import org.icspl.icsconnect.MainActivity
import org.icspl.icsconnect.R
import org.icspl.icsconnect.adapters.TimesheetAdaptor
import org.icspl.icsconnect.adapters.ViewTimesheetAdaptor
import org.icspl.icsconnect.adapters.ViewTimesheetStatusAdaptor
import org.icspl.icsconnect.models.DtGetViewTstatus
import org.icspl.icsconnect.preferences.LoginPreference
import org.icspl.icsconnect.utils.Common
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class TimesheetStatus : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val mLoginPreference by lazy { LoginPreference.getInstance(this) }

    private lateinit var mToolbar:androidx.appcompat.widget.Toolbar
    private lateinit var toggle:ActionBarDrawerToggle
    private val mService by lazy { Common.getAPI() }
    private lateinit var back: ImageView
    private lateinit var list:MutableList<DtGetViewTstatus>
    private lateinit var customAdapter:ViewTimesheetStatusAdaptor
    private lateinit var user:String
    private lateinit var master_admin:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timesheet_status)
        mToolbar = findViewById(R.id.toolbar)
        master_admin=mLoginPreference.getStringData("masteradmin","").toString()
        user=mLoginPreference.getStringData("id","").toString()

        back= findViewById(R.id.back)
        back.setOnClickListener(View.OnClickListener {
            finish()
        })
        setSupportActionBar(mToolbar)
        toggle = ActionBarDrawerToggle(this, drawer_layout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.isDrawerIndicatorEnabled=true
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        val menu = navigation_view.menu

        val nav_dashboard = menu.findItem(R.id.deletegrp)
        if(master_admin==user)
        {
            nav_dashboard.setVisible(true)
        }
        navigation_view.setNavigationItemSelectedListener(this)
        list= ArrayList()
        function()
    }

    private fun function() {
        val years:MutableList<String> = ArrayList()
       // val year = Calendar.getInstance().get(Calendar.YEAR)
        var joinyear: Int =Common.getFormatedDate(mLoginPreference.getStringData("joindate","")!!).toInt()
        val year = Calendar.getInstance().get(Calendar.YEAR)
        years.add("SELECT YEAR".toString())
        for ( i in year downTo joinyear)
        {
            years.add(i.toString())
        }
        val adapter =  ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years)
        sp_ts_year.adapter = adapter
        adapter.notifyDataSetChanged()
        btn_submit.setOnClickListener(View.OnClickListener {
            var year=sp_ts_year.getItemAtPosition(sp_ts_year.selectedItemPosition).toString()
            if(year.equals("SELECT YEAR"))
            {
                Toasty.error(this,"Please select year",Toast.LENGTH_LONG).show()
                return@OnClickListener
            }
            viewdata(year)
        })
    }

    private fun viewdata(year: String) {
        mService.getdata(mLoginPreference.getStringData("id","")!!,year).enqueue(object: Callback<DtGetViewTstatus>
        {
            override fun onFailure(call: Call<DtGetViewTstatus>, t: Throwable) {
            Toasty.error(this@TimesheetStatus,"error"+t,Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<DtGetViewTstatus>, response: Response<DtGetViewTstatus>) {
                if(response.isSuccessful&& response.body()!!.dtGetViewTStatus!!.size>0)
                {
                    list= response.body()!!.dtGetViewTStatus as MutableList<DtGetViewTstatus>
                    customAdapter = ViewTimesheetStatusAdaptor(this@TimesheetStatus, list)
                    val layoutManager = LinearLayoutManager(this@TimesheetStatus)
                    rv_timesheetstatus.setLayoutManager(layoutManager)
                    rv_timesheetstatus.setAdapter(customAdapter)
                }
                else{
                    Toasty.error(this@TimesheetStatus,sp_ts_year.selectedItem.toString(),Toast.LENGTH_LONG).show()

                }
            }
        }
        )
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
                var edit = mLoginPreference.sharedPreferences!!.edit()
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
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
