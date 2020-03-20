package org.icspl.icsconnect.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.labters.lottiealertdialoglibrary.DialogTypes
import com.labters.lottiealertdialoglibrary.LottieAlertDialog
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_time_sheet.*
import kotlinx.android.synthetic.main.activity_view_timesheet.*
import kotlinx.android.synthetic.main.leave_status_frag.*
import kotlinx.android.synthetic.main.leave_status_frag.view.*
import org.icspl.icsconnect.R
import org.icspl.icsconnect.models.CompOffstatus
import org.icspl.icsconnect.models.GetLeaveStatusDetails
import org.icspl.icsconnect.models.GetleaveYear
import org.icspl.icsconnect.preferences.LoginPreference
import org.icspl.icsconnect.utils.Common
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class Leave_Status: androidx.fragment.app.Fragment()
{
    private lateinit var mContext: Context
    private lateinit var mView: View
    private val mLoginPreference by lazy { LoginPreference.getInstance(mContext) }

    private val mService by lazy { Common.getAPI() }
    val years:MutableList<String> =ArrayList()
    lateinit var select_year:String
    companion object {

        fun newInstance(): Leave_Status {
            return Leave_Status()
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mView= inflater.inflate(R.layout.leave_status_frag, container, false)
       initview()
        return mView
    }

    private fun initview()
    {
        mContext= mView!!.context

       /* mView.name.text=mLoginPreference.getStringData("name","")
        mView.emp_code.text=mLoginPreference.getStringData("id","")
        mView.station.text=mLoginPreference.getStringData("station","")*/
      //  var get_year=leave_year.pfinYear
         getleave_year()



    /* val currentyear=Calendar.getInstance().get(Calendar.YEAR)
        years.add("SELECT")
        for (i in currentyear downTo currentyear-10)
        {
            years.add(i.toString())
        }*/

    }

    private fun getLeaveDetails() {
        spinner_avail_year.onItemSelectedListener=object :AdapterView.OnItemSelectedListener
        {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                select_year=mView.spinner_avail_year.selectedItem.toString()
                mService!!.get_leave_status_details(mLoginPreference.getStringData("id","").toString(),select_year)
                    .enqueue(object : Callback<List<GetLeaveStatusDetails>> {
                        override fun onFailure(call: Call<List<GetLeaveStatusDetails>>, t: Throwable) {
                            Toast.makeText(mContext,"hhhh",Toast.LENGTH_LONG).show()
                        }

                        override fun onResponse(call: Call<List<GetLeaveStatusDetails>>, response: Response<List<GetLeaveStatusDetails>>) {

                            if(response.isSuccessful)
                            {
                                if(response.body()!!.size>0)
                                {
                                    for (i in 1..response.body()!!.size)
                                    {
                                        cl.text= response.body()!!.get(i-1).cL
                                        sl.text=response.body()!!.get(i-1).sL
                                        comp_off.text=response.body()!!.get(i-1).compoff
                                        al.text=response.body()!!.get(i-1).aL
                                     //   pl.text=response.body()!!.get(i-1).pL

                                    }
                                }
                                else
                                {
                                    Toasty.error(mContext,"You don't have any leave",Toast.LENGTH_LONG).show()
                                }

//                       mView.cl.text= response.body()!!.get(0).cL
                            }
                        }
                    })



        }
    }




    }

    private fun getleave_year() {


        mService!!.get_leave_year(mLoginPreference.getStringData("id","").toString())
            .enqueue(object : Callback<List<GetleaveYear>> {
                override fun onFailure(call: Call<List<GetleaveYear>>, t: Throwable) {
                    Toast.makeText(mContext,"hhhh",Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<List<GetleaveYear>>, response: Response<List<GetleaveYear>>) {

                    if(response.isSuccessful)
                    {
                        for (i in 1..response.body()!!.size)
                        {
                            years.add(response.body()!!.get(i-1).pfinYear.toString())

                        }
                        val adapter =  ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, years)
                        mView.spinner_avail_year.adapter = adapter
                        adapter.notifyDataSetChanged()
                        getLeaveDetails()
                    }
                }
            })

    }

}