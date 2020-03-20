package org.icspl.icsconnect.adapters

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.labters.lottiealertdialoglibrary.ClickListener
import com.labters.lottiealertdialoglibrary.DialogTypes
import com.labters.lottiealertdialoglibrary.LottieAlertDialog
import org.icspl.icsconnect.R
import org.icspl.icsconnect.models.DtGetViewTimesheetstatus
import org.icspl.icsconnect.models.GetLeaveDetails

class LeaveDetailsAdapter(internal var context: Context, internal var array: List<GetLeaveDetails>) :
    RecyclerView.Adapter<LeaveDetailsAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaveDetailsAdapter.Holder {
        val view = LayoutInflater.from(context).inflate(org.icspl.icsconnect.R.layout.row_view_leave_details, parent, false)
        return Holder(view)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: LeaveDetailsAdapter.Holder, position: Int) {
        holder.leave_type.text = array[position].leaveType
        var from_datee= array[position].fromDate
       var to_datee= array[position].toDate
        val date_apply=array[position].dateOfApply
        holder.days.text = array[position].days
        holder.status.text = array[position].status
        holder.approver.text = array[position].approver
        holder.compagainst.text = array[position].compagainst
        holder.halfdayagainst.text=array[position].halfdayagainst
        var reason_lines=array[position].reason!!.chars().count()
        if (reason_lines>15)
        {
            val read_more = "<font color='#EE0000'>...Read more</font>"
            holder.reason.text=(Html.fromHtml(array[position].reason!!.substring(0,14) +read_more))
        }
       else
        {
            holder.reason.text = array[position].reason
        }
        holder.reason.setOnClickListener(View.OnClickListener {
            var line=array[position].reason!!.chars().count()
            if(line>15)
            {
                var alertDialog : LottieAlertDialog = LottieAlertDialog.Builder(context, DialogTypes.TYPE_CUSTOM,"animation.json")
                    .setDescription(array[position].reason!!.toString())
                    .setPositiveButtonColor(Color.MAGENTA)
                    .setPositiveTextColor(Color.WHITE)
                    .setPositiveText("Ok")
                    .setPositiveListener(object : ClickListener {
                        override fun onClick(dialog: LottieAlertDialog) {
                            dialog.dismiss()

                        }

                    })
                    .build()
                alertDialog.setCancelable(false)
                alertDialog.show()

            }
        })
        if (from_datee!=null)
        {
            holder.from_date.text =from_datee.substring(0,10)
        }
        else
        {
            holder.from_date.text="-"
        }
        if (to_datee!=null)
        {
            holder.to_date.text = to_datee.substring(0,10)
        }
        else
        {
            holder.to_date.text="-"
        }
        if (date_apply!=null)
        {
            holder.date_of_apply.text=date_apply.substring(0,10)
        }
        else
        {
            holder.date_of_apply.text="-"
        }


    }

    override fun getItemCount(): Int {
        return array.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var leave_type: TextView
        internal var from_date: TextView
        internal var to_date: TextView
        internal var days: TextView
        internal var date_of_apply: TextView
        internal var reason: TextView
        internal var status: TextView
        internal var approver: TextView
        internal var compagainst: TextView
        internal var halfdayagainst: TextView

        init {
            leave_type = itemView!!.findViewById(R.id.leave_type)
            from_date = itemView!!.findViewById(R.id.from_date)
            to_date = itemView!!.findViewById(R.id.to_date)
            days = itemView!!.findViewById(R.id.days)
            date_of_apply = itemView!!.findViewById(R.id.date_of_apply)
            reason = itemView!!.findViewById(R.id.reason)
            status = itemView!!.findViewById(R.id.status)
            approver = itemView!!.findViewById(R.id.approver)
            compagainst = itemView!!.findViewById(R.id.compagainst)
            halfdayagainst = itemView!!.findViewById(R.id.halfday_against)

        }
    }
}
