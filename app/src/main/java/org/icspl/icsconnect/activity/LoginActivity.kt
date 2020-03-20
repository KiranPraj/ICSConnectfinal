package org.icspl.icsconnect.activity

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.layout_holder_me.*
import kotlinx.android.synthetic.main.layout_holder_you.*
import org.icspl.icsconnect.MainActivity
import org.icspl.icsconnect.R
import org.icspl.icsconnect.models.EmployeeDetail
import org.icspl.icsconnect.preferences.LoginPreference
import org.icspl.icsconnect.utils.Common
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Build
import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat.getSystemService
import android.view.inputmethod.InputMethodManager
import es.dmoral.toasty.Toasty
import org.icspl.icsconnect.preferences.Preferencetoken
import java.time.LocalDate
import kotlin.math.log


class LoginActivity : AppCompatActivity() {

    val TAG = LoginActivity::class.java.canonicalName
    private val mService by lazy { Common.getAPI() }
    private val mLoginPreference by lazy { LoginPreference.getInstance(this@LoginActivity) }
    lateinit var token: String
    private val mPreferencetoken by lazy { Preferencetoken.getInstance(this@LoginActivity) }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     //   requestWindowFeature(Window.FEATURE_NO_TITLE)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )
        token = gettoken()
        setContentView(R.layout.activity_login)
        object : CountDownTimer(2000, 1000) {
            override fun onFinish() {
                bookITextView.visibility = View.GONE
                loadingProgressBar.visibility = View.GONE
                rootView.setBackgroundColor(ContextCompat.getColor(this@LoginActivity, R.color.yellow))
                //bookIconImageView.setImageResource(R.drawable.ic_menu_gallery)
                startAnimation()
            }

            override fun onTick(p0: Long) {}
        }.start()

        if (mLoginPreference.getStringData("id", "") != "") {
            val v = mLoginPreference.getStringData("id", "")
            startActivity(
                Intent(
                    this@LoginActivity, DashBoardActivity
                    ::class.java

                )

            )

        }
        loginButton!!.setOnClickListener { validateUserInput() }
       // var number =getMyPhoneNO()
       // Toasty.info(this@LoginActivity, number.toString(),Toast.LENGTH_SHORT).show()
    }
    @SuppressLint("MissingPermission")
    fun getMyPhoneNO(): String? {
        var tMgr = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var mPhoneNumber = tMgr!!.simSerialNumber
        return mPhoneNumber;
    }
    // show animations
    private fun startAnimation() {
        bookIconImageView.animate().apply {
            x(50f)
            y(100f)
            duration = 1000
        }.setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                afterAnimationView.visibility = VISIBLE


            }

            override fun onAnimationCancel(p0: Animator?) {

            }

            override fun onAnimationStart(p0: Animator?) {

            }
        })
    }

    //start validation
    private fun validateUserInput() {
        val mgr = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        mgr.hideSoftInputFromWindow(loginButton.getWindowToken(), 0)
            loadingProgressBar.visibility=View.VISIBLE
        // validating user name and password Inputs
        val strUsername = login_email.text.toString()
        val password = login_password.text.toString()

        if (!TextUtils.isEmpty(strUsername) && !TextUtils.isEmpty(password)) {
            // user is online handle creation or login records
            fetchLogin(strUsername, password)
            // gotoReportFragment(); // if all goes rigth go to home
            l_user!!.isErrorEnabled = false
            l_pass!!.isErrorEnabled = false
        } else if (TextUtils.isEmpty(strUsername) && TextUtils.isEmpty(password)) {
            l_user!!.error = "Input required"
            loadingProgressBar.visibility=View.GONE
            l_pass!!.error = "Input required"
            l_user!!.isErrorEnabled = true
            l_pass!!.isErrorEnabled = true
        } else if (TextUtils.isEmpty(strUsername)) {
            loadingProgressBar.visibility=View.GONE
            l_user!!.error = "Input required"
            l_user!!.isErrorEnabled = true
        } else if (TextUtils.isEmpty(password)) {
            loadingProgressBar.visibility=View.GONE
            l_user!!.error = "Input required"
            l_pass!!.isErrorEnabled = true
            l_user!!.isErrorEnabled = false
        } else {
            loadingProgressBar.visibility=View.GONE
            l_pass!!.isErrorEnabled = false
            l_user!!.isErrorEnabled = false
        }


    }

    private fun fetchLogin(strUsername: String, password: String) {

        mService.checkLogin(strUsername, password, token)
            .enqueue(object : Callback<EmployeeDetail> {
                @SuppressLint("CheckResult")
                override fun onResponse(call: Call<EmployeeDetail>, response: Response<EmployeeDetail>) {
                    try {
                        if (response.isSuccessful && response.body()!!.employeeDetails.isNotEmpty()) {
                         //   Toast.makeText(this@LoginActivity, ""+response.body(), Toast.LENGTH_LONG).show()
                            mLoginPreference.savStringeData("id", strUsername)
                            mLoginPreference.savStringeData("password", password)
                            mLoginPreference.savStringeData("name", response.body()!!.employeeDetails[0].name)
                            mLoginPreference.savStringeData("masteradmin", response.body()!!.employeeDetails[0].masteradmin)
                          if(response.body()!!.employeeDetails[0].photo== null){
                              mLoginPreference.savStringeData("photo",R.drawable.bg_chat_img.toString())
                          }else{
                              mLoginPreference.savStringeData("photo", response.body()!!.employeeDetails[0].photo)

                          }
                            mLoginPreference.savStringeData("station",response.body()!!.employeeDetails[0].station)
                            mLoginPreference.savStringeData("designation", response.body()!!.employeeDetails[0].role)
                            mLoginPreference.savStringeData("joindate", response.body()!!.employeeDetails[0].doj)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                mLoginPreference.savStringeData("logindate", LocalDate.now().toString())
                            }


                            //room insert
                          //  var noteRepository = NoteRepository(applicationContext) as NoteRepository
                        //    noteRepository.insertTask(strUsername,password,response.body()!!.employeeDetails[0].photo, response.body()!!.employeeDetails[0].name,response.body()!!.employeeDetails[0].masteradmin,response.body()!!.employeeDetails[0].masteradmin,response.body()!!.employeeDetails[0].masteradmin)







                            loadingProgressBar.visibility=View.GONE
                            Toasty.success(this@LoginActivity,"Login Success",Toast.LENGTH_LONG).show()
                            //Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_LONG).show()
                            startActivity(
                                Intent(
                                    this@LoginActivity, DashBoardActivity
                                    ::class.java

                                )


                            )

                            //helliohh this@MainActivity.finish()
                        }
                        else
                        {
                            loadingProgressBar.visibility=View.GONE
                            Toasty.error(this@LoginActivity, "Login Failed ", Toast.LENGTH_SHORT).show()
//                            Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        loadingProgressBar.visibility=View.GONE
                        e.message.toString()
                        Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<EmployeeDetail>, t: Throwable) {
                    loadingProgressBar.visibility=View.GONE
                    Log.i(TAG, "Error: ${t.message}")
                    Toast.makeText(
                        this@LoginActivity, "Login Failed Error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            })

    }

    private fun gettoken(): String {
        var token: String? = null
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(OnCompleteListener {
                if (!it.isSuccessful) {
                    Log.w("TOKEN Failed", "getInstanceId failed", it.exception)
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                token = it.result!!.token

                if (token != null) mPreferencetoken.savStringeData("token", token!!)

                Log.d(MainActivity.TAG, "Login Firebase Token: " + token)
            })

        return mPreferencetoken.getStringData("token", "")!!
    }
}

