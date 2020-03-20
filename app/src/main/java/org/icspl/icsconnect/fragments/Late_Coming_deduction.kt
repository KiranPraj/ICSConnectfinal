package org.icspl.icsconnect.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.leave_details_frag.view.*
import org.icspl.icsconnect.R
import org.icspl.icsconnect.adapters.LeaveAdaptor
import java.util.*
import kotlin.collections.ArrayList

class Late_Coming_deduction:Fragment()
{
    private lateinit var mView:View
    private lateinit var mContext:Context
    companion object
    {
        fun newInstance():Late_Coming_deduction
        {
            return Late_Coming_deduction()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView=inflater.inflate(R.layout.late_coming_deduction,container,false)
        initview(mView)
        return mView
    }

    private fun initview(mView: View?) {
        mContext=context as Activity
        val years:MutableList<String> = ArrayList()
        val year=Calendar.getInstance().get(Calendar.YEAR)
        years.add("SELCET YEAR".toString())
        for (i in year downTo year-10)
        {
            years.add(i.toString())
        }

      val adaptor=ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item,years)
        mView!!.sp_year.adapter=adaptor
         adaptor.notifyDataSetChanged()
        mView.btn_submit.setOnClickListener(View.OnClickListener {
            /*if(mView.sp_month.selectedItem.toString().equals("SELECT MONTH"))
            {
                Toasty.error(mContext,"Please Select Month").show()
                return@OnClickListener
            }
            if(mView.sp_year.selectedItem.toString().equals("SELECT YEAR"))
            {
                Toasty.error(mContext,"Please Select Year").show()
                return@OnClickListener
            }*/
            //  loadtimesheet(sp_year.selectedItem.toString(),sp_month.selectedItemPosition.toString())
        })

    }
}