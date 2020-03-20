package org.icspl.icsconnect.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.labters.lottiealertdialoglibrary.ClickListener
import com.labters.lottiealertdialoglibrary.DialogTypes
import com.labters.lottiealertdialoglibrary.LottieAlertDialog
import org.icspl.icsconnect.R
import org.icspl.icsconnect.models.DtGetViewTimesheetstatus
import android.text.Html




class ViewTimesheetAdaptor(internal var context: Context, internal var array: List<DtGetViewTimesheetstatus>) :
    RecyclerView.Adapter<ViewTimesheetAdaptor.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewTimesheetAdaptor.Holder {
        val view = LayoutInflater.from(context).inflate(org.icspl.icsconnect.R.layout.row_viewtimesheet, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: ViewTimesheetAdaptor.Holder, position: Int) {
        holder.date.text = array[position].dt
        holder.dh.text = array[position].dh
        holder.extrahrs.text = array[position].eh
        holder.activity.text = array[position].activity
        holder.client.text = array[position].client
        holder.location.text = array[position].audiLoc
        holder.tada.text = array[position].oa
        holder.travelexp.text = array[position].travelCharge
        holder.manday.text = array[position].mandy
        var lines= array[position].rem!!.lines().count()
        if(lines>3)
        {
            val next = "<font color='#EE0000'>...Read more</font>"
            holder.remarks.text=(Html.fromHtml(array[position].rem +next))
        }
        else
        {
            holder.remarks.text = array[position].rem
        }
        holder.remarks.setOnClickListener(View.OnClickListener {
            var line=array[position].rem!!.lines().count()
            if(line>3)
            {
                var alertDialog : LottieAlertDialog = LottieAlertDialog.Builder(context, DialogTypes.TYPE_CUSTOM,"animation.json")
                    .setDescription(array[position].rem!!.toString())
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

    }

    override fun getItemCount(): Int {
        return array.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var date: TextView
        internal var dh: TextView
        internal var extrahrs: TextView
        internal var activity: TextView
        internal var client: TextView
        internal var location: TextView
        internal var tada: TextView
        internal var travelexp: TextView
        internal var manday: TextView
        internal var remarks: TextView

        init {
            date = itemView!!.findViewById(R.id.tv_date_vt)
            dh = itemView!!.findViewById(R.id.tv_dh)
            extrahrs = itemView!!.findViewById(R.id.tv_extraworkinghr)
            activity = itemView!!.findViewById(R.id.tv_actvity)
            client = itemView!!.findViewById(R.id.tv_client)
            location = itemView!!.findViewById(R.id.tv_location)
            tada = itemView!!.findViewById(R.id.tv_oa)
            travelexp = itemView!!.findViewById(R.id.tv_travelcharge)
            manday = itemView!!.findViewById(R.id.tv_manday)
            remarks = itemView!!.findViewById(R.id.tv_remark)

        }
    }
}
