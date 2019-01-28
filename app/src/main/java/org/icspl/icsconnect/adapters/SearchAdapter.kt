package org.icspl.icsconnect.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.icspl.icsconnect.R



class SearchAdapter(
    private val mList: List<String>,
    private val mContext: Context
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {


    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        public var tv_search_result: TextView


        init {
            tv_search_result = view.findViewById(R.id.tv_search_result)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_search_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = mList[holder.adapterPosition]
        holder.tv_search_result.text = model
    }

    companion object {
        val TAG = this::class.java.simpleName
    }


}