package org.icspl.icsconnect.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_closed_query.*
import org.icspl.icsconnect.R
import org.icspl.icsconnect.adapters.CloesedAdapter
import org.icspl.icsconnect.models.CloseMessage
import org.icspl.icsconnect.preferences.LoginPreference
import org.icspl.icsconnect.utils.Common


class CLosedQueryActivity : AppCompatActivity() {
    private val mService by lazy { Common.getAPI() }
    private val mLoginPreference by lazy { LoginPreference.getInstance(this@CLosedQueryActivity) }
    private val mDisposable = CompositeDisposable()
    private var mToolbar: Toolbar? = null
    private var mAdapter: CloesedAdapter? = null
    private lateinit var mList: ArrayList<CloseMessage.ClosedMSGDetails>
    private lateinit var menu: Menu


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_closed_query)


        initViews()
    }

    private fun initViews() {
        mToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar!!.title = "All Closed Query"
        val manager = androidx.recyclerview.widget.LinearLayoutManager(this@CLosedQueryActivity)
        rv_closed.setHasFixedSize(true)
        manager.reverseLayout = false;
        manager.stackFromEnd = true;
        rv_closed.layoutManager = manager
        mList = ArrayList()
        mAdapter = CloesedAdapter(mList)
        fetchClosedMSGData()

    }

    private fun fetchClosedMSGData() {
        progress_close.visibility = View.VISIBLE
        mDisposable.add(
            mService.allCloseQuery("ICS/167")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ s ->
                    if (s != null && !s.closeMessages.isNullOrEmpty()) {
                        mList?.clear()
                        mList.addAll(s.closeMessages as ArrayList<CloseMessage.ClosedMSGDetails>)
                        rv_closed.adapter = mAdapter
                        mAdapter!!.notifyDataSetChanged()
                        progress_close.visibility = View.GONE

                    } else
                        Toast.makeText(
                            this@CLosedQueryActivity, "Query Failed to Load",
                            Toast.LENGTH_SHORT
                        ).show()
                }, { throwable ->
                    Log.i("Error:", throwable.message)
                })
        )
    }

    override fun onStop() {

        mList.clear()
        mDisposable.clear()
        super.onStop()
    }

}
