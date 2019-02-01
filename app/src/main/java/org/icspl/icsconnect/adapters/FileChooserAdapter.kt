package org.icspl.icsconnect.adapters

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import org.icspl.icsconnect.R

class FileChooserAdapter(
    context: Context,
    private val count: Int


) : BaseAdapter() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    override fun getCount(): Int {
        return count
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewHolder: ViewHolder
        var view: View? = convertView

        if (view == null) {
            view = layoutInflater.inflate(R.layout.griddialog_filechooser, parent, false)
            viewHolder = ViewHolder(view.findViewById(R.id.img_camera), view.findViewById(R.id.item_name))
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder

        }
        val context = parent.context
    viewHolder.textView.text=context.getString(R.string.connect_us)
      //  viewHolder.imageView.setImageResource(R.mipmap.ic_launcher)
      //  viewHolder.imageView.setImageResource(R.drawable.user3)

        return view!!
    }


    public data class ViewHolder(public val imageView: ImageView, public val textView: TextView)
}




