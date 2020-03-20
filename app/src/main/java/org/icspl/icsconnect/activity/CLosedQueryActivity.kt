package org.icspl.icsconnect.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_closed_query.*
import kotlinx.android.synthetic.main.activity_main.*
import org.icspl.icsconnect.MainActivity
import org.icspl.icsconnect.R
import org.icspl.icsconnect.adapters.CloesedAdapter
import org.icspl.icsconnect.models.CloseMessage
import org.icspl.icsconnect.models.HomescreenModel
import org.icspl.icsconnect.preferences.LoginPreference
import org.icspl.icsconnect.utils.Common


class CLosedQueryActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {

    private lateinit var back:ImageView
    private val mService by lazy { Common.getAPI() }
    private val mLoginPreference by lazy { LoginPreference.getInstance(this@CLosedQueryActivity) }
    private val mDisposable = CompositeDisposable()
    private var mToolbar: Toolbar? = null
    private var mAdapter: CloesedAdapter? = null
    private lateinit var mList: ArrayList<CloseMessage.ClosedMSGDetails>
    private lateinit var menu: Menu
    private lateinit var user:String
    private lateinit var master_admin:String

    private var item: MenuItem? = null
    private lateinit var toggle:ActionBarDrawerToggle

    // private lateinit var rv : RecyclerView.Recycler
    lateinit var list: MutableList<HomescreenModel>
    var context=this@CLosedQueryActivity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_closed_query)
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

        navigation_view_closedquery.setNavigationItemSelectedListener(this)




        var context=this@CLosedQueryActivity

        initViews()
    }

    private fun initViews() {

        val manager = LinearLayoutManager(this@CLosedQueryActivity)
        rv_closed.setHasFixedSize(true)
        manager.reverseLayout = false;
        manager.stackFromEnd = true;
        rv_closed.layoutManager = manager
        mList = ArrayList()
        mAdapter = CloesedAdapter(mList)
        fetchClosedMSGData()


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

    private fun fetchClosedMSGData() {
        progress_close.visibility = View.VISIBLE
        mDisposable.add(
            mService.allCloseQuery(mLoginPreference.getStringData("id","")!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ s ->
                    if (s != null && !s.closeMessages.isNullOrEmpty()) {
                        mList?.clear()
                        mList.addAll(s.closeMessages as ArrayList<CloseMessage.ClosedMSGDetails>)
                        rv_closed.adapter = mAdapter
                        mAdapter!!.notifyDataSetChanged()
                        progress_close.visibility = View.GONE

                    } else{
                        progress_close.visibility = View.GONE
                        Toast.makeText(

                            this@CLosedQueryActivity, "No Closed Query",
                            Toast.LENGTH_SHORT
                        ).show()}
                }, { throwable ->
                    progress_close.visibility = View.GONE
                    Log.i("Error:", throwable.message)
                })
        )
    }

    override fun onStop() {

        mList.clear()
        mDisposable.clear()
        super.onStop()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}
