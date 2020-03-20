package org.icspl.icsconnect.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log.i
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_opend_count.*
import kotlinx.android.synthetic.main.fragment_opend_count.view.*
import org.icspl.icsconnect.activity.CountMSGDetailsActivity
import org.icspl.icsconnect.adapters.OpenedCountAdapter
import org.icspl.icsconnect.models.CountMSGDetailsModel
import org.icspl.icsconnect.models.CountMSGModel
import org.icspl.icsconnect.preferences.LoginPreference
import org.icspl.icsconnect.utils.Common
import java.io.File


class OpenedCountFragment : androidx.fragment.app.Fragment(), OpenedCountAdapter.CounterListener {


    private lateinit var mView: View
    private lateinit var mContext: Context
    private lateinit var mAdapter: OpenedCountAdapter
    private val mDisposable = CompositeDisposable()
    internal var imageBtnFile: File? = null
    private var imageToUploadUri: Uri? = null
    private val CAMERA_REQUEST_CODE: Int = 2541
    private var cameraButton: Button? = null
    private val mService by lazy { Common.getAPI() }
    var countList = arrayListOf<CountMSGModel.Countmessage>()
    private val mLoginPreference by lazy { LoginPreference.getInstance(mContext) }


    companion object {
        private val TAG = OpenedCountFragment::getTag.name
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(org.icspl.icsconnect.R.layout.fragment_opend_count, container, false)
        init()
        return mView
    }

    private fun fetchCountMsgs() {

        mView.progress_open.visibility = View.VISIBLE
        mDisposable.add(
            mService.getCountMSg(mLoginPreference.getStringData("id", "ICS/123")!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ s   ->
                    if (s != null) {
                        s.countmessageList?.forEach {
                            countList.add(it)
                        }
                        mAdapter.notifyDataSetChanged()
                        s.sendbyyou?.forEach {

                            var sby = CountMSGModel().Countmessage()
                            sby.fromEmpName = it.toEmpName
                            sby.fromemp = it.toemp
                            sby.msgCount = it.countmessage
                            sby.photo = it.photo
                            sby.toemp = it.fromemp
                            sby.isIAm = true

                            i(TAG, "${it.photo}")
                            countList.add(sby)
                        }

                        // countList.add(s.countmessageList!!.get(0))
                        /*  s.countmessageList?.forEach {

                          }*/
                        mAdapter.notifyDataSetChanged()
                    } else
                        i(TAG, "Null Data:")
                    progress_open.visibility = View.GONE
                }, { throwable ->
                    i(TAG, throwable.message)
                    progress_open.visibility = View.GONE
                })
        )

    }

    private fun init() {
        mContext = mView.context
        //questionsList = arrayListOf()
        val mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            mContext,
            RecyclerView.VERTICAL,
            false
        )
        mView.rv_groups.layoutManager = mLayoutManager
        mView.rv_groups.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        mAdapter = OpenedCountAdapter(countList, mContext, this)
        mAdapter.notifyDataSetChanged()
        mView.rv_groups.adapter = mAdapter
        fetchCountMsgs()
    }


    override fun onPause() {
        mDisposable.clear()
        super.onPause()
    }


    // get counter id and and get specific users alll opend query
    override fun clickListenere(id: String, photo: String?, isMineQuery: Boolean) {
        var fromId: String
        var toID: String
        if (isMineQuery) {
            mLoginPreference.savStringeData("fromemp","yes")
            toID = mLoginPreference.getStringData("id", "ICS/123")!!
            fromId = id
        } else {
            mLoginPreference.savStringeData("fromemp","no")
            fromId = mLoginPreference.getStringData("id", "ICS/123")!!
            toID = id
        }
        var mList = arrayListOf<CountMSGDetailsModel.IndividualDetail>()

        progress_open.visibility = View.VISIBLE
        mDisposable.add(
            mService.getCountDetails(fromId, toID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ s ->
                    if (s != null) {
                        s.individualDetails?.forEach {
                            i(TAG, it.queryid)
                            mList.run {
                                it.photoPath = photo
                                add(it)

                            }
                        }
                        progress_open.visibility = View.GONE
                        startActivity(
                            Intent(mContext, CountMSGDetailsActivity::class.java)
                                .putExtra("data", mList)
                        )
                    } else
                        i(TAG, "Null Data:")
                    progress_open.visibility = View.GONE
                }, { throwable ->
                    progress_open.visibility = View.GONE
                    i(TAG, throwable.message)
                })
        )
    }

}

// Your App ID: 28824fb7-a421-4f36-a73b-170c0872c47b