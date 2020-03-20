package org.icspl.icsconnect.fragments
import android.content.Context
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_leave.view.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home_leave.view.*
import org.icspl.icsconnect.R

import org.icspl.icsconnect.adapters.LeaveAdaptor
import org.icspl.icsconnect.models.HomescreenModel
import org.icspl.icsconnect.preferences.LoginPreference

class LeaveHomeFragment : Fragment(), LeaveAdaptor.Callback {


    var list = ArrayList<HomescreenModel>()
    private lateinit var mContext: Context
    private lateinit var mView: View
    private lateinit var mActivity: FragmentActivity
    private lateinit var master_admin:String
    private lateinit var user:String
    private lateinit var mLoginPreference:LoginPreference
    //  private val mLoginPreference by lazy { LoginPreference.getInstance(mContext) }

    companion object {

        fun newInstance(): LeaveHomeFragment {
            return LeaveHomeFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

       mView= inflater.inflate(R.layout.fragment_home_leave, container, false)
         mLoginPreference=LoginPreference.getInstance(mView.context)
          master_admin=mLoginPreference.getStringData("masteradmin","").toString()
        user=mLoginPreference.getStringData("user","").toString()

        linearview(mView)
        mActivity = activity as FragmentActivity
      return mView
    }

    private fun linearview(mView: View) {
        mContext = mView.context
       list.clear()
        val homescreenmodel1 = HomescreenModel(R.drawable.timesheet, "Leave Status")
        val homescreenmodel2 = HomescreenModel(R.drawable.al_leave, "Apply Leave")
        val homescreenmodel3 = HomescreenModel(R.drawable.leave, "Leave Details")
        val homescreenmodel4 = HomescreenModel(R.drawable.leave, "Late Coming Deduction")
        val homescreenmodel5 = HomescreenModel(R.drawable.leave, "Cancel CompOff")

           list.add(homescreenmodel1)
           list.add(homescreenmodel2)
           list.add(homescreenmodel3)
          list.add(homescreenmodel4)
          list.add(homescreenmodel5)
           val layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
           mView.rv.setLayoutManager(layoutManager)
           val adaptorHOme = LeaveAdaptor(mContext,this, list)
           mView.rv.setAdapter(adaptorHOme)
           adaptorHOme.notifyDataSetChanged()



    }
    override fun positionhandler(position: Int)
    {
        if(position.equals(0))
        {

            mActivity.supportFragmentManager.beginTransaction().replace(R.id.group_container, Leave_Status(), getString(R.string.frag_leave_status)).addToBackStack(getString(R.string.leave_home_frg)).commit()


            //startActivity(Intent(mContext, Leave_Status::class.java))
        }
        else if(position.equals(1))
        {

            mActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.group_container,Apply_Leave())
                .addToBackStack(null)
                .commit()
        }
        else if(position.equals(2))
        {


            mActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.group_container,Leave_Details())
                .addToBackStack(null)
                .commit()

        }
        else if(position.equals(3))
        {
            mActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.group_container,Late_Coming_deduction())
                .addToBackStack(null)
                .commit()
        }
        else if(position.equals(4))
        {
            mActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.group_container,Cancel_Compoff_Fragment())
                .addToBackStack(null)
                .commit()
        }

    }


}
