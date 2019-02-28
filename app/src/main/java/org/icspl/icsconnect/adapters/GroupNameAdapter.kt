package org.icspl.icsconnect.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import org.icspl.icsconnect.R
import org.icspl.icsconnect.models.IndividualGrpNameModel

/**
 * Created by Suraj on 2/14/2018.
 */

class GroupNameAdapter(
    private val mList: List<IndividualGrpNameModel.IndividualGroup>, private val mContext:
    Context, var mCallback: GrpNameCallback
) : androidx.recyclerview.widget.RecyclerView.Adapter<GroupNameAdapter.ViewHolder>() {

    interface GrpNameCallback {
        fun GrpNameListener(
            id: String, photo: String?, isMineQuery: Boolean,
            models: IndividualGrpNameModel.IndividualGroup
        )
    }

    inner class ViewHolder internal constructor(view: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        public var tv_iitem_group_name: TextView
        public var ll_root_grp_name: LinearLayout


        init {
            tv_iitem_group_name = view.findViewById(R.id.tv_iitem_group_name)
            ll_root_grp_name = view.findViewById(R.id.ll_root_grp_name)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_group_name, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = mList[holder.adapterPosition]
        holder.tv_iitem_group_name.text = model.groupTitle
        holder.ll_root_grp_name.setOnClickListener {
            mCallback.GrpNameListener("abcd", "abcd", true,mList.get(holder.adapterPosition))
        }
    }

    companion object {
        const val TAG = "GroupNameAdapter"
    }


}