package org.icspl.icsconnect.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.google.android.material.navigation.NavigationView
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_offer.*
import kotlinx.android.synthetic.main.activity_salary__slip.*
import kotlinx.android.synthetic.main.activity_salary__slip.navigation_view1
import org.icspl.icsconnect.MainActivity
import org.icspl.icsconnect.R
import org.icspl.icsconnect.models.HomescreenModel
import org.icspl.icsconnect.preferences.LoginPreference
import org.icspl.icsconnect.utils.Common

class Offer : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
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
        setContentView(R.layout.activity_offer)
        mLoginPreference = LoginPreference(this)
        master_admin= mLoginPreference!!.getStringData("masteradmin","").toString()
        user= mLoginPreference!!.getStringData("id","").toString()

        mToolbar = findViewById(org.icspl.icsconnect.R.id.toolbar_VT)
        back= findViewById(org.icspl.icsconnect.R.id.back)
        back.setOnClickListener(View.OnClickListener {
            finish()
        })
        setSupportActionBar(mToolbar)
        toggle = ActionBarDrawerToggle(this, drawer_layout_OL, mToolbar, org.icspl.icsconnect.R.string.navigation_drawer_open, org.icspl.icsconnect.R.string.navigation_drawer_close)
        toggle.isDrawerIndicatorEnabled=true
        drawer_layout_OL.addDrawerListener(toggle)
        toggle.syncState()
        navigation_view1.setNavigationItemSelectedListener(this)
        init()

        val menu = navigation_view1.menu

        val nav_dashboard = menu.findItem(R.id.deletegrp)
        if(master_admin==user)
        {
            nav_dashboard.setVisible(true)
        }

    }

    private fun init() {
        var empcode=mLoginPreference!!.getStringData("id", "").toString()
        if(empcode.equals("ICS/3180")) {
            offer_letter.fromAsset("ICS-3180KrishnaGajulaoffer.pdf")
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
            Toasty.error(this@Offer,"NO offer letter Available",Toast.LENGTH_SHORT).show()
        }
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
        }
        return true
    }
}