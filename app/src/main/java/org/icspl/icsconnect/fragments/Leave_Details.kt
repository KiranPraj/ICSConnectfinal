package org.icspl.icsconnect.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_view_timesheet.*
import kotlinx.android.synthetic.main.leave_details_frag.view.*
import kotlinx.android.synthetic.main.leave_status_frag.view.*
import org.icspl.icsconnect.R
import org.icspl.icsconnect.adapters.LeaveDetailsAdapter
import org.icspl.icsconnect.models.GetLeaveDetails
import org.icspl.icsconnect.models.GetleaveYear
import org.icspl.icsconnect.preferences.LoginPreference
import org.icspl.icsconnect.utils.Common
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class Leave_Details: Fragment() {
    private lateinit var mContext: Context
    private lateinit var mView: View
    private val mLoginPreference by lazy { LoginPreference.getInstance(mContext) }

    private val mService by lazy { Common.getAPI() }
    private lateinit var leavedetailsList:MutableList<GetLeaveDetails>
    private lateinit var leaveDetailsAdapater:LeaveDetailsAdapter
    companion object
    {
        fun newInstance(): Leave_Details{
            return Leave_Details()
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mView= inflater.inflate(R.layout.leave_details_frag, container, false)
      initview(mView)
        return mView
    }

    private fun initview(mView: View) {
          mContext=context as Activity
        leavedetailsList=ArrayList()
        val years:MutableList<String> = ArrayList()
        val year = Calendar.getInstance().get(Calendar.YEAR)
        years.add("SELECT YEAR".toString())
        years.add("All")
        for ( i in year downTo year-10)
        {
            years.add(i.toString())  //simple_list_item_1
        }
        val adapter =  ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, years)
        mView.sp_year.adapter = adapter
        adapter.notifyDataSetChanged()
        mView.btn_submit.setOnClickListener(View.OnClickListener {

            if(mView.sp_year.selectedItem.toString().equals("SELECT YEAR"))
            {
                Toasty.error(mContext,"Please Select Year").show()
                return@OnClickListener
            }
            leavedetails()
          //  loadtimesheet(sp_year.selectedItem.toString(),sp_month.selectedItemPosition.toString())
        })
    }

    private fun leavedetails() {
        mService!!.get_leave_details(mLoginPreference.getStringData("id","").toString())
            .enqueue(object : Callback<List<GetLeaveDetails>> {
                override fun onFailure(call: Call<List<GetLeaveDetails>>, t: Throwable) {
                    Toast.makeText(mContext,"Network error please try again", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<List<GetLeaveDetails>>, response: Response<List<GetLeaveDetails>>) {

                    if(response.isSuccessful&& response.body()!!.size>0)
                    {
                       leavedetailsList=response.body() as MutableList<GetLeaveDetails>
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
                 leaveDetailsAdapater=
                         LeaveDetailsAdapter(mContext,leavedetailsList)
                        val layoutManager = LinearLayoutManager(mContext)
                        rv_viewtimesheet.setLayoutManager(layoutManager)
                        rv_viewtimesheet.setAdapter(leaveDetailsAdapater)
                    }
                }
            })

    }
}