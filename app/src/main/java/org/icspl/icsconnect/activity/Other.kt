package org.icspl.icsconnect.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.drawer_layout
import kotlinx.android.synthetic.main.activity_main.navigation_view
import kotlinx.android.synthetic.main.activity_main.rv
import kotlinx.android.synthetic.main.activity_other.*
import org.icspl.icsconnect.MainActivity
import org.icspl.icsconnect.R
import org.icspl.icsconnect.adapters.OtherAdaptor
import org.icspl.icsconnect.models.HomescreenModel
import org.icspl.icsconnect.preferences.LoginPreference

class Other : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, OtherAdaptor.Callback {
    private val mLoginPreference by lazy { LoginPreference.getInstance(this) }
    private lateinit var back:ImageView
    private lateinit var mToolbar:androidx.appcompat.widget.Toolbar
    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var list: MutableList<HomescreenModel>
    private lateinit var master_admin:String
    private lateinit var user:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other)
        mToolbar = findViewById(R.id.toolbar)
        back= findViewById(R.id.back)
        master_admin=mLoginPreference.getStringData("masteradmin","").toString()
        user=mLoginPreference.getStringData("id","").toString()
        back.setOnClickListener(View.OnClickListener {
            finish()
        })
        setSupportActionBar(mToolbar)
        toggle = ActionBarDrawerToggle(this, drawer_layout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.isDrawerIndicatorEnabled=true
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        linearview()
        navigation_view.setNavigationItemSelectedListener(this)
    }
    @SuppressLint("WrongConstant")
    private fun linearview() {
        val homescreenmodel1 = HomescreenModel(R.drawable.view_sic, "View SIC")

        list = ArrayList<HomescreenModel>()
        list.add(homescreenmodel1)
        val layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        rv.setLayoutManager(layoutManager)
        val adaptorHOme = OtherAdaptor(this, this, list)
        rv.setAdapter(adaptorHOme)
        val menu = navigation_view.menu

        val nav_dashboard = menu.findItem(R.id.deletegrp)
        if(master_admin==user)
        {
            nav_dashboard.setVisible(true)
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
    override fun positionhandler(position: Int) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
