package org.icspl.icsconnect.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.icspl.icsconnect.R
import org.icspl.icsconnect.models.CountMSGDetailsModel

/**
 * Created by Suraj on 2/14/2018.
 */

class CountDetailsAdapter(
    private val mList: List<CountMSGDetailsModel.IndividualDetail>,
    private val mContext: Context,
    var mCallback: CounterListener
) : androidx.recyclerview.widget.RecyclerView.Adapter<CountDetailsAdapter.ViewHolder>() {

    interface CounterListener {
        fun clickListenere(id: String, photo: String?, toemp: String?)
    }

    inner class ViewHolder internal constructor(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        public var tv_user_name: TextView
        public var tv_count_msg: TextView
        public var tv_last_chat: TextView
        public var iv_user_photo: ImageView
        public var tv_emp_id: TextView


        init {
            tv_user_name = view.findViewById(R.id.tv_user_name)
            tv_last_chat = view.findViewById(R.id.tv_last_chat)
            iv_user_photo = view.findViewById(R.id.iv_user_photo)
            tv_count_msg = view.findViewById(R.id.tv_count_msg)
            tv_emp_id = view.findViewById(R.id.tv_emp_id)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_count_query, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model = mList[holder.adapterPosition]
        holder.tv_count_msg.visibility = View.GONE

        holder.tv_user_name.text = model.fromEmpName
        holder.tv_emp_id.text = model.fromemp
        holder.tv_last_chat.text = model.remarks?.take(36)?.replace("\n", "")
        holder.tv_emp_id.tag = model.queryid
        // holder.tv_count_msg.text = model.fromemp

        Picasso.get().load("http://icspl.org/data/Employeephoto/" + model.photoPath)
            .placeholder(R.drawable.ic_user)
            .error(R.drawable.ic_user)
            .into(holder.iv_user_photo)

        holder.itemView.setOnClickListener {
            mCallback.clickListenere(holder.tv_emp_id.tag.toString(), model.photoPath, holder.tv_emp_id.text as String?)
        }
    }

    companion object {
        val TAG = this::class.java.simpleName
    }


}