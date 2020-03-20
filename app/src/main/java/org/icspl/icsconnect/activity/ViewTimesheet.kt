package org.icspl.icsconnect.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_view_timesheet.*
import kotlinx.android.synthetic.main.activity_view_timesheet.btn_submit
import org.icspl.icsconnect.R
import org.icspl.icsconnect.adapters.ViewTimesheetAdaptor
import org.icspl.icsconnect.preferences.LoginPreference
import org.icspl.icsconnect.utils.Common
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import org.icspl.icsconnect.MainActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.icspl.icsconnect.adapters.ViewExpensesDetailsAdapter
import org.icspl.icsconnect.adapters.ViewInspectionDetailsAdapter
import org.icspl.icsconnect.adapters.ViewManDayDetailsAdapter
import org.icspl.icsconnect.models.*


class ViewTimesheet : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var mLoginPreference: LoginPreference? = null
    private val mService by lazy { Common.getAPI() }
    private lateinit var mToolbar:androidx.appcompat.widget.Toolbar
    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var list: MutableList<HomescreenModel>
    lateinit var timsheetlist: MutableList<DtGetViewTimesheetstatus>
    lateinit var expenseslist: MutableList<DtGetViewExpensesDetails>
    lateinit var inspectionlist: MutableList<DtGetViewInspectionDetail>
    lateinit var mandaylist:MutableList<DtGetViewMandayDetail>


    private lateinit var customAdapter:ViewTimesheetAdaptor
    private lateinit var customAdapter_expenses:ViewExpensesDetailsAdapter
    private lateinit var customAdapter_inspection: ViewInspectionDetailsAdapter
    private lateinit var customAdapter_manday: ViewManDayDetailsAdapter
    private lateinit var back: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_timesheet)
        mLoginPreference = LoginPreference(this@ViewTimesheet)
        mToolbar = findViewById(org.icspl.icsconnect.R.id.toolbar_VT)
        back= findViewById(org.icspl.icsconnect.R.id.back)
        back.setOnClickListener(View.OnClickListener {
            finish()
        })
        setSupportActionBar(mToolbar)
        toggle = ActionBarDrawerToggle(this@ViewTimesheet, drawer_layout_VT, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.isDrawerIndicatorEnabled=true
        drawer_layout_VT.addDrawerListener(toggle)
        toggle.syncState()
        initview()
        navigation_view1.setNavigationItemSelectedListener(this)
    }
    private fun initview() {

        val years:MutableList<String> = ArrayList()
        val year = Calendar.getInstance().get(Calendar.YEAR)
        for ( i in year downTo year-10)
        {
            years.add(i.toString())
        }
        val adapter =  ArrayAdapter<String>(this@ViewTimesheet, android.R.layout.simple_spinner_item, years)
        sp_year.adapter = adapter
        adapter.notifyDataSetChanged()
        btn_submit.setOnClickListener(View.OnClickListener {
            if(sp_month.selectedItem.toString().equals("SELECT MONTH"))
            {
                Toasty.error(this@ViewTimesheet,"Please Select Month").show()
                return@OnClickListener
            }
            if(sp_year.selectedItem.toString().equals("SELECT YEAR"))
            {
                Toasty.error(this@ViewTimesheet,"Please Select Year").show()
                return@OnClickListener
            }
            loadtimesheet(sp_year.selectedItem.toString(),sp_month.selectedItemPosition.toString())
           // loadexpensesdetails(sp_year.selectedItem.toString(),sp_month.selectedItemPosition.toString())
          //  loadinspectiondetails(sp_year.selectedItem.toString(),sp_month.selectedItemPosition.toString())
           // loadmandaydetails(sp_year.selectedItem.toString(),sp_month.selectedItemPosition.toString())
        })
        viewTimeSheet.setOnCheckedChangeListener(object: CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if(isChecked)
                {
                    timesheet.visibility=View.VISIBLE


                }
                else
                {
                    timesheet.visibility=View.GONE

                }
            }
        })
        view_expenses.setOnCheckedChangeListener(object: CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if(isChecked)
                {
                    expenses_horizontal.visibility=View.VISIBLE
                    loadexpensesdetails(sp_year.selectedItem.toString(),sp_month.selectedItemPosition.toString())


                }
                else
                {
                    expenses_horizontal.visibility=View.GONE
                }
            }
        })
        viewmanday_details.setOnCheckedChangeListener(object: CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if(isChecked)
                {
                    manday.visibility=View.VISIBLE
                    loadmandaydetails(sp_year.selectedItem.toString(),sp_month.selectedItemPosition.toString())
                }
                else
                {
                    manday.visibility=View.GONE
                }
            }
        })


        viewInspectionDetails.setOnCheckedChangeListener(object: CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if(isChecked)
                {
                    inspection.visibility=View.VISIBLE
                    loadinspectiondetails(sp_year.selectedItem.toString(),sp_month.selectedItemPosition.toString())

                }
                else
                {
                    inspection.visibility=View.GONE
                }
            }
        })
    }

    private fun loadtimesheet(year: String, month: String) {

        timsheetlist=ArrayList()

        var empcode=mLoginPreference!!.getStringData("id", "").toString()
        mService.viewtimesheet(empcode,month,year)
            .enqueue(object : Callback<DtGetViewTimesheetstatus> {
                override fun onFailure(call: Call<DtGetViewTimesheetstatus>, t: Throwable) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onResponse(
                    call: Call<DtGetViewTimesheetstatus>,
                    response: Response<DtGetViewTimesheetstatus>
                ) {
                    if(response.isSuccessful&& response.body()!!.dtGetViewTimesheetStatus!!.size>0)
                    {
                        data.visibility=View.VISIBLE
                        viewTimeSheet.isChecked=true
                        timsheetlist= response.body()!!.dtGetViewTimesheetStatus as MutableList<DtGetViewTimesheetstatus>


                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
                        customAdapter = ViewTimesheetAdaptor(this@ViewTimesheet, timsheetlist)
                        val layoutManager = LinearLayoutManager(this@ViewTimesheet)
                        rv_viewtimesheet.setLayoutManager(layoutManager)
                        rv_viewtimesheet.setAdapter(customAdapter)



                    }
                    else
                    {
                       // data.visibility= View.GONE
                        Toasty.error(this@ViewTimesheet,"No data Found", Toast.LENGTH_SHORT).show()
                    }

                }

            })

    }
    private fun loadexpensesdetails(year: String, month: String) {

        expenseslist=ArrayList()

        var empcode=mLoginPreference!!.getStringData("id", "").toString()
        mService.view_expenses_details(empcode,month,year)
            .enqueue(object : Callback<DtGetViewExpensesDetails> {
                override fun onFailure(call: Call<DtGetViewExpensesDetails>, t: Throwable) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onResponse(
                    call: Call<DtGetViewExpensesDetails>,
                    response: Response<DtGetViewExpensesDetails>
                ) {
                    if(response.isSuccessful&& response.body()!!.dtGetExpensesDetails!!.size>0)
                    {
                        data.visibility=View.VISIBLE
                        view_expenses.isChecked=true
                        expenseslist= response.body()!!.dtGetExpensesDetails as MutableList<DtGetViewExpensesDetails>


                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
                            customAdapter_expenses = ViewExpensesDetailsAdapter(this@ViewTimesheet, expenseslist)
                        val layoutManager = LinearLayoutManager(this@ViewTimesheet)
                        rv_viewexpenses_details.setLayoutManager(layoutManager)
                        rv_viewexpenses_details.setAdapter(customAdapter_expenses)



                    }
                    else
                    {
                       // data.visibility= View.GONE
                        Toasty.error(this@ViewTimesheet,"No data Found", Toast.LENGTH_SHORT).show()
                    }

                }

            })

    }

    private fun loadinspectiondetails(year: String, month: String) {

        expenseslist=ArrayList()

        var empcode=mLoginPreference!!.getStringData("id", "").toString()
        mService.view_inspection_details(empcode,month,year)
            .enqueue(object : Callback<DtGetViewInspectionDetail> {
                override fun onFailure(call: Call<DtGetViewInspectionDetail>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<DtGetViewInspectionDetail>,
                    response: Response<DtGetViewInspectionDetail>
                ) {
                    if(response.isSuccessful&& response.body()!!.dtGetInspectionDetails!!.size>0)
                    {
                        data.visibility=View.VISIBLE
                        viewInspectionDetails.isChecked=true
                        inspectionlist= response.body()!!.dtGetInspectionDetails as MutableList<DtGetViewInspectionDetail>


                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
                            customAdapter_inspection = ViewInspectionDetailsAdapter(this@ViewTimesheet, inspectionlist)
                        val layoutManager = LinearLayoutManager(this@ViewTimesheet)
                        rv_viewinspection.setLayoutManager(layoutManager)
                        rv_viewinspection.setAdapter(customAdapter_inspection)



                    }
                    else
                    {
                       // data.visibility= View.GONE
                        Toasty.error(this@ViewTimesheet,"No data Found", Toast.LENGTH_SHORT).show()
                    }

                }

            })

    }
    private fun loadmandaydetails(year: String, month: String) {

        mandaylist=ArrayList()

        var empcode=mLoginPreference!!.getStringData("id", "").toString()
        mService.view_manday_details(empcode,month,year)
            .enqueue(object : Callback<DtGetViewMandayDetail> {
                override fun onFailure(call: Call<DtGetViewMandayDetail>, t: Throwable) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onResponse(
                    call: Call<DtGetViewMandayDetail>,
                    response: Response<DtGetViewMandayDetail>
                ) {
                    if(response.isSuccessful&& response.body()!!.dtGetMandayDetails!!.size>0)
                    {
                        data.visibility=View.VISIBLE
                        viewmanday_details.isChecked=true
                        mandaylist= response.body()!!.dtGetMandayDetails as MutableList<DtGetViewMandayDetail>


                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
                            customAdapter_manday = ViewManDayDetailsAdapter(this@ViewTimesheet, mandaylist)
                        val layoutManager = LinearLayoutManager(this@ViewTimesheet)
                        rv_viewmanday_details.setLayoutManager(layoutManager)
                        rv_viewmanday_details.setAdapter(customAdapter_manday)



                    }
                    else
                    {

                       // data.visibility= View.GONE
                        Toasty.error(this@ViewTimesheet,"No data Found", Toast.LENGTH_SHORT).show()
                    }

                }

            })

    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            org.icspl.icsconnect.R.id.home->
            {
                startActivity(Intent(this,DashBoardActivity::class.java))
            }
            org.icspl.icsconnect.R.id.query ->{
                startActivity(Intent(this, MainActivity::class.java))
            }
            org.icspl.icsconnect.R.id.menu_close -> {
                startActivity(Intent(this, CLosedQueryActivity::class.java))
            }
            org.icspl.icsconnect.R.id.menu_group -> {
                startActivity(Intent(this, GroupActivity::class.java))
            }
            org.icspl.icsconnect.R.id.Logout -> {
                var edit = mLoginPreference!!.sharedPreferences!!.edit()
                edit.remove("id")
                //edit.clear()
                edit.apply()
                var i = Intent(this, LoginActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

                startActivity(i)
//                this@MainActivity.finish()
            }
            org.icspl.icsconnect.R.id.timesheet -> {
                startActivity(Intent(this, FillTimeSheet::class.java))
            }
        }
        return true
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
