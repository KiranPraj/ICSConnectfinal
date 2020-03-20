package org.icspl.icsconnect.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_leave.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.drawer_layout
import org.icspl.icsconnect.MainActivity
import org.icspl.icsconnect.R
import org.icspl.icsconnect.fragments.ExpensesHomeFragment
import org.icspl.icsconnect.fragments.LeaveHomeFragment
import org.icspl.icsconnect.models.HomescreenModel
import org.icspl.icsconnect.preferences.LoginPreference

class Expenses : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener
{
    private val mLoginPreference by lazy { LoginPreference.getInstance(this) }
    private lateinit var back: ImageView
    private lateinit var mToolbar: Toolbar
    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var list: MutableList<HomescreenModel>
    private lateinit var user:String
    private lateinit var master_admin:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenses)
        mToolbar = findViewById(R.id.toolbar)
        back= findViewById(R.id.back)
        master_admin=mLoginPreference.getStringData("masteradmin","").toString()
        user=mLoginPreference.getStringData("id","").toString()
        back.setOnClickListener(View.OnClickListener {
            // finish()
            onBackPressed()
        })
        setSupportActionBar(mToolbar)
        toggle = ActionBarDrawerToggle(this, drawer_layout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.isDrawerIndicatorEnabled=true
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        val menu = navigation_view_leave.menu

        val nav_dashboard = menu.findItem(R.id.deletegrp)
        if(master_admin==user)
        {
            nav_dashboard.setVisible(true)
        }
        // linearview()
        navigation_view_leave.setNavigationItemSelectedListener(this)
        supportFragmentManager.beginTransaction()
            .add(org.icspl.icsconnect.R.id.group_container, ExpensesHomeFragment(), getString(org.icspl.icsconnect.R.string.expense_home_frg))
            .commit()

    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item!!.itemId) {
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

    }
    override fun onBackPressed() {
        val fm = fragmentManager
        if (fm.backStackEntryCount > 0) {
            Log.i("MainActivity", "popping backstack")
            fm.popBackStack()
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super")
            super.onBackPressed()
        }
    }
}
