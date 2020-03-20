package org.icspl.icsconnect.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.icspl.icsconnect.R

class Cancel_Compoff_Fragment: Fragment()
{
    private lateinit var mView: View
    companion object
    {
        fun newInstance():Cancel_Compoff_Fragment {
           return Cancel_Compoff_Fragment()
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
       mView=inflater.inflate(R.layout.cancel_compoff_frag,container,false)
        return mView
    }
}