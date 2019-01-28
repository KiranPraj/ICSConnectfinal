package org.icspl.icsconnect.activity

import LoginPreference
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.util.Log.i
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_count_msgdetails.*
import org.icspl.icsconnect.R
import org.icspl.icsconnect.adapters.CountDetailsAdapter
import org.icspl.icsconnect.models.CountMSGDetailsModel
import org.icspl.icsconnect.utils.Common

class CountMSGDetailsActivity : AppCompatActivity(), CountDetailsAdapter.CounterListener {


    private val mLoginPreference by lazy { LoginPreference.getInstance(this@CountMSGDetailsActivity) }
    private val mService by lazy { Common.getAPI() }
    private val mDisposable = CompositeDisposable()
    private lateinit var mList: ArrayList<CountMSGDetailsModel.IndividualDetail>
    private lateinit var mAdapter: CountDetailsAdapter

    companion object {
        private val TAG = CountMSGDetailsActivity::class.java.simpleName
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count_msgdetails)

        if (intent != null) {
            mList = intent.getParcelableArrayListExtra("data")
        }
        initRecyclerView()

    }

    private fun initRecyclerView() {
        val mLayoutManager = LinearLayoutManager(this@CountMSGDetailsActivity, LinearLayoutManager.VERTICAL, false)
        rv_count_details.layoutManager = mLayoutManager
        rv_count_details.itemAnimator = DefaultItemAnimator()
        mAdapter = CountDetailsAdapter(mList, this@CountMSGDetailsActivity, this@CountMSGDetailsActivity)
        rv_count_details.adapter = mAdapter

        mList.forEach {
            i(TAG, "Details: ${it.remarks}")
        }


        mAdapter.notifyDataSetChanged()

    }

    override fun onPause() {
        mDisposable.clear()
        super.onPause()
    }

    private fun fetchCountMsgs() {

        mDisposable.add(
            mService.getCountMSg(mLoginPreference.getStringData("id", "ICS/123")!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ s ->
                    if (s != null) {
                        s.countmessageList?.forEach {
                            //  countList.add(it)

                        }
                        // countList.add(s.countmessageList!!.get(0))
                        /*  s.countmessageList?.forEach {

                          }*/
                        // mAdapter.notifyDataSetChanged()
                    } else
                        Log.i(TAG, "Null Data:")
                }, { throwable ->
                    Log.i(TAG, throwable.message)
                })
        )

    }

    // adapters  implemented methods
    override fun clickListenere(id: String, photo: String?, toemp: String?) {
        startActivity(
            Intent(this@CountMSGDetailsActivity, ChatActivity::class.java)
                .putExtra("queryId", id)
                .putExtra("photo", photo)
                .putExtra("toemp", toemp)
        )
    }

}
