package org.icspl.icsconnect.adapters

import org.icspl.icsconnect.models.DtGetViewPunchDatastatus

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.icspl.icsconnect.R
import org.icspl.icsconnect.activity.ViewTimesheet
import org.icspl.icsconnect.models.DtGetViewTimesheetstatus

class ViewRealTimePunchDataAdaptor(internal var context: Context, internal var array: List<DtGetViewPunchDatastatus>) :
    RecyclerView.Adapter<ViewRealTimePunchDataAdaptor.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewRealTimePunchDataAdaptor.Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_real_time_punch_data, parent, false)
        return Holder(view)
    }
    override fun onBindViewHolder(holder: ViewRealTimePunchDataAdaptor.Holder, position: Int) {
        holder.tv_date_rt.text= array[position].date
        holder.tv_day_rt.text= array[position].eDay
        holder.tv_intime.text= array[position].inTime
        holder.tv_outtime.text= array[position].outTime
        holder.tv_workinghr_rt.text= array[position].workhours
    }
    override fun getItemCount(): Int {
        return array.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var tv_date_rt: TextView
        internal var tv_day_rt: TextView
        internal var tv_intime: TextView
        internal var tv_outtime: TextView
        internal var tv_workinghr_rt: TextView

        init {
            tv_date_rt = itemView!!.findViewById(R.id.tv_date_rt)
            tv_day_rt = itemView!!.findViewById(R.id.tv_day_rt)
            tv_intime = itemView!!.findViewById(R.id.tv_intime)
            tv_outtime = itemView!!.findViewById(R.id.tv_outtime)
            tv_workinghr_rt = itemView!!.findViewById(R.id.tv_workinghr_rt)

        }
    }

}
