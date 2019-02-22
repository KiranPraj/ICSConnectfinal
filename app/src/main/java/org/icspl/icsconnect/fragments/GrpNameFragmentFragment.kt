package org.icspl.icsconnect.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_grpnamefragment_list.view.*
import org.icspl.icsconnect.R
import org.icspl.icsconnect.activity.GroupChatActivity
import org.icspl.icsconnect.adapters.GroupNameAdapter
import org.icspl.icsconnect.models.IndividualGrpNameModel
import org.icspl.icsconnect.preferences.LoginPreference
import org.icspl.icsconnect.utils.Common


class GrpNameFragmentFragment : androidx.fragment.app.Fragment(), GroupNameAdapter.GrpNameCallback {

    private val mLoginPreference by lazy { LoginPreference.getInstance(requireContext()) }
    private val mService by lazy { Common.getAPI() }
    private val mDisposable = CompositeDisposable()
    private lateinit var mView: View
    private lateinit var mAdapter: GroupNameAdapter
    private var mNameList: MutableList<IndividualGrpNameModel>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_grpnamefragment_list, container, false)

        initViews()
        return mView
    }

    // init all views
    private fun initViews() {

        getMemberGroupNames()

    }


    private fun getMemberGroupNames() {
        mView.pb_grp_name.visibility = View.VISIBLE
        mDisposable.add(
            mService.getIndividualGroups(
                "ICSA/1633"// mLoginPreference.getStringData("id", "")!!
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ s ->
                    if (s != null) {
                        initRecyclerView(s.individualGroupsList)
                    } else {
                        Snackbar.make(mView.ll_grp_name, "No Data Found", Snackbar.LENGTH_LONG).show()
                    }
                }, { throwable ->
                    mView.pb_grp_name.visibility = View.GONE
                })
        )
    }

    private fun initRecyclerView(individualGroupsList: List<IndividualGrpNameModel.IndividualGroup>?) {
        mView.pb_grp_name.visibility = View.GONE
        mView.rv_group_name.hasFixedSize()
        mView.rv_group_name.layoutManager = LinearLayoutManager(
            requireContext(), RecyclerView.VERTICAL, false
        )
        mView.rv_group_name.itemAnimator = DefaultItemAnimator()
        mAdapter = GroupNameAdapter(individualGroupsList!!, requireContext(), this@GrpNameFragmentFragment)
        mView.rv_group_name.adapter = mAdapter
        mAdapter.notifyDataSetChanged()
    }

    override fun onStop() {
        mDisposable.clear()
        super.onStop()
    }

    override fun GrpNameListener(id: String, photo: String?, isMineQuery: Boolean) {
        startActivity(Intent(requireActivity(), GroupChatActivity::class.java))
    }

}
