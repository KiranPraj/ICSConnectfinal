package org.icspl.icsconnect.adapters

import android.content.Context
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import org.icspl.icsconnect.R
import org.icspl.icsconnect.fragments.LeaveHomeFragment
import org.icspl.icsconnect.models.HomescreenModel

class LeaveAdaptor(
    homescreen: Context,
    callback: LeaveHomeFragment, internal var array: List<HomescreenModel>) :
    RecyclerView.Adapter<LeaveAdaptor.Holder>() {
    internal var context: Context
    private var callback: Callback

    init {
        this.context = homescreen
        this.callback=callback

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): Holder {
        val v = LayoutInflater.from(context).inflate(R.layout.row_option_view, viewGroup, false)
        return Holder(v)
    }

    override fun onBindViewHolder(holder: Holder, i: Int) {

        holder.option.text = array[i].getName()
        holder.option.setIconResource(array[i].image)
        holder.option.setOnClickListener(View.OnClickListener {
            callback.positionhandler(holder.adapterPosition)
        })

    }

    override fun getItemCount(): Int {
        return array.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val option: MaterialButton

        init {
            option=itemView.findViewById(R.id.option)
        }
    }
    interface Callback {
        fun positionhandler(position: Int)
    }
}
