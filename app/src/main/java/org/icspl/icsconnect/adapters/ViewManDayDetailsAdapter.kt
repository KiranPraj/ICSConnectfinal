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
import org.icspl.icsconnect.models.DtGetViewExpensesDetails
import org.icspl.icsconnect.models.DtGetViewInspectionDetail
import org.icspl.icsconnect.models.DtGetViewMandayDetail


class ViewManDayDetailsAdapter(internal var context: Context, internal var array: List<DtGetViewMandayDetail>) :
    RecyclerView.Adapter<ViewManDayDetailsAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewManDayDetailsAdapter.Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_view_manday_details, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: ViewManDayDetailsAdapter.Holder, position: Int) {
        holder.date.text = array[position].cFRDate
        holder.type.text = array[position].typeofactivity
        holder.client.text = array[position].clientName
        holder.reg_no.text = array[position].regNo
        holder.cert_type.text = array[position].tOC
        holder.market_status.text = array[position].mrktIncStatus
        holder.marketer_name.text = array[position].mrktrName
        holder.manday.text = array[position].manday





    }

    override fun getItemCount(): Int {
        return array.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var date: TextView
        internal var client: TextView
        internal var reg_no: TextView
        internal var cert_type: TextView
        internal var type: TextView
        internal var market_status: TextView
        internal var marketer_name: TextView
        internal var manday: TextView



        init {
            date = itemView!!.findViewById(R.id.tv_date_man)
            type = itemView!!.findViewById(R.id.tv_typ_man)
            client = itemView!!.findViewById(R.id.tv_client_man)
            reg_no = itemView!!.findViewById(R.id.tv_reg_man)
            cert_type = itemView!!.findViewById(R.id.tv_cert_typ_man)
            market_status = itemView!!.findViewById(R.id.tv_market_status_man)
            marketer_name = itemView!!.findViewById(R.id.tv_marketer_name_man)
            manday = itemView!!.findViewById(R.id.tv_manday_man)



        }
    }
}
