package org.icspl.icsconnect.adapters

import android.content.AsyncQueryHandler
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.icspl.icsconnect.R
import org.icspl.icsconnect.models.CountMSGModel

/**
 * Created by Suraj on 2/14/2018.
 */

class OpenedCountAdapter(
    private val mList: List<CountMSGModel.Countmessage>, private val mContext: Context, var mCallback: CounterListener
) : androidx.recyclerview.widget.RecyclerView.Adapter<OpenedCountAdapter.ViewHolder>() {

    interface CounterListener {
        fun clickListenere(id: String, photo: String?, isMineQuery: Boolean)
    }

    inner class ViewHolder internal constructor(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        public var tv_user_name: TextView
        public var tv_count_msg: TextView
        public var tv_last_chat: TextView
        public var iv_user_photo: ImageView
        public var tv_emp_id: TextView
        public var online_indicator: View
        public var v_dot_red: View

        init {
            tv_user_name = view.findViewById(R.id.tv_user_name)
            tv_last_chat = view.findViewById(R.id.tv_last_chat)
            iv_user_photo = view.findViewById(R.id.iv_user_photo)
            tv_count_msg = view.findViewById(R.id.tv_count_msg)
            tv_emp_id = view.findViewById(R.id.tv_emp_id)
            online_indicator = view.findViewById(R.id.online_indicator)
            v_dot_red = view.findViewById(R.id.v_dot_red)
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
        holder.tv_count_msg.text = model.msgCount.toString()
        holder.tv_user_name.text = model.fromEmpName
        holder.tv_emp_id.text = model.fromemp
        // holder.tv_count_msg.text = model.fromemp

        if (model.isIAm) {
            holder.online_indicator.visibility = View.GONE
            holder.v_dot_red.visibility = View.VISIBLE
            holder.itemView.setOnClickListener {
                mCallback.clickListenere(holder.tv_emp_id.text.toString(), model.photo,true)
            }
        } else {
            holder.online_indicator.visibility = View.VISIBLE
            holder.v_dot_red.visibility = View.GONE

            holder.itemView.setOnClickListener {
                mCallback.clickListenere(holder.tv_emp_id.text.toString(), model.photo,false)
            }
        }

        Picasso.get().load("http://icspl.org/data/Employeephoto/" + model.photo)
            .placeholder(R.drawable.ic_user)
            .error(R.drawable.ic_user)
            .into(holder.iv_user_photo)


    }

    companion object {
        const val TAG = "OpenedCountAdapter"
    }


}