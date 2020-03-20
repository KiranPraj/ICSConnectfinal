package org.icspl.icsconnect.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.Log.i
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.transition.TransitionManager
import com.google.android.material.chip.Chip
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_conversation.*
import kotlinx.android.synthetic.main.fragment_master_admin.*
import kotlinx.android.synthetic.main.fragment_master_admin.view.*
import org.icspl.icsconnect.MainActivity
import org.icspl.icsconnect.R
import org.icspl.icsconnect.models.*
import org.icspl.icsconnect.preferences.LoginPreference
import org.icspl.icsconnect.utils.Common
import org.icspl.icsconnect.utils.SearchableSpinner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MasterAdminFragment : androidx.fragment.app.Fragment() {

    private lateinit var mView: View
    private lateinit var mContext: Context
    private val mDisposable = CompositeDisposable()
    private val mService by lazy { Common.getAPI() }
    private val mLoginPreference by lazy { LoginPreference.getInstance(mContext) }
    private lateinit var mAdminList: MutableSet<String>
    private lateinit var mCompanyList: MutableSet<String>
    private lateinit var mRegionList: MutableSet<String>
    private lateinit var mStationList: MutableSet<String>
    private lateinit var mStatusList: MutableSet<String>
    private lateinit var mDepartmentList: MutableSet<String>
    private lateinit var mMemberList:MutableSet<String>


    companion object {
        private val TAG = MasterAdminFragment::getTag.name
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_master_admin, container, false)
        init()
     return mView
    }

    private fun init() {
        mContext = mView.context
        mView.btn_create_grp.visibility=View.INVISIBLE
        var i=0

        // var s = Intent.getIntent("Group_id")
        var k = mLoginPreference.getStringData("groupid", "")
        if (k != "") {
            i=1;
            var edit = mLoginPreference.sharedPreferences!!.edit()
            //edit.remove("id")
            edit.remove("groupid")
            var id = mLoginPreference.getStringData("id", "")
            var master = mLoginPreference.getStringData("masterAdmin", "")
            var Admin = mLoginPreference.getStringData("Admin", "")
            var Member = mLoginPreference.getStringData("members", "")
            var title = mLoginPreference.getStringData("title", "")
            edit.remove("Admin")
            edit.remove("members")
            edit.remove("title")
            edit.commit()

            Admin = Admin!!.replace("[", "")
            Admin = Admin!!.replace("]", "")
            Member = Member!!.replace("[", "")
            Member = Member!!.replace("]", "")
            val admin = Admin!!.split(",")
            val member = Member!!.split(",")
            //var pos=mLoginPreference.getStringData("position","")
            mView.ll_create_groups_body.visibility = View.VISIBLE
            mView.ll_create_groups.visibility = View.GONE
            mView.et_group_name.setText(title)
            mView.sp_add_admin.visibility = View.INVISIBLE
            for (i: Int in 0 until admin.size) {
                val chip = Chip(requireContext())
                chip.text = admin[i]
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(10, 20, 10, 10)
                chip.layoutParams = layoutParams

                chip.isCloseIconVisible = true
                chip.closeIcon =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_close_black_24dp)
                chip.chipIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_person)
                chip.setTextColor(Color.WHITE)
                chip.setChipIconTintResource(R.color.colorGreen)
                if (id == master) {
                    mView.sp_add_admin.visibility = View.VISIBLE
                    chip.setOnCloseIconClickListener {
                        TransitionManager.beginDelayedTransition(viewgrp)
                        viewgrp.removeView(chip)
                    }
                }
                if (chip.text != "") {
                    mView.viewgrp.addView(chip)
                }
            }
            for (i: Int in 0 until member.size) {
                val chip = Chip(requireContext())

                chip.text = member[i]

                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
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
                //Toast.makeText(context,chip.text,Toast.LENGTH_LONG).show()
                if (chip.text != "") {
                    mView.viewgrp_member.addView(chip)
                }
            }
//            mView.btn_create_groups.setOnClickListener {
//                mView.ll_create_groups.postDelayed(
//                    {
//                        mView.ll_create_groups.visibility = View.GONE
//                        mView.ll_create_groups_body.visibility = View.VISIBLE
//
//                    },
//                    500
//                )
//            }

            initSpinner()
            companyspinner()

            mView.btn_create_grp.setOnClickListener {
                if (mView.et_group_name.text!!.isEmpty()) {
                    Toast.makeText(requireContext(), "Group Name Required:", Toast.LENGTH_SHORT).show()
                  //  mView.til_group_name.isErrorEnabled = true
                    mView.til_group_name.error = "Group Name not be Empty"
                    return@setOnClickListener
                }
          /*      if (mView.til_group_name.isErrorEnabled)
                    mView.til_group_name.isErrorEnabled = false
*/
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

                mDisposable.add(
                    mService.createGroup(
                        et_group_name.text.toString(),
                        master!!,
                        listAdmin.toString(),
                        listMember.toString(), k!!
                    )

                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ s ->
                            if (s != null) {
                                if (s.get(0).response!! >= 1) {
                                    Toast.makeText(mContext, "Group Updated", Toast.LENGTH_LONG).show()
                                    startActivity(
                                        Intent(
                                            context, MainActivity
                                            ::class.java
                                        )
                                    )
                                    activity!!.finish()
                                } else {

                                    Toast.makeText(
                                        mContext,
                                        "Only Master Admin can create Group",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } else {
                                Toast.makeText(mContext, "You cannot create group", Toast.LENGTH_LONG).show()
                            }
                        }, { throwable ->
                            //progress_chat.visibility = View.GONE
                            Log.i("Error:", throwable.message)
                        })
                )


            }

        } else {

            mView.ll_create_groups_body.visibility = View.GONE

            mView.btn_view_groups.setOnClickListener {

                var list: List<IndividualGrpNameModel.IndividualGroup>


                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.group_container, GrpNameFragmentFragment(), getString(R.string.master_admin_frag))
                    .commit()
            }

            mView.btn_create_groups.setOnClickListener {
                mView.ll_create_groups.postDelayed(
                    {
                        mView.ll_create_groups.visibility = View.GONE
                        mView.ll_create_groups_body.visibility = View.VISIBLE

                    },
                    500
                )
                initSpinner()
                companyspinner()

                mView.btn_create_grp.setOnClickListener {

                    if (mView.et_group_name.text!!.isEmpty()) {
                        Toast.makeText(requireContext(), "Group Name Required:", Toast.LENGTH_SHORT).show()
                    //    mView.til_group_name.isErrorEnabled = true
                        mView.til_group_name.error = "Group Name not be Empty"
                        return@setOnClickListener
                    }
                 /*   if (mView.til_group_name.isErrorEnabled)
                        mView.til_group_name.isErrorEnabled = false
*/
                    val listAdmin = arrayListOf<String>()
                    for (item: Int in 0 until viewgrp.childCount) {
                        val chip = viewgrp.getChildAt(item) as Chip
                        i(TAG, "Child Chip: ${chip.text}")
                        listAdmin.add(chip.text.toString())

                    }



                    var listMember = mutableSetOf<String>()
                    for (item: Int in 0 until viewgrp_member.childCount) {
                        val chip = viewgrp_member.getChildAt(item) as Chip
                        if(chip.text=="All")
                        {
                            listMember=mMemberList
                        }
                        else
                        {
                            listMember.add(chip.text.toString())
                        }
                        i(TAG, "Child Chip: ${chip.text}")

                    }

                    mDisposable.add(
                        mService.createGroup(
                            et_group_name.text.toString(),
                            mLoginPreference.getStringData("id", "")!!,
                            listAdmin.toString(),
                            listMember.toString(),
                            "123"
                        )

                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ s ->
                                if (s != null) {
                                    if (s.get(0).response!! >= 1) {
                                        Toast.makeText(mContext, "Group Created", Toast.LENGTH_LONG).show()
//                                        startActivity(
//                                            Intent(
//                                                context, MainActivity
//                                                ::class.java
//                                            )
//                                        )
//                                        activity!!.finish()
                                        mView.ll_create_groups.visibility = View.VISIBLE
                                        mView.ll_create_groups_body.visibility = View.GONE
                                    } else {

                                        Toast.makeText(
                                            mContext,
                                            "Only Master Admin can create Group",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(mContext, "You cannot create group", Toast.LENGTH_LONG).show()
                                }
                            }, { throwable ->
                                //progress_chat.visibility = View.GONE
                                Log.i("Error:", throwable.message)
                            })
                    )

                }

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


        if(i.equals(0))
        {
            if (mLoginPreference.getStringData("masteradmin", "") != mLoginPreference.getStringData("id", "")) {

                var list: List<IndividualGrpNameModel.IndividualGroup>


                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.group_container, GrpNameFragmentFragment(), getString(R.string.master_admin_frag))
                    .commit()
            }
        }
    }

    private fun companyspinner() {
        if (!Common.isConnectedMobile(requireContext())) {
            Toast.makeText(requireContext(), "No Internet Connection", Toast.LENGTH_LONG).show()
            return
        }
        val searchListcompany = mutableMapOf<String, String>()
        mCompanyList = mutableSetOf()
//        val searchHashList = mutableMapOf<String, String>()
//        val searchListcompany=mutableMapOf<String,String>()


        mService.getCompany()
            .enqueue(object : Callback<CompanyNameModal> {

                override fun onResponse(call: Call<CompanyNameModal>, response: Response<CompanyNameModal>) {
                    // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        if (response.body()?.companies?.isNotEmpty()!!) {

                        mCompanyList.add("Select Company")
                         searchListcompany["All"] = "All"
                        response.body()!!.companies!!.forEach {

                            mCompanyList.add(it.groupCompanies.toString())
                            searchListcompany[it.groupCompanies.toString()] = it.groupCompanies.toString()
                        }
                        sl(mView.sp_company, searchListcompany)
                        var company =    mLoginPreference.savStringeData("company","All")
                        mView.sp_company.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                if (position >= 0) {
                                    var tv_company = sp_company.getItemAtPosition(position)
                                    mLoginPreference.savStringeData("company",tv_company.toString())
                                    var c=mLoginPreference.getStringData("company","")!!

                                    Spinnerregion(c)
                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }
                        }
                    }else{
                        searchListcompany.clear()
                        sl(mView.sp_company, searchListcompany)
                        Spinnerregion("Empty")
                    }


                }

                override fun onFailure(call: Call<CompanyNameModal>, t: Throwable) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })


//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ s ->
//                  //  if (s.isNotEmpty()) {
//
//                        mCompanyList.add("Select Company")
//                 //       s.forEach { mCompanyList.add(it.Company().company.toString()) }
//                      //  s.forEach {
//                            mCompanyList.add(it.companies..toString())
//                           // searchHashList[it.name.toString().plus("- ").plus(it.id.toString())] = it.id.toString()
//                            searchListcompany[it.companies.toString()]
//                        }
//                    //    sp(mView.sp_add_members, searchHashList)
//                        sp(mView.sp_company, searchListcompany)
//
//
//
//                        mView.sp_company.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                                if (position > 0) {
//                                    val chip = Chip(requireContext())
//
//                                   // chip.text = mCompanyList[(view as TextView).text.toString()]
//
//                                    val layoutParams = LinearLayout.LayoutParams(
//                                        LinearLayout.LayoutParams.WRAP_CONTENT,
//                                        LinearLayout.LayoutParams.WRAP_CONTENT
//                                    )
//                                    layoutParams.setMargins(10, 20, 10, 10)
//                                    chip.setLayoutParams(layoutParams)
//                                    chip.isCloseIconVisible = true
//                                    chip.closeIcon =
//                                        ContextCompat.getDrawable(requireContext(), R.drawable.ic_close_black_24dp)
//                                    chip.chipIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_person)
//                                    chip.setTextColor(Color.WHITE)
//                                    chip.setChipIconTintResource(R.color.colorGreen)
//                                    chip.setOnCloseIconClickListener {
//                                        TransitionManager.beginDelayedTransition(viewgrp_member)
//                                        viewgrp_member.removeView(chip)
//                                    }
//                                    mView.viewgrp_member.addView(chip)
//
//
//                                }
//                            }
//
//                            override fun onNothingSelected(parent: AdapterView<*>?) {
//
//                            }
//                        }
//
//                    }
//
//                }, { throwable ->
//                    progress_chat.visibility = View.GONE
//                    Log.i("Error:", throwable.message)
//                })


    }

    private fun Spinnerregion(c:String) {
        if (!Common.isConnectedMobile(requireContext())) {
            Toast.makeText(requireContext(), "No Internet Connection", Toast.LENGTH_LONG).show()
            return
        }
        val searchListRegion = mutableMapOf<String, String>()
        mRegionList = mutableSetOf()
        mService.getRegion(c.toString())
            .enqueue(object : Callback<RegionNameModal> {
                override fun onFailure(call: Call<RegionNameModal>, t: Throwable) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onResponse(call: Call<RegionNameModal>, response: Response<RegionNameModal>) {
                    // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    if (response.body()?.office?.isNotEmpty()!!) {

                        mCompanyList.add("Select Company")
                        searchListRegion["All"] = "All"
                        response.body()!!.office!!.forEach {

                            mCompanyList.add(it.region.toString())
                            searchListRegion[it.region.toString()] = it.region.toString()
                        }
                        sl(mView.sp_Region, searchListRegion)
                        mLoginPreference.savStringeData("region","All")
                        mView.sp_Region.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                if (position >= 0) {

                                    var tv_Region = sp_Region.getItemAtPosition(position)
                                    mLoginPreference.savStringeData("region",tv_Region.toString())
                                    var c=mLoginPreference.getStringData("company","")!!
                                    var r=mLoginPreference.getStringData("region","")!!

                                    Spinnerstation(c,r)
                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }
                        }
                    }else{
                        mRegionList.clear()
                        searchListRegion.clear()
                        searchListRegion["no region"]="No region"
                        sl(mView.sp_Region, searchListRegion)
                        Spinnerstation(c,"empty")
                    }
                }
            })
    }

    private fun Spinnerstation(c: String,r: String) {
        if (!Common.isConnectedMobile(requireContext())) {
            Toast.makeText(requireContext(), "No Internet Connection", Toast.LENGTH_LONG).show()
            return
        }
        val searchListStation = mutableMapOf<String, String>()
        mStationList = mutableSetOf()
        mService.getStation(c.toString(),r.toString())
            .enqueue(object : Callback<StationNameModal> {
                override fun onFailure(call: Call<StationNameModal>, t: Throwable) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onResponse(call: Call<StationNameModal>, response: Response<StationNameModal>) {
                    // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    if (response.body()?.stations?.isNotEmpty()!!) {

                        mStationList.add("Select Company")
                        searchListStation["All"] = "All"
                        response.body()!!.stations!!.forEach {

                            mCompanyList.add(it.station.toString())
                            searchListStation[it.station.toString()] = it.station.toString()
                        }
                        sl(mView.sp_Station, searchListStation)
                        mLoginPreference.savStringeData("station","All")
                        mView.sp_Station.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                if (position >= 0) {
                                    var tv_Station = sp_Station.getItemAtPosition(position)
                                    mLoginPreference.savStringeData("station",tv_Station.toString())
                                    var c=mLoginPreference.getStringData("company","")!!
                                    var r=mLoginPreference.getStringData("region","")!!
                                    var s=mLoginPreference.getStringData("station","")!!

                                    SpinnerStatus(c, r,s)

                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }
                        }
                    }else{
                        mStationList.clear()
                        searchListStation.clear()
                        searchListStation["no station"]="No station"
                        sl(mView.sp_Station, searchListStation)
                        SpinnerStatus(c, r,"empty")
                    }
                }
            })

    }

    private fun SpinnerStatus(c: String, r: String, s: String) {

        if (!Common.isConnectedMobile(requireContext())) {
            Toast.makeText(requireContext(), "No Internet Connection", Toast.LENGTH_LONG).show()
            return
        }
        val searchListStatus = mutableMapOf<String, String>()
        mStatusList = mutableSetOf()
        mService.getEmpStatus(c.toString(),r.toString(),s.toString())
            .enqueue(object : Callback<EmpStatusNameModal> {
                override fun onFailure(call: Call<EmpStatusNameModal>, t: Throwable) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onResponse(call: Call<EmpStatusNameModal>, response: Response<EmpStatusNameModal>) {
                    // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    if (response.body()?.empstatuss?.isNotEmpty()!!) {

                        mStatusList.add("Select Company")
                        searchListStatus["All"] = "All"
                        response.body()!!.empstatuss!!.forEach {

                            mStatusList.add(it.empStatus.toString())
                            searchListStatus[it.empStatus.toString()] = it.empStatus.toString()
                        }
                        sl(mView.sp_Status, searchListStatus)
                        mLoginPreference.savStringeData("status","All")
                        mView.sp_Status.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                if (position >= 0) {
                                    var tv_Status = sp_Status.getItemAtPosition(position)
                                    mLoginPreference.savStringeData("status",tv_Status.toString())
                                    var c=mLoginPreference.getStringData("company","")!!
                                    var r=mLoginPreference.getStringData("region","")!!
                                    var s=mLoginPreference.getStringData("station","")!!
                                    var st=mLoginPreference.getStringData("status","")!!

                                    SpinnerDepartment(c, r,s,st)

                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }
                        }
                    }else{
                        mStatusList.clear()
                        searchListStatus.clear()
                        searchListStatus["No status"]="No status available"
                        sl(mView.sp_Status, searchListStatus)
                        SpinnerDepartment(c, r,s,"empty")
                    }
                }
            })
    }

    private fun SpinnerDepartment(c: String, r: String, s: String, st: String) {

        if (!Common.isConnectedMobile(requireContext())) {
            Toast.makeText(requireContext(), "No Internet Connection", Toast.LENGTH_LONG).show()
            return
        }
        val searchListDepartment = mutableMapOf<String, String>()
        mDepartmentList = mutableSetOf()
        mService.getDepartment(c.toString(),r.toString(),s.toString(),st.toString())
            .enqueue(object : Callback<DepartmentNameModal> {
                override fun onFailure(call: Call<DepartmentNameModal>, t: Throwable) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onResponse(call: Call<DepartmentNameModal>, response: Response<DepartmentNameModal>) {
                    // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    if (response.body()?.department?.isNotEmpty()!!) {

                        mDepartmentList.add("Select Company")
                        searchListDepartment["All"] = "All"
                        response.body()!!.department!!.forEach {

                            mDepartmentList.add(it.departments.toString())
                            searchListDepartment[it.departments.toString()] = it.departments.toString()
                        }
                        sl(mView.sp_Department, searchListDepartment)
                        mLoginPreference.savStringeData("department","All")
                        mView.sp_Department.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                if (position >= 0) {
                                    var tv_Department = sp_Department.getItemAtPosition(position)
                                    mLoginPreference.savStringeData("department",tv_Department.toString())
                                    var c=mLoginPreference.getStringData("company","")!!
                                    var r=mLoginPreference.getStringData("region","")!!
                                    var s=mLoginPreference.getStringData("station","")!!
                                    var st=mLoginPreference.getStringData("status","")!!
                                    var d=mLoginPreference.getStringData("department","")!!
                                    initSpinner2(c, r,s,st,d)

                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }
                        }
                    }else{
                        mDepartmentList.clear()
                        searchListDepartment.clear()
                        searchListDepartment["no department available"]="No department"
                        sl(mView.sp_Department, searchListDepartment)
                        initSpinner2(c, r,s,st,"empty")
                    }
                }
            })
    }

    private fun initSpinner2(c: String, r: String, s: String, st:String, d: String) {

        if (!Common.isConnectedMobile(requireContext())) {
            Toast.makeText(requireContext(), "No Internet Connection", Toast.LENGTH_LONG).show()
            return
        }
        val searchListMember = mutableMapOf<String, String>()
        mMemberList = mutableSetOf()

        mService.getEmpname(c,r,s,st,d)
            .enqueue(object : Callback<EmpNameModal> {
                override fun onFailure(call: Call<EmpNameModal>, t: Throwable) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onResponse(call: Call<EmpNameModal>, response: Response<EmpNameModal>) {
                    // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    if (response.body()?.empname?.isNotEmpty()!!) {

                        searchListMember["select"]="select"
                        searchListMember["All"] = "All"
                        response.body()!!.empname!!.forEach {

                            mMemberList.add(it.empCode.toString())
                            searchListMember[it.empName.toString().plus(it.empCode.toString())] = it.empCode.toString()
                        }
                        sl(mView.sp_add_members, searchListMember)
                        mView.sp_add_members.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                mView.sp_add_members.visibility=View.VISIBLE
                                if (position == 0){
                                    mView.btn_create_grp.visibility=View.INVISIBLE
                                }
                                if (position > 0) {

                                  mView.btn_create_grp.visibility=View.VISIBLE

                                    val chip = Chip(requireContext())

                                    chip.text = searchListMember[(view as TextView).text.toString()]
                                    if(chip.text=="All")
                                    { mView.sp_add_members.visibility=View.INVISIBLE
                                        mView.viewgrp_member.removeAllViews()
                                    }

                                    val layoutParams = LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
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
                                        if(chip.text=="All")
                                        {
                                            mView.sp_add_members.visibility=View.VISIBLE

                                        }

                                        TransitionManager.beginDelayedTransition(viewgrp_member)
                                        viewgrp_member.removeView(chip)
                                    }
                                    mView.viewgrp_member.addView(chip)




                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }
                        }
                    }else{
                        mMemberList.clear()
                        searchListMember.clear()
                        searchListMember["no member"]="no member"
                        sl(mView.sp_add_members, searchListMember)
                    }
                }
            })
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
                            searchHashList["select"]="select"
                        s.forEach {
                            mAdminList.add(it.id.toString())
                            searchHashList[it.name.toString().plus("- ").plus(it.id.toString())] = it.id.toString()
                        }
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
                                    chip.layoutParams = layoutParams

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

                    }

                }, { throwable ->
                    progress_chat.visibility = View.GONE
                    Log.i("Error:", throwable.message)
                })
        )


    }

    private fun sl(sl: SearchableSpinner, searchListcompany: MutableMap<String, String>) {

        val roAdapter =
            ArrayAdapter<String>(
                mContext,
                android.R.layout.simple_spinner_dropdown_item,
                searchListcompany.keys.toList()
            )

        sl.adapter = roAdapter

    }

    private fun sp(sp: SearchableSpinner, searchHashList: MutableMap<String, String>) {

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



