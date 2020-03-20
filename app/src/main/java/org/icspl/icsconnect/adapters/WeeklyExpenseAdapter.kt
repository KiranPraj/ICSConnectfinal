package org.icspl.icsconnect.adapters

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.labters.lottiealertdialoglibrary.ClickListener
import com.labters.lottiealertdialoglibrary.DialogTypes
import com.labters.lottiealertdialoglibrary.LottieAlertDialog
import kotlinx.android.synthetic.main.activity_time_sheet.*
import kotlinx.android.synthetic.main.fragment_expenses_home.view.*
import org.icspl.icsconnect.R
import org.icspl.icsconnect.models.DtGetViewTimesheetstatus
import org.icspl.icsconnect.models.WeeklyExpenseModel
import java.util.*
import kotlin.collections.ArrayList


class WeeklyExpenseAdapter(internal var context: Context, internal var array: ArrayList<WeeklyExpenseModel>, private val mCallback: ITopicClickListener) :
    RecyclerView.Adapter<WeeklyExpenseAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyExpenseAdapter.Holder {
        val view = LayoutInflater.from(context).inflate(org.icspl.icsconnect.R.layout.weekly_expenses_layout, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: WeeklyExpenseAdapter.Holder, position: Int)
    {

        holder.selectdate.setOnClickListener {
            val cldr = Calendar.getInstance()
            val day = cldr.get(Calendar.DAY_OF_MONTH)
            val month = cldr.get(Calendar.MONTH)
            val year = cldr.get(Calendar.YEAR)

            holder.picker = DatePickerDialog(context,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    holder.selectdate.text = (monthOfYear + 1).toString() + "-" + dayOfMonth + "-" + year
                }, year, month, day
            )
            holder.picker.datePicker.maxDate = cldr.timeInMillis
            holder.picker.show()
            var calenderdate=holder.selectdate.text.toString()

        }
        holder.browse_file.setOnClickListener {
            mCallback.photoCallback(holder.browse_file)
        }


    }




    override fun getItemCount(): Int {
        return array.size
    }




    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var selectdate: TextView
        internal var particular_expenses: EditText
        internal var total_amount: EditText
        internal var igst: EditText
        internal var cgst: EditText
        internal var sgst: EditText
        internal var gst_no: EditText
        internal var service_provider: EditText
       // internal var paid_by: EditText
        internal var inv_amount: EditText
        internal var client_no: EditText
        internal var browse_file: Button
        internal lateinit var picker: DatePickerDialog
        init {
            selectdate = itemView!!.findViewById(R.id.edit_date)
            particular_expenses = itemView!!.findViewById(R.id.particular_expenses)
            total_amount = itemView!!.findViewById(R.id.total_amount)
            igst = itemView!!.findViewById(R.id.igst)
            cgst = itemView!!.findViewById(R.id.cgst)
            sgst = itemView!!.findViewById(R.id.sgst)
            gst_no = itemView!!.findViewById(R.id.gst_no)
            service_provider = itemView!!.findViewById(R.id.service_provider)
            inv_amount = itemView!!.findViewById(R.id.inv_amount)
            browse_file = itemView!!.findViewById(R.id.chose_file)
            client_no=itemView.findViewById(R.id.client_no)
          //  paid_by=itemView.findViewById(R.id.paid_by)


        }
    }
}

interface ITopicClickListener {
    fun photoCallback(btnPhoto: Button)
}
