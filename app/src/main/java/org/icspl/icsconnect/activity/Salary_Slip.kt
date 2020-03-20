package org.icspl.icsconnect.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_salary__slip.*
import kotlinx.android.synthetic.main.activity_salary__slip.navigation_view1
import kotlinx.android.synthetic.main.toolbar.*
import org.icspl.icsconnect.R
import org.icspl.icsconnect.models.HomescreenModel
import org.icspl.icsconnect.preferences.LoginPreference
import org.icspl.icsconnect.utils.Common
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.airbnb.lottie.LottieCompositionFactory.fromAsset
import android.R.attr.password
import android.R.attr.spacing
import android.content.res.AssetManager
import android.widget.ArrayAdapter
import android.widget.Toast

import com.github.barteksc.pdfviewer.PDFView
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_salary__slip.btn_submit
import kotlinx.android.synthetic.main.activity_salary__slip.sp_month
import kotlinx.android.synthetic.main.activity_salary__slip.sp_year
import kotlinx.android.synthetic.main.activity_view_timesheet.*
import org.icspl.icsconnect.MainActivity
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class Salary_Slip : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var mLoginPreference: LoginPreference? = null
    private val mService by lazy { Common.getAPI() }
    private lateinit var mToolbar:androidx.appcompat.widget.Toolbar
    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var list: MutableList<HomescreenModel>
    private lateinit var back: ImageView
    private lateinit var user:String
    private lateinit var master_admin:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(org.icspl.icsconnect.R.layout.activity_salary__slip)
        mLoginPreference = LoginPreference(this)
        mToolbar = findViewById(org.icspl.icsconnect.R.id.toolbar_VT)
        master_admin= mLoginPreference!!.getStringData("masteradmin","").toString()
        user= mLoginPreference!!.getStringData("id","").toString()
        back= findViewById(org.icspl.icsconnect.R.id.back)
        back.setOnClickListener(View.OnClickListener {
            finish()
        })
        setSupportActionBar(mToolbar)
        toggle = ActionBarDrawerToggle(this, drawer_layout_SS, mToolbar, org.icspl.icsconnect.R.string.navigation_drawer_open, org.icspl.icsconnect.R.string.navigation_drawer_close)
        toggle.isDrawerIndicatorEnabled=true
        drawer_layout_SS.addDrawerListener(toggle)
        toggle.syncState()
        navigation_view1.setNavigationItemSelectedListener(this)
        init()
    }

    private fun init() {
        //val pdfView = findViewById(org.icspl.icsconnect.R.id.salary_slip)
        val menu = navigation_view1.menu

        val nav_dashboard = menu.findItem(R.id.deletegrp)
        if(master_admin==user)
        {
            nav_dashboard.setVisible(true)
        }
        val years:MutableList<String> = ArrayList()
        val year = Calendar.getInstance().get(Calendar.YEAR)
        years.add("SELECT YEAR".toString())
        for ( i in year downTo year-10)
        {
            years.add(i.toString())
        }
        val adapter =  ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years)
        sp_year.adapter = adapter
        adapter.notifyDataSetChanged()
        btn_submit.setOnClickListener(View.OnClickListener {
            if(sp_month.selectedItem.toString().equals("SELECT MONTH"))
            {
                Toasty.error(this,"Please Select Month").show()
                return@OnClickListener
            }
            if(sp_year.selectedItem.toString().equals("SELECT YEAR"))
            {
                Toasty.error(this,"Please Select Year").show()
                return@OnClickListener
            }
            if(sp_month.selectedItemPosition.equals(2))
            {
                viewsalaryslip(2)
            }
            if(sp_month.selectedItemPosition.equals(3))
            {
                viewsalaryslip(3)
            }
            if(sp_month.selectedItemPosition.equals(4))
            {
                viewsalaryslip(4)
            }
            if(sp_month.selectedItemPosition.equals(5))
            {
                viewsalaryslip(5)

            }
        })
    }

    private fun viewsalaryslip(i: Int) {
        if(i.equals(2))
        {
            salary_slip.visibility=View.VISIBLE
            salary_slip.fromAsset("ICS3180February2019payslip.pdf")
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(true)
                .enableDoubletap(true)
                .defaultPage(0)
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0)
                .pageFitPolicy(FitPolicy.WIDTH)
                .load()
        }
        else if(i.equals(3))
        {
            salary_slip.visibility=View.VISIBLE
            salary_slip.fromAsset("ICS3180March2019payslip.pdf")
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(true)
                .enableDoubletap(true)
                .defaultPage(0)
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0)
                .pageFitPolicy(FitPolicy.WIDTH)
                .load()

        }
        else if(i.equals(5))
        {
            salary_slip.visibility=View.VISIBLE
            salary_slip.fromAsset("ICS3180May2019payslip.pdf")
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(true)
                .enableDoubletap(true)
                .defaultPage(0)
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0)
                .pageFitPolicy(FitPolicy.WIDTH)
                .load()

        }
        else
        {
            salary_slip.visibility=View.GONE
            Toasty.error(this@Salary_Slip,"SALARY Slip is Not Available for this month", Toast.LENGTH_SHORT).show()
        }

  /*      salary_slip.fromAsset("ICS3180February2019payslip.pdf")
            .enableSwipe(true) // allows to block changing pages using swipe
            .swipeHorizontal(true)
            .enableDoubletap(true)
            .defaultPage(0)
            .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
            .password(null)
            .scrollHandle(null)
            .enableAntialiasing(true) // improve rendering a little bit on low-res screens
            // spacing between pages in dp. To define spacing color, set view background
            .spacing(0)
            .pageFitPolicy(FitPolicy.WIDTH)
            .load()*/


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
    }


}
