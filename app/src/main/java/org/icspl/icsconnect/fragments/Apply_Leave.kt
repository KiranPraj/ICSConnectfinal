package org.icspl.icsconnect.fragments

import android.annotation.TargetApi
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.labters.lottiealertdialoglibrary.ClickListener
import com.labters.lottiealertdialoglibrary.DialogTypes
import com.labters.lottiealertdialoglibrary.LottieAlertDialog
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_time_sheet.*
import kotlinx.android.synthetic.main.apply_leave_frag.*
import kotlinx.android.synthetic.main.apply_leave_frag.view.*
import kotlinx.android.synthetic.main.leave_status_frag.*
import kotlinx.android.synthetic.main.leave_status_frag.view.*
import org.icspl.icsconnect.R
import org.icspl.icsconnect.models.CompOffstatus
import org.icspl.icsconnect.models.GetLeaveStatusDetails
import org.icspl.icsconnect.models.GetleaveYear
import org.icspl.icsconnect.preferences.LoginPreference
import org.icspl.icsconnect.utils.Common
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.util.*
import kotlin.collections.ArrayList

class Apply_Leave:Fragment() {
    private lateinit var mContext: Context
    private lateinit var mView: View
    internal lateinit var picker: DatePickerDialog
    internal lateinit var picker1: DatePickerDialog
    lateinit var cldr1: Calendar
    private val mLoginPreference by lazy { LoginPreference.getInstance(mContext) }
    private val mService by lazy { Common.getAPI() }
    val fin_years: MutableList<String> = ArrayList()
    val comp_leavelist:MutableList<String> = ArrayList()
    private lateinit var causual_leave: String
    private lateinit var sick_leave: String
    private lateinit var comp_off_leave: String
    private lateinit var pl_leave: String
    private lateinit var al_leave: String
    private  var select_leave_type:String="SELECT"


    companion object {

        fun newInstance(): LeaveHomeFragment {
            return LeaveHomeFragment()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mView = inflater.inflate(R.layout.apply_leave_frag, container, false)

        initview(mView)
        return mView
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initview(mView: View?) {
        mContext = mView!!.context
        mView.name_apply.text = mLoginPreference.getStringData("name", "")
        mView.emp_code_apply.text = mLoginPreference.getStringData("id", "")
        mView.station_apply.text = mLoginPreference.getStringData("station", "")
        getleave_year()
        getleavetype()
        selectdatehere()





    }

    private fun getleavetype() {
        val leave_type: MutableList<String> = ArrayList()
        leave_type.add("SELECT")
        leave_type.add("CL")
        leave_type.add("SL")
        leave_type.add("Comp")
        leave_type.add("Annual Leave")
        leave_type.add("Leave Without Pay")
        leave_type.add("Restricted Holiday")
        leave_type.add("Half Day")

        val adapter_leave =
            ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, leave_type)
        mView.leave_type_spinner.adapter = adapter_leave
        adapter_leave.notifyDataSetChanged()
    }

    private fun getSelected_leave() {

        leave_type_spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    mView.from_date.text=""
                    mView.to_date.text=""

                    select_leave_type = mView.leave_type_spinner.selectedItem.toString()
                    if (select_leave_type == "CL") {
                        linear_halfday_against.visibility = View.GONE
                        linear_comp_against.visibility=View.GONE
                       CLLottie()
                    }
                    if (select_leave_type == "SL") {
                        linear_halfday_against.visibility = View.GONE
                        linear_comp_against.visibility=View.GONE
                      SLLottie()
                    }
                    if (select_leave_type == "Comp") {
                        linear_halfday_against.visibility = View.GONE
                        CompLottie()

                    }
                    if (select_leave_type == "Annual Leave") {
                        linear_halfday_against.visibility = View.GONE
                        linear_comp_against.visibility=View.GONE
                        if (al_leave != "0.0") {
                            var alertDialog: LottieAlertDialog = LottieAlertDialog.Builder(
                                context,
                                DialogTypes.TYPE_CUSTOM,
                                "animation.json"
                            )
                                .setDescription("You have " + al_leave + " AL, Apply?")
                                .setPositiveButtonColor(Color.GREEN)
                                .setPositiveTextColor(Color.WHITE)
                                .setPositiveText("Ok")
                                .setNegativeButtonColor(Color.RED)
                                .setNegativeTextColor(Color.WHITE)
                                .setNegativeText("Cancel")
                                .setPositiveListener(object : ClickListener {
                                    override fun onClick(dialog: LottieAlertDialog) {
                                        dialog.dismiss()
                                        //  Applyleave()
                                    }

                                })
                                .setNegativeListener(object : ClickListener {
                                    override fun onClick(dialog: LottieAlertDialog) {
                                        dialog.dismiss()
                                        getleavetype()
                                    }

                                })

                                .build()
                            alertDialog.setCancelable(false)
                            alertDialog.show()

                        } else {
                            Toasty.error(mContext, "You don't have any CL", Toast.LENGTH_LONG).show()
                            getleavetype()
                        }
                    }
                    if (select_leave_type == "Leave Without Pay") {
                        linear_halfday_against.visibility = View.GONE
                        linear_comp_against.visibility=View.GONE
                    }

                    if (select_leave_type == "Restricted Holiday") {
                        linear_halfday_against.visibility = View.GONE
                        linear_comp_against.visibility=View.GONE
                    }

                    if (select_leave_type == "Half Day") {
                        linear_halfday_against.visibility = View.VISIBLE
                        linear_comp_against.visibility=View.GONE
                        val halfday_type: MutableList<String> = ArrayList()
                        halfday_type.add("SELECT")
                        halfday_type.add("CL")
                        halfday_type.add("SL")
                        halfday_type.add("Comp")
                        halfday_type.add("Leave Without Pay")

                        val adapter_leave = ArrayAdapter<String>(
                            mContext,
                            android.R.layout.simple_list_item_1,
                            halfday_type
                        )
                        mView.halfdayagainst_spinner.adapter = adapter_leave
                        adapter_leave.notifyDataSetChanged()
                        gethalfday_againstLeave()

                    }


                }
            }
    }

    private fun CompLottie() {
        if (comp_off_leave != "0.0") {
            var alertDialog: LottieAlertDialog = LottieAlertDialog.Builder(
                context,
                DialogTypes.TYPE_CUSTOM,
                "animation.json"
            )
                .setDescription("You have " + comp_off_leave + " Comp, Apply?")
                .setPositiveButtonColor(Color.GREEN)
                .setPositiveTextColor(Color.WHITE)
                .setPositiveText("Ok")
                .setNegativeButtonColor(Color.RED)
                .setNegativeTextColor(Color.WHITE)
                .setNegativeText("Cancel")
                .setPositiveListener(object : ClickListener {
                    override fun onClick(dialog: LottieAlertDialog) {
                        dialog.dismiss()
                        linear_comp_against.visibility=View.VISIBLE
                        getCompOffleave()
                        //  Applyleave()
                    }

                })
                .setNegativeListener(object : ClickListener {
                    override fun onClick(dialog: LottieAlertDialog) {
                        dialog.dismiss()
                        getleavetype()
                        gethalfday_againstLeave()
                        linear_halfday_against.visibility = View.GONE
                        linear_comp_against.visibility=View.GONE

                    }

                })

                .build()
            alertDialog.setCancelable(false)
            alertDialog.show()

        } else {
            Toasty.error(mContext, "You don't have any CompOff", Toast.LENGTH_LONG).show()
            getleavetype()
            gethalfday_againstLeave()
            linear_halfday_against.visibility = View.GONE
            linear_comp_against.visibility=View.GONE
        }
    }

    private fun getCompOffleave() {
        mService!!.getCompoOffDetails(mLoginPreference.getStringData("id","").toString())
            .enqueue(object : Callback<CompOffstatus> {
                override fun onFailure(call: Call<CompOffstatus>, t: Throwable) {
                    Toast.makeText(mContext,"something wrong", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<CompOffstatus>, response: Response<CompOffstatus>) {

                    if(response.isSuccessful)
                    {
                        for (i in 1..response.body()!!.compOffStatus!!.size)
                        {
                            comp_leavelist.add(response.body()!!.compDate.toString())

                        }
                        val adapter =  ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, comp_leavelist)
                        mView.compagainst_spinner.adapter = adapter
                        adapter.notifyDataSetChanged()
                       getCompoffagainstLeave()
                    }
                }
            })

    }

    private fun getCompoffagainstLeave() {

    }

    private fun gethalfday_againstLeave() {
        halfdayagainst_spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val select_halfdayagainst_type =
                        mView.halfdayagainst_spinner.selectedItem.toString()
                    if (select_halfdayagainst_type == "CL") {
                        CLLottie()
                    }
                    if (select_halfdayagainst_type == "SL") {
                        SLLottie()
                    }
                    if (select_halfdayagainst_type=="Comp")
                    {
                        CompLottie()
                    }


                }
            }
    }

    private fun  SLLottie()
    {

            if (sick_leave != "0.0")
    {
        var alertDialog: LottieAlertDialog =
            LottieAlertDialog.Builder(context, DialogTypes.TYPE_CUSTOM, "animation.json")
                .setDescription("You have " + sick_leave + " SL, Apply?")
                .setPositiveButtonColor(Color.GREEN)
                .setPositiveTextColor(Color.WHITE)
                .setPositiveText("Ok")
                .setNegativeButtonColor(Color.RED)
                .setNegativeTextColor(Color.WHITE)
                .setNegativeText("Cancel")
                .setPositiveListener(object : ClickListener {
                    override fun onClick(dialog: LottieAlertDialog) {
                        dialog.dismiss()
                     //  selectdatehere()
                    }

                })
                .setNegativeListener(object : ClickListener {
                    override fun onClick(dialog: LottieAlertDialog) {
                        dialog.dismiss()
                        getleavetype()
                        gethalfday_againstLeave()
                        linear_halfday_against.visibility = View.GONE
                    }

                })

                .build()
        alertDialog.setCancelable(false)
        alertDialog.show()

    } else
    {
        Toasty.error(mContext, "You dont have any SL", Toast.LENGTH_LONG).show()
        getleavetype()
        linear_halfday_against.visibility = View.GONE
    }
}
    private fun CLLottie() {
        if (causual_leave != "0.0") {
            var alertDialog: LottieAlertDialog = LottieAlertDialog.Builder(
                context,
                DialogTypes.TYPE_CUSTOM,
                "animation.json"
            )
                .setDescription("You have " + causual_leave + " CL, Apply?")
                .setPositiveButtonColor(Color.GREEN)
                .setPositiveTextColor(Color.WHITE)
                .setPositiveText("Ok")
                .setNegativeButtonColor(Color.RED)
                .setNegativeTextColor(Color.WHITE)
                .setNegativeText("Cancel")
                .setPositiveListener(object : ClickListener {

                    @RequiresApi(Build.VERSION_CODES.N)
                    override fun onClick(dialog: LottieAlertDialog) {
                        dialog.dismiss()
                        //  Applyleave()
                     //   selectdatehere()
                    }

                })
                .setNegativeListener(object : ClickListener {
                    override fun onClick(dialog: LottieAlertDialog) {
                        dialog.dismiss()
                        getleavetype()
                        gethalfday_againstLeave()
                        linear_halfday_against.visibility = View.GONE
                    }

                })

                .build()
            alertDialog.setCancelable(false)
            alertDialog.show()

        } else {
            Toasty.error(mContext, "You don't any CL", Toast.LENGTH_LONG).show()
            getleavetype()
            linear_halfday_against.visibility = View.GONE
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun selectdatehere() {
        var mindate: Long = 0
        mView.from_date.setOnClickListener {
            if (select_leave_type!="SELECT")
            {

                cldr1 = Calendar.getInstance()
                val day = cldr1.get(Calendar.DAY_OF_MONTH)
                val month = cldr1.get(Calendar.MONTH)
                val year = cldr1.get(Calendar.YEAR)

                picker1 = DatePickerDialog(
                    mContext,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        mView.from_date.text =
                            (monthOfYear + 1).toString() + "-" + dayOfMonth + "-" + year
                    }, year, month, day
                )


                picker1.show()
            }
            else
            {
                Toasty.error(mContext,"Firstly select Leave Type",Toast.LENGTH_LONG).show()
            }

        }

        mView.to_date.setOnClickListener {
            if (mView.from_date.text!="")
            {
                val cldr = Calendar.getInstance()
                val day = cldr.get(Calendar.DAY_OF_MONTH)
                val month = cldr.get(Calendar.MONTH)
                val year = cldr.get(Calendar.YEAR)
                picker = DatePickerDialog(
                    mContext,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        mView.to_date.text =
                            (monthOfYear + 1).toString() + "-" + dayOfMonth + "-" + year

                    }, year, month, day
                )
                /*var min:Any
                min=mView.from_date.text
                var minim:Long
                minim= min as Long*/
                var date: Date
                val formatter = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
                try {
                    date = formatter.parse(from_date.text.toString() + " 00:00:00")

                    mindate = date.getTime()
                } catch (e: ParseException) {
                    e.printStackTrace()
                }


                picker.datePicker.minDate = mindate
                picker.show()

            }
            else
            {
                Toasty.error(mContext,"Firstly select From Date",Toast.LENGTH_LONG).show()
            }

        }
    }


    private fun getleave_year() {


        mService!!.get_leave_year(mLoginPreference.getStringData("id","").toString())
            .enqueue(object : Callback<List<GetleaveYear>> {
                override fun onFailure(call: Call<List<GetleaveYear>>, t: Throwable) {
                    Toast.makeText(mContext,"something wrong", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<List<GetleaveYear>>, response: Response<List<GetleaveYear>>) {

                    if(response.isSuccessful)
                    {
                        for (i in 1..response.body()!!.size)
                        {
                            fin_years.add(response.body()!!.get(i-1).pfinYear.toString())

                        }
                        val adapter =  ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, fin_years)
                        mView.spinner_financial_year.adapter = adapter
                        adapter.notifyDataSetChanged()
                        getLeaveDetails()
                    }
                }
            })

    }

    private fun getLeaveDetails() {
        spinner_financial_year.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val select_year = mView.spinner_financial_year.selectedItem.toString()
                    mService!!.get_leave_status_details(
                        mLoginPreference.getStringData(
                            "id",
                            ""
                        ).toString(), select_year
                    )
                        .enqueue(object : Callback<List<GetLeaveStatusDetails>> {
                            override fun onFailure(
                                call: Call<List<GetLeaveStatusDetails>>,
                                t: Throwable
                            ) {
                                Toast.makeText(mContext, "some thing wrong", Toast.LENGTH_LONG)
                                    .show()
                            }

                            override fun onResponse(
                                call: Call<List<GetLeaveStatusDetails>>,
                                response: Response<List<GetLeaveStatusDetails>>
                            ) {

                                if (response.isSuccessful) {
                                    if (response.body()!!.size > 0) {
                                        for (i in 1..response.body()!!.size) {

                                            causual_leave =
                                                response.body()!!.get(i - 1).cL.toString()
                                            sick_leave = response.body()!!.get(i - 1).sL.toString()
                                            comp_off_leave =
                                                response.body()!!.get(i - 1).compoff.toString()
                                            al_leave = response.body()!!.get(i - 1).aL.toString()
                                            pl_leave = response.body()!!.get(i - 1).pL.toString()
                                            getSelected_leave()

                                        }
                                    } else {
                                        Toasty.error(
                                            mContext,
                                            "You don't have any leave",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }

                                }
                            }
                        })


                }
            }
    }}

