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


class ViewExpensesDetailsAdapter(internal var context: Context, internal var array: List<DtGetViewExpensesDetails>) :
    RecyclerView.Adapter<ViewExpensesDetailsAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewExpensesDetailsAdapter.Holder {
        val view = LayoutInflater.from(context).inflate(org.icspl.icsconnect.R.layout.row_view_expense_details, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: ViewExpensesDetailsAdapter.Holder, position: Int) {
        holder.date.text = array[position].empDt
        holder.total_amount.text = array[position].totalamt
        holder.narration.text = array[position].perExpns
        holder.status.text = array[position].status
        holder.remark.text = array[position].remarks
        holder.filename.text = array[position].filename
        holder.tranfar_remark.text = array[position].transferRemark
      if ((array[position].remarks)!=null)
      {
          var lines= array[position].remarks!!.lines().count()
          if(lines>3)
          {
              val next = "<font color='#EE0000'>...Read more</font>"
              holder.remark.text=(Html.fromHtml(array[position].remarks +next))
          }
          else
          {
              holder.remark.text = array[position].remarks
          }
      }

        holder.remark.setOnClickListener(View.OnClickListener {
            var line=array[position].remarks!!.lines().count()
            if(line>3)
            {
                var alertDialog : LottieAlertDialog = LottieAlertDialog.Builder(context, DialogTypes.TYPE_CUSTOM,"animation.json")
                    .setDescription(array[position].remarks!!.toString())
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
        internal var total_amount: TextView
        internal var narration: TextView
        internal var status: TextView
        internal var remark: TextView
        internal var filename: TextView
        internal var tranfar_remark: TextView


        init {
            date = itemView!!.findViewById(R.id.tv_date_expense)
            total_amount = itemView!!.findViewById(R.id.tv_total_amount)
            narration = itemView!!.findViewById(R.id.tv_narration)
            status = itemView!!.findViewById(R.id.tv_status_expenses)
            remark = itemView!!.findViewById(R.id.tv_remark_expenses)
            filename = itemView!!.findViewById(R.id.tv_filename_expenses)
            tranfar_remark = itemView!!.findViewById(R.id.trasfer_remark_expenses)


        }
    }
}
