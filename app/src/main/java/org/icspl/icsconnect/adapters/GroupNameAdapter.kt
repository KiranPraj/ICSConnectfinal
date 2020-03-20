package org.icspl.icsconnect.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import kotlinx.android.synthetic.main.fragment_master_admin.view.*
import org.icspl.icsconnect.MainActivity
import org.icspl.icsconnect.R
import org.icspl.icsconnect.activity.GroupActivity
import org.icspl.icsconnect.fragments.GrpNameFragmentFragment
import org.icspl.icsconnect.fragments.MasterAdminFragment
import org.icspl.icsconnect.models.IndividualGrpNameModel
import org.icspl.icsconnect.preferences.LoginPreference
import android.widget.CompoundButton



/**
 * Created by Suraj on 2/14/2018.
 */

class GroupNameAdapter(

    private val mList: List<IndividualGrpNameModel.IndividualGroup>,
    var arrayList: ArrayList<String>,
    private val mContext:


    Context, var mCallback: GrpNameCallback
    ) : androidx.recyclerview.widget.RecyclerView.Adapter<GroupNameAdapter.ViewHolder>() {

    interface GrpNameCallback {
        fun GrpNameListener(
            id: String, photo: String?, isMineQuery: Boolean,
            models: IndividualGrpNameModel.IndividualGroup,arrayList: ArrayList<String>
        )
        fun GrpVisible(models: IndividualGrpNameModel.IndividualGroup,arrayList: ArrayList<String>)
    }

    inner class ViewHolder internal constructor(view: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        public var tv_iitem_group_name: TextView
        public var lin: LinearLayout
        public var checkbox: CheckBox
        public var editpen:ImageView
        public var schecked:Boolean = true


        init {
            tv_iitem_group_name = view.findViewById(R.id.tv_iitem_group_name)
            lin = view.findViewById(R.id.lin)
           // var arrayList:ArrayList<String>?=null
            checkbox = view.findViewById(R.id.checkbox)
            editpen=view.findViewById(R.id.editpen)
            //this.setIsRecyclable(false)

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_group_name, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun getItemViewType(position: Int): Int {
        return 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model = mList[holder.adapterPosition]

        holder.checkbox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {

            override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
                if (isChecked) {



                    var k = mList.get(holder.adapterPosition).groupId!!
                    if (arrayList.contains(k)) {

                        Toast.makeText(mContext, "Group is already selected", Toast.LENGTH_SHORT).show()
                    } else {


                        arrayList.add(k)
                     //   Toast.makeText(mContext, "name" + arrayList, Toast.LENGTH_LONG).show()
                        //     Log.e("Tag", arrayList!!.get(0).toString())
                        //  mCallback.GrpNameListener("abcd", "abcd", true, mList.get(holder.adapterPosition),arrayList)
                        mCallback.GrpVisible(mList.get(holder.adapterPosition), arrayList)
                    }
                }

                else
                {


                    var k = mList.get(holder.adapterPosition).groupId!!
                    var pos=arrayList.indexOf(k)
                    arrayList.removeAt(pos)
                   // Toast.makeText(mContext, "name" + arrayList, Toast.LENGTH_LONG).show()

                }


            }

        })

        holder.tv_iitem_group_name.text = model.groupTitle
//holder.checkbox.isChecked= model.schecked!!
        val mLoginPreference by lazy { LoginPreference.getInstance(mContext)}
        holder.lin.setOnClickListener {
            mLoginPreference.savStringeData("groupid",model.groupId!!)
            holder.setIsRecyclable(false)
            mCallback.GrpNameListener("abcd", "abcd", true, mList.get(holder.adapterPosition),arrayList)
        }

            holder.checkbox.visibility = View.INVISIBLE
            holder.editpen.visibility=View.INVISIBLE


            val id=mLoginPreference.getStringData("id","")
            val master=mList.get(holder.adapterPosition).masterAdmin
            val admin=mList.get(holder.adapterPosition).groupAdmin
            val members=mList.get(holder.adapterPosition).members
        if(!members!!.contains(id!!)||members!!.contains(master!!))
        {
                holder.lin.setOnLongClickListener(View.OnLongClickListener {
                    holder.editpen.visibility=View.VISIBLE
                     if(id==master)
                     {
                         holder.checkbox.visibility = View.VISIBLE


                    if (holder.checkbox.isChecked) {
//                        holder.checkbox.visibility = View.VISIBLE

                        var k = mList.get(holder.adapterPosition).groupId!!
                        if (arrayList.contains(k)) {
                            Toast.makeText(mContext, "Group is already selected", Toast.LENGTH_SHORT).show()
                        } else {
                            arrayList.add(k)
                        //    Toast.makeText(mContext, "name" + arrayList, Toast.LENGTH_LONG).show()
                       //     Log.e("Tag", arrayList!!.get(0).toString())
                            //  mCallback.GrpNameListener("abcd", "abcd", true, mList.get(holder.adapterPosition),arrayList)

//

                            //holder.setIsRecyclable(false)
                            mCallback.GrpVisible(mList.get(holder.adapterPosition), arrayList)
                        }
                        holder.setIsRecyclable(false)
                     }
                     }

//                checkbox.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
//
//                    if (isChecked){
//                        arrayList!!.add(mList.get(0).groupId.toString())
//
//                        Toast.makeText(mContext,"name"+arrayList,Toast.LENGTH_LONG).show()
//                        Log.e("Tag",arrayList.get(0).toString())
//                    }
//                })
                     return@OnLongClickListener true
                })
        //
            }
        //   holder.checkbox.visibility=View.INVISIBLE
            holder.setIsRecyclable(false)
        holder.editpen.setOnClickListener(View.OnClickListener {
            var i=Intent(mContext,GroupActivity::class.java)
       //     mLoginPreference.savStringeData("position",position.toString())
       mLoginPreference.savStringeData("groupid",model.groupId!!)
            mLoginPreference.savStringeData("masterAdmin",model.masterAdmin!!)
            mLoginPreference.savStringeData("Admin",model.groupAdmin!!)
            mLoginPreference.savStringeData("members",model.members!!)
            mLoginPreference.savStringeData("title",model.groupTitle!!)
            mContext.startActivity(i)
        })
    }
    companion object {
        const val TAG = "GroupNameAdapter"
        }

}