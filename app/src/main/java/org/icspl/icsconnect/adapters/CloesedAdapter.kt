package org.icspl.icsconnect.adapters

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.icspl.icsconnect.R
import org.icspl.icsconnect.models.CloseMessage

/**
 * Created by Suraj on 2/14/2018.
 */

public class CloesedAdapter(
    private val mList: ArrayList<CloseMessage.ClosedMSGDetails>
) : androidx.recyclerview.widget.RecyclerView.Adapter<CloesedAdapter.ViewHolder>() {

    inner class ViewHolder internal constructor(view: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        public var c_qid: TextView
        public var c_open_date: TextView
        public var c_close_date: TextView
        public var c_remarks: TextView
        public var qbyid: TextView




        init {
            c_qid = view.findViewById(R.id.c_id)
            c_close_date = view.findViewById(R.id.c_close_date)
            c_remarks = view.findViewById(R.id.c_remarks)
            c_open_date = view.findViewById(R.id.c_open_date)
            qbyid=view.findViewById(R.id.qbyid)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_closed_query, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model = mList[holder.adapterPosition]
        var opendate=model.sendfrm
        var closedate=model.closedate
        if(!opendate.equals(null))
        {
             opendate=opendate!!.replace("T","/")
        }
        if(!closedate.equals(null))
        {
             closedate=closedate!!.replace("T","/")
        }

        holder.c_qid.text = model.queryid
        holder.c_close_date.text = closedate
        holder.c_open_date.text = opendate

        holder.c_remarks.text = model.remarks
        holder.qbyid.text=model.toemp
    }

    companion object {
        val TAG = this::class.java.simpleName
    }

}