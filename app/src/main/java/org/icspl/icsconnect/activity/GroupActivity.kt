package org.icspl.icsconnect.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.icspl.icsconnect.R
import org.icspl.icsconnect.fragments.GrpNameFragmentFragment
import org.icspl.icsconnect.fragments.MasterAdminFragment
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import com.google.android.material.navigation.NavigationView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_closed_query.*
import kotlinx.android.synthetic.main.activity_closed_query.drawer_layout_SS
import kotlinx.android.synthetic.main.activity_group.*
import org.icspl.icsconnect.MainActivity
import org.icspl.icsconnect.adapters.CloesedAdapter
import org.icspl.icsconnect.models.CloseMessage
import org.icspl.icsconnect.models.HomescreenModel
import org.icspl.icsconnect.preferences.LoginPreference
import org.icspl.icsconnect.utils.Common


class GroupActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {


  private lateinit var back:ImageView
  private val mService by lazy { Common.getAPI() }
  private val mLoginPreference by lazy { LoginPreference.getInstance(this@GroupActivity) }
  private val mDisposable = CompositeDisposable()
  private var mToolbar: Toolbar? = null
  private var mAdapter: CloesedAdapter? = null
  private lateinit var mList: ArrayList<CloseMessage.ClosedMSGDetails>
  private lateinit var menu: Menu
  private lateinit var user:String
  private lateinit var master_admin:String

  private var item: MenuItem? = null
  private lateinit var toggle: ActionBarDrawerToggle

  // private lateinit var rv : RecyclerView.Recycler
  lateinit var list: MutableList<HomescreenModel>
  var context=this@GroupActivity

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_group)

    master_admin=mLoginPreference.getStringData("masteradmin","").toString()
    user=mLoginPreference.getStringData("id","").toString()
    back= findViewById(R.id.back)
    mToolbar = findViewById(R.id.toolbar)
    back.setOnClickListener {
      onBackPressed()
    }
    setSupportActionBar(mToolbar)

    //mToolbar.setTitle("ICS Connect")

    toggle = ActionBarDrawerToggle(this, drawer_layout_SS, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

    toggle.isDrawerIndicatorEnabled=true
    drawer_layout_SS.addDrawerListener(toggle)
    toggle.syncState()

    navigation_view_group.setNavigationItemSelectedListener(this)




    var context=this@GroupActivity

    supportFragmentManager.beginTransaction()
      .add(org.icspl.icsconnect.R.id.group_container, MasterAdminFragment(), getString(org.icspl.icsconnect.R.string.master_admin_frag))
      .commit()

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
  override fun onBackPressed() {
    super.onBackPressed()
  }
}
