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


class ViewInspectionDetailsAdapter(internal var context: Context, internal var array: List<DtGetViewInspectionDetail>) :
    RecyclerView.Adapter<ViewInspectionDetailsAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewInspectionDetailsAdapter.Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_view_inspection_details, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: ViewInspectionDetailsAdapter.Holder, position: Int) {
        holder.date.text = array[position].empDt
        holder.type.text = array[position].typeOl
        holder.client_name.text = array[position].olName
        holder.vandor.text = array[position].olRegion
        holder.po_number.text = array[position].olCustID
        holder.range_tv.text = array[position].rangeSts
        holder.expenses_tv.text = array[position].expenses
        holder.report_no.text = array[position].reportNo
        holder.quantity.text = array[position].quantity
        holder.invoice_type.text = array[position].invoicetype





    }

    override fun getItemCount(): Int {
        return array.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var date: TextView
        internal var type: TextView
        internal var client_name: TextView
        internal var vandor: TextView
        internal var po_number: TextView
        internal var range_tv: TextView
        internal var expenses_tv: TextView
        internal var report_no: TextView
        internal var quantity: TextView
        internal var invoice_type: TextView


        init {
            date = itemView!!.findViewById(R.id.tv_date_ins)
            type = itemView!!.findViewById(R.id.tv_typ_ins)
            client_name = itemView!!.findViewById(R.id.tv_clientname_ins)
            vandor = itemView!!.findViewById(R.id.tv_vandor_ins)
            po_number = itemView!!.findViewById(R.id.tv_po_number_ins)
            range_tv = itemView!!.findViewById(R.id.tv_range_ins)
            expenses_tv = itemView!!.findViewById(R.id.tv_expenses_ins)
            report_no = itemView!!.findViewById(R.id.tv_report_no_ins)
            quantity = itemView!!.findViewById(R.id.tv_quantity_ins)
            invoice_type = itemView!!.findViewById(R.id.tv_invoice_ins)


        }
    }
}
