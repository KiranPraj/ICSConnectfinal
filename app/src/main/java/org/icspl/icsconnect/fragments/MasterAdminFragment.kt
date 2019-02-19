package org.icspl.icsconnect.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.Log.i
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.google.android.material.chip.Chip
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_conversation.*
import kotlinx.android.synthetic.main.fragment_master_admin.*
import kotlinx.android.synthetic.main.fragment_master_admin.view.*
import kotlinx.android.synthetic.main.fragment_opend_count.*
import kotlinx.android.synthetic.main.fragment_opend_count.view.*
import org.icspl.icsconnect.R
import org.icspl.icsconnect.adapters.OpenedCountAdapter
import org.icspl.icsconnect.models.CountMSGModel
import org.icspl.icsconnect.preferences.LoginPreference
import org.icspl.icsconnect.utils.Common
import org.icspl.icsconnect.utils.SearchableSpinner


class MasterAdminFragment : androidx.fragment.app.Fragment() {


    private lateinit var mView: View
    private lateinit var mContext: Context
    private lateinit var mAdapter: OpenedCountAdapter
    private val mDisposable = CompositeDisposable()
    private val mService by lazy { Common.getAPI() }
    var countList = arrayListOf<CountMSGModel.Countmessage>()
    private val mLoginPreference by lazy { LoginPreference.getInstance(mContext) }
    private lateinit var mAdminList: MutableSet<String>


    companion object {
        private val TAG = MasterAdminFragment::getTag.name
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(org.icspl.icsconnect.R.layout.fragment_master_admin, container, false)
        init()
        return mView
    }

    private fun fetchCountMsgs() {

        mView.progress_open.visibility = View.VISIBLE
        mDisposable.add(
            mService.getCountMSg(mLoginPreference.getStringData("id", "ICS/123")!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ s ->
                    if (s != null) {
                        s.countmessageList?.forEach {
                            countList.add(it)
                        }
                        mAdapter.notifyDataSetChanged()
                        s.sendbyyou?.forEach {

                            val sby = CountMSGModel().Countmessage()
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
        mView.ll_create_groups_body.visibility = View.GONE
        mView.btn_create_groups.setOnClickListener {
            mView.ll_create_groups.postDelayed(
                {
                    mView.ll_create_groups.visibility = View.GONE
                    mView.ll_create_groups_body.visibility = View.VISIBLE

                },
                500
            )
            initSpinner()
            mView.btn_create_grp.setOnClickListener {
                if (mView.et_group_name.text!!.isEmpty()) {
                    Toast.makeText(requireContext(), "Group Name Required:", Toast.LENGTH_SHORT).show()
                    mView.til_group_name.isErrorEnabled = true
                    mView.til_group_name.error = "Group Name not be Empty"
                    return@setOnClickListener
                }
                if (mView.til_group_name.isErrorEnabled)
                    mView.til_group_name.isErrorEnabled = false

                val listAdmin = arrayListOf<String>()
                for (item: Int in 0 until viewgrp.childCount) {
                    val chip = viewgrp.getChildAt(item) as Chip
                    i(TAG, "Child Chip: ${chip.text}")
                    listAdmin.add(chip.text.toString())

                }
                val listMember = arrayListOf<String>()
                for (item: Int in 0 until viewgrp_member.childCount) {
                    val chip = viewgrp_member.getChildAt(item) as Chip
                    i(TAG, "Child Chip: ${chip.text}")
                    listMember.add(chip.text.toString())
                }
                i(TAG, "Admin: ${listAdmin.toTypedArray()}")
                i(TAG, "Members: ${listMember}")


                mDisposable.add(
                    mService.createGroup(
                        et_group_name.text.toString(), listAdmin.toString(), listAdmin.toString(),
                        listMember.toString()
                    )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ s ->
                            if (s != null) {
                                Toast.makeText(mContext, "Grp Created", Toast.LENGTH_LONG).show()
                            }
                        }, { throwable ->
                            //progress_chat.visibility = View.GONE
                            Log.i("Error:", throwable.message)
                        })
                )

            }
            /*mView.rv_groups.layoutManager = mLayoutManager
            mView.rv_groups.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
            mAdapter = OpenedCountAdapter(countList, mContext, this)
            mAdapter.notifyDataSetChanged()
            mView.rv_groups.adapter = mAdapter
            fetchCountMsgs()*/
        }

        /*  mView.btn_add_admin.setOnClickListener {
              val mSpinner = SearchableSpinner(requireContext())
              mSpinner.setTitle("Choose Admin")

              mSpinner.setPositiveButton("Add", object : DialogInterface.OnClickListener {
                  override fun onClick(dialog: DialogInterface?, which: Int) {

                  }

              })

          }*/
    }

    private fun initSpinner() {
        if (!Common.isConnectedMobile(requireContext())) {
            Toast.makeText(requireContext(), "No Internet Connection", Toast.LENGTH_LONG).show()
            return
        }
        mAdminList = mutableSetOf()
        val searchHashList = mutableMapOf<String, String>()

        mDisposable.add(
            mService.search("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ s ->
                    if (s.isNotEmpty()) {
                        mAdminList.clear()
                        mAdminList.add("Select Admin")
                        s.forEach { mAdminList.add(it.id.toString()) }
                        mAdminList.clear()
                        mAdminList.add("Select Members")
                        s.forEach {
                            mAdminList.add(it.id.toString())
                            searchHashList.put(it.id.toString().plus("-").plus(it.name.toString()), it.id.toString())
                        }
                        sp(mView.sp_add_members, searchHashList)
                        sp(mView.sp_add_admin, searchHashList)
                        mView.sp_add_admin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                if (position > 0) {
                                    val chip = Chip(requireContext())
                                    chip.text = searchHashList[(view as TextView).text.toString()]
                                    val layoutParams = LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
                                    )
                                    layoutParams.setMargins(10, 20, 10, 10)
                                    chip.setLayoutParams(layoutParams)

                                    chip.isCloseIconVisible = true
                                    chip.closeIcon =
                                        ContextCompat.getDrawable(requireContext(), R.drawable.ic_close_black_24dp)
                                    chip.chipIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_person)
                                    chip.setTextColor(Color.WHITE)
                                    chip.setChipIconTintResource(R.color.colorGreen)

                                    chip.setOnCloseIconClickListener {
                                        TransitionManager.beginDelayedTransition(viewgrp)
                                        viewgrp.removeView(chip)
                                    }
                                    mView.viewgrp.addView(chip)
                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }
                        }

                        mView.sp_add_members.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                if (position > 0) {
                                    val chip = Chip(requireContext())
                                    chip.text = searchHashList[(view as TextView).text.toString()]

                                    val layoutParams = LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
                                    )
                                    layoutParams.setMargins(10, 20, 10, 10)
                                    chip.setLayoutParams(layoutParams)
                                    chip.isCloseIconVisible = true
                                    chip.closeIcon =
                                        ContextCompat.getDrawable(requireContext(), R.drawable.ic_close_black_24dp)
                                    chip.chipIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_person)
                                    chip.setTextColor(Color.WHITE)
                                    chip.setChipIconTintResource(R.color.colorGreen)

                                    chip.setOnCloseIconClickListener {
                                        TransitionManager.beginDelayedTransition(viewgrp_member)
                                        viewgrp_member.removeView(chip)
                                    }
                                    mView.viewgrp_member.addView(chip)
                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }
                        }

                    }

                }, { throwable ->
                    progress_chat.visibility = View.GONE
                    Log.i("Error:", throwable.message)
                })
        )


    }

    fun sp(sp: SearchableSpinner, searchHashList: MutableMap<String, String>) {

        val roAdapter =
            ArrayAdapter<String>(
                mContext,
                android.R.layout.simple_spinner_dropdown_item,
                searchHashList.keys.toList()
            )

        sp.adapter = roAdapter

    }

    override fun onPause() {
        mDisposable.clear()
        super.onPause()
    }
}



