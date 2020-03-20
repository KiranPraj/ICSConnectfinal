package org.icspl.icsconnect.adapters

import android.app.Activity
import android.content.Context
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.icspl.icsconnect.R
import org.icspl.icsconnect.activity.DashBoardActivity
import org.icspl.icsconnect.activity.Timesheet
import org.icspl.icsconnect.models.HomescreenModel

class HomeAdaptor(
    homescreen: Context,
    callback: DashBoardActivity, internal var array: List<HomescreenModel>) :
    RecyclerView.Adapter<HomeAdaptor.Holder>() {
    internal var context: Context
    private var callback: Callback

    init {
        this.context = homescreen
        this.callback=callback

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): Holder {
        val v = LayoutInflater.from(context).inflate(R.layout.row_adaptorhome, viewGroup, false)
        return Holder(v)
    }

    override fun onBindViewHolder(holder: Holder, i: Int) {

        holder.tv_home.text = array[i].getName()
        holder.img_home.setImageResource(array[i].image)
        holder.selected.setOnClickListener(View.OnClickListener {
            callback.positionhandler(holder.adapterPosition)
        })

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { holder.getAdapterPosition();
                Toast.makeText(context, ""+holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                if (holder.getAdapterPosition()==0){
                    Intent intent = new Intent(context, Society_Details.class);
                    context.startActivity(intent);
                } else if (holder.getAdapterPosition()==5) {
                    Intent intent = new Intent(context, AccountManage.class);
                    context.startActivity(intent);
                }else if(holder.getAdapterPosition()==1){
                    Intent intent = new Intent(context, Meetings.class);
                    context.startActivity(intent);
                }
                else if (holder.getAdapterPosition()==2)
                {
                    Intent intent= new Intent(context, Notice.class);
                    context.startActivity(intent);
                }
                else if(holder.getAdapterPosition()==4)
                {
                    Intent intent = new Intent(context, Committee.class);
                    context.startActivity(intent);
                }
                else if(holder.getAdapterPosition()==6)
                {
                    Intent intent= new Intent(context, Complaints.class);
                    context.startActivity(intent);
                }
            }
        });*/


    }

    override fun getItemCount(): Int {
        return array.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img_home: ImageView
        val tv_home: TextView
        val selected:LinearLayout

        init {
            selected=itemView.findViewById(R.id.selected)
            img_home = itemView.findViewById(R.id.img_home)
            tv_home = itemView.findViewById(R.id.tv_home)

        }
    }
    interface Callback {
        fun positionhandler(position: Int)
    }
}
