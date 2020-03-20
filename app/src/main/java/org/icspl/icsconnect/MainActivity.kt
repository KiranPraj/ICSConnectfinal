package org.icspl.icsconnect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import org.icspl.icsconnect.fragments.OpenedCountFragment
import org.icspl.icsconnect.preferences.LoginPreference
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.tonyodev.fetch2core.isNetworkAvailable
import kotlinx.android.synthetic.main.toolbar.*
import org.icspl.icsconnect.activity.*
import org.icspl.icsconnect.models.HomescreenModel


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val mLoginPreference by lazy { LoginPreference.getInstance(this@MainActivity) }
    private lateinit var mToolbar: Toolbar
    private lateinit var menu: Menu
    private var item: MenuItem? = null
    private lateinit var toggle:ActionBarDrawerToggle
    private lateinit var back:ImageView
   // private lateinit var rv : RecyclerView.Recycler
   lateinit var list: MutableList<HomescreenModel>
    var context=this@MainActivity
    companion object {
        val TAG = MainActivity::class.java.canonicalName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        back= findViewById(R.id.back)
        mToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(mToolbar)
        back.setOnClickListener {
            onBackPressed()
        }
        //mToolbar.setTitle("ICS Connect")

         toggle = ActionBarDrawerToggle(this, drawer_layout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        toggle.isDrawerIndicatorEnabled=true
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        navigation_view.setNavigationItemSelectedListener(this)


        var context=this@MainActivity


        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result!!.token
                mLoginPreference.savStringeData("token", token)

                Log.d(TAG, "Firebase Token: " + token)
            })
           // gridview()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, OpenedCountFragment(), getString(R.string.count_fragment))
            .commit()
        //fab.visibility=View.GONE
        fab.setOnClickListener {
            if(isNetworkAvailable()){
            val intent = Intent(this@MainActivity, RaiseQueryActivity::class.java)
            startActivity(intent)
            }
            else
            {
                Toast.makeText(context,"please connect internet",Toast.LENGTH_SHORT).show()
            }
        }
        //navigation view
    }

   /**/

    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(org.icspl.icsconnect.R.menu.menu_open, menu)
        this.menu = menu
        item = menu.findItem(org.icspl.icsconnect.R.id.menu_close)
        item = menu.findItem(
            org.icspl.icsconnect.R.id.deletegrp

        ).setVisible(false)
        return true
    }*/


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.home->
            {
                startActivity(Intent(this@MainActivity,DashBoardActivity::class.java))
            }
            R.id.query ->{
            startActivity(Intent(this,MainActivity::class.java))
            }
            R.id.menu_close -> {
                startActivity(Intent(this@MainActivity, CLosedQueryActivity::class.java))
            }
            R.id.menu_group -> {
                startActivity(Intent(this@MainActivity, GroupActivity::class.java))
            }
            R.id.Logout -> {
                var edit = mLoginPreference.sharedPreferences!!.edit()
                edit.remove("id")
                //edit.clear()
                edit.apply()
                var i = Intent(this@MainActivity, LoginActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

                startActivity(i)
//                this@MainActivity.finish()
            }
            R.id.timesheet -> {
                startActivity(Intent(this@MainActivity, FillTimeSheet::class.java))
            }
        }
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
      /*  when (item?.itemId) {
            R.id.menu_close -> {
                startActivity(Intent(this@MainActivity, CLosedQueryActivity::class.java))
            }
            R.id.menu_group -> {
                startActivity(Intent(this@MainActivity, GroupActivity::class.java))
            }
            R.id.Logout -> {
                var edit = mLoginPreference.sharedPreferences!!.edit()
                edit.remove("id")
                //edit.clear()
                edit.apply()
                var i = Intent (this@MainActivity,LoginActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

                startActivity(i)
//                this@MainActivity.finish()
            }
            R.id.timesheet ->{
                startActivity(Intent(this@MainActivity, FillTimeSheet::class.java))
            }
        }
        return true*/
        return toggle.onOptionsItemSelected(item)||super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }else {
            super.onBackPressed()
           // finishAffinity()
            //this@MainActivity.finish()
            // return
        }
    }

    override fun onStart() {
        super.onStart()
        if(!isNetworkAvailable())
        {
            Toast.makeText(context,"No Internet Connection",Toast.LENGTH_LONG).show()
        }
    }

}
