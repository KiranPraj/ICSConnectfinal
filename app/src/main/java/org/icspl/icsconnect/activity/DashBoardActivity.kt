package org.icspl.icsconnect.activity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.navigation.NavigationView
import com.tonyodev.fetch2core.isNetworkAvailable
import kotlinx.android.synthetic.main.activity_main.*
import org.icspl.icsconnect.MainActivity
import org.icspl.icsconnect.R
import org.icspl.icsconnect.adapters.HomeAdaptor
import org.icspl.icsconnect.models.HomescreenModel
import org.icspl.icsconnect.preferences.LoginPreference

class DashBoardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, HomeAdaptor.Callback {
    override fun positionhandler(position: Int) {
        clickhandler(position)
       // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun clickhandler(position: Int) {
        if(position.equals(0))
        {
            startActivity(Intent(this,ICSConnect::class.java))
        }
        else if(position.equals(1))
        {
            startActivity(Intent(this,Timesheet::class.java))
        }
        else if(position.equals(2))
        {
            startActivity(Intent(this,Leave::class.java))
        }
        else if(position.equals(3))
        {
            startActivity(Intent(this,RealTimePunchData::class.java))
        }
        else if (position.equals(4))
        {
            startActivity(Intent(this,Account::class.java))
        }
        else if(position.equals(5))
        {
            startActivity(Intent(this,Other::class.java))
        }
        else if (position.equals(6))
        {
            startActivity(Intent(this,Expenses::class.java))
        }
    }

    private val mLoginPreference by lazy { LoginPreference.getInstance(this@DashBoardActivity) }

    private lateinit var mToolbar:androidx.appcompat.widget.Toolbar
    private lateinit var toggle:ActionBarDrawerToggle
    lateinit var list: MutableList<HomescreenModel>
    private lateinit var back:ImageView
    private lateinit var master_admin:String
    private lateinit var user:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
        val notificationManager = this@DashBoardActivity
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationIntent = Intent(this@DashBoardActivity, MainActivity::class.java)

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val intent = PendingIntent.getActivity(
            this@DashBoardActivity, 0,
            notificationIntent, 0
        )
        mToolbar = findViewById(R.id.toolbar)
        back= findViewById(R.id.back)
        back.visibility = View.GONE
        setSupportActionBar(mToolbar)
        master_admin=mLoginPreference.getStringData("masteradmin","").toString()
        user=mLoginPreference.getStringData("id","").toString()
        toggle = ActionBarDrawerToggle(this, drawer_layout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.isDrawerIndicatorEnabled=true
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        gridview()
        navigation_view.setNavigationItemSelectedListener(this)
        val menu = navigation_view.menu

        val nav_dashboard = menu.findItem(R.id.deletegrp)
        if(master_admin==user)
        {
            nav_dashboard.setVisible(true)
        }

    }
    private fun gridview() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val homescreenmodel = HomescreenModel(R.drawable.icslogo, "ICS Connect")
        val homescreenmodel1 = HomescreenModel(R.drawable.timesheet_icon, "TIMESHEET")
        val homescreenmodel2 = HomescreenModel(R.drawable.sick_leave, "LEAVE")
        val homescreenmodel3 = HomescreenModel(R.drawable.realtimepunch_data, "REAL TIME PUNCH DATA")
        val homescreenmodel4 = HomescreenModel(R.drawable.accounts, "ACCOUNT")
        val homescreenmodel5 = HomescreenModel(R.drawable.other, "OTHER")
        val homescreenmodel6=HomescreenModel(R.drawable.expenses,"EXPENSES")
        list = ArrayList<HomescreenModel>()
        list.add(homescreenmodel)
        list.add(homescreenmodel1)
        list.add(homescreenmodel2)
        list.add(homescreenmodel3)
        list.add(homescreenmodel4)
        list.add(homescreenmodel5)
        list.add(homescreenmodel6)

        val layoutManager = GridLayoutManager(applicationContext, 2)
        rv.setLayoutManager(layoutManager)
        val adaptorHOme = HomeAdaptor(this@DashBoardActivity,this@DashBoardActivity, list)
        rv.setAdapter(adaptorHOme)
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return toggle.onOptionsItemSelected(item)||super.onOptionsItemSelected(item)
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item?.itemId) {
            R.id.home->
            {
                startActivity(Intent(this,DashBoardActivity::class.java))
            }
            R.id.query ->{
                startActivity(Intent(this@DashBoardActivity,MainActivity::class.java))
            }
            R.id.menu_close -> {
                startActivity(Intent(this@DashBoardActivity, CLosedQueryActivity::class.java))
            }
            R.id.menu_group -> {
                startActivity(Intent(this@DashBoardActivity, GroupActivity::class.java))
            }
            R.id.Logout -> {
                var edit = mLoginPreference.sharedPreferences!!.edit()
                edit.remove("id")
                //edit.clear()
                edit.apply()
                var i = Intent(this@DashBoardActivity, LoginActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

                startActivity(i)
//                this@MainActivity.finish()
            }
            R.id.timesheet -> {
                startActivity(Intent(this@DashBoardActivity, FillTimeSheet::class.java))
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
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }else {
            super.onBackPressed()
            finishAffinity()
             this@DashBoardActivity.finish()
        }
    }
    override fun onStart() {
        super.onStart()
        if(!isNetworkAvailable())
        {
            Toast.makeText(this@DashBoardActivity,"No Internet Connection", Toast.LENGTH_LONG).show()
        }
    }
}
