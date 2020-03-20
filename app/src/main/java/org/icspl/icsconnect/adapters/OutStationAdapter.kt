package org.icspl.icsconnect.adapters

import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import org.icspl.icsconnect.R
import org.icspl.icsconnect.models.OutStationExpensesModel
import org.icspl.icsconnect.models.WeeklyExpenseModel

class OutStationAdapter(internal var context: Context, internal var array: ArrayList<OutStationExpensesModel>,private val mCallback: ITopicClickListener) :
    RecyclerView.Adapter<OutStationAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutStationAdapter.Holder {
        val view = LayoutInflater.from(context).inflate(org.icspl.icsconnect.R.layout.raw_outstation_expenses, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: OutStationAdapter.Holder, position: Int) {
            if(array.get(position).location_boarding.toString()=="y")
            {
                holder.dateView.visibility=View.GONE
                holder.textforoutstation.text="Boarding and Lodging/Communication(Phone,Fax,Email)/Other Business Expenses"
            }
        else
            {
                holder.locaationView.visibility=View.GONE
                holder.textforoutstation.text="Fare(From-To)/Local Travel"
            }
        holder.browse_file.setOnClickListener {
            mCallback.photoCallback(holder.browse_file)
        }
    }

    override fun getItemCount(): Int {
        return array.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
      /*  internal var fromdate: EditText
        internal var todate: EditText
        internal var expenses_local: EditText
        internal var igst_local: EditText
        internal var cgst_local: EditText
        internal var sgst_local: EditText
        internal var gst_no_local: EditText
        internal var service_provider_local: EditText
        internal var paid_by_local: EditText
        internal var invoiceable_local: EditText
        internal var narration_local: EditText
        */
        internal var fromdate: EditText
        internal var todate: EditText
        internal var location_boarding: EditText
        internal var expenses_boarding: EditText
        internal var igst_boarding: EditText
        internal var cgst_boarding: EditText
        internal var sgst_boarding: EditText
        internal var gst_no_boarding: EditText
        internal var service_provider_boarding: EditText
       // internal var paid_by_boarding: EditText
       // internal var invoiceable_boarding: EditText
        internal var narration_boarding: EditText
        internal var browse_file_boarding: TextView
        internal var dateView: LinearLayoutCompat
        internal var locaationView: LinearLayoutCompat
        internal var textforoutstation:TextView
        internal var browse_file: Button



        init {
            fromdate = itemView!!.findViewById(R.id.from_localtravel)
            todate = itemView!!.findViewById(R.id.to_localtravel)
            location_boarding = itemView!!.findViewById(R.id.location)
            expenses_boarding = itemView!!.findViewById(R.id.expenses_boarding)
            igst_boarding=itemView.findViewById(R.id.igst_boarding)
            cgst_boarding = itemView!!.findViewById(R.id.boarding_cgst)
            sgst_boarding = itemView!!.findViewById(R.id._boarding_sgst)
            gst_no_boarding = itemView!!.findViewById(R.id.boarding_gst_no)
            service_provider_boarding = itemView!!.findViewById(R.id.boarding_service_provider)
          //  paid_by_boarding = itemView!!.findViewById(R.id.paid_by_boarding)
          //  invoiceable_boarding = itemView!!.findViewById(R.id.invoice_boarding)
            narration_boarding = itemView!!.findViewById(R.id.narrration_boarding)
            browse_file_boarding=itemView.findViewById(R.id.chose_file_boarding)
            dateView=itemView.findViewById(R.id.date)
            locaationView=itemView.findViewById(R.id.locationView)
            textforoutstation=itemView.findViewById(R.id.textforoutstation)
            browse_file=itemView.findViewById(R.id.chose_file_boarding)
        }
    }
}
