package org.icspl.icsconnect.activity

import LoginPreference
import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import org.icspl.icsconnect.MainActivity
import org.icspl.icsconnect.R
import org.icspl.icsconnect.models.EmployeeDetail
import org.icspl.icsconnect.utils.Common
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    val TAG = LoginActivity::class.java.canonicalName
    private val mService by lazy { Common.getAPI() }
    private val mLoginPreference by lazy { LoginPreference.getInstance(this@LoginActivity) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_login)
        object : CountDownTimer(2000, 1000) {
            override fun onFinish() {
                bookITextView.visibility = View.GONE
                loadingProgressBar.visibility = View.GONE
                rootView.setBackgroundColor(ContextCompat.getColor(this@LoginActivity, R.color.colorSplashText))
                bookIconImageView.setImageResource(R.drawable.ic_menu_gallery)
                startAnimation()
            }

            override fun onTick(p0: Long) {}
        }.start()

        if (mLoginPreference.getStringData("id", "") != null) {
            startActivity(
                Intent(
                    this@LoginActivity, MainActivity
                    ::class.java
                )
            )
        }
        loginButton!!.setOnClickListener { validateUserInput() }
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
            l_pass!!.error = "Input required"
            l_user!!.isErrorEnabled = true
            l_pass!!.isErrorEnabled = true
        } else if (TextUtils.isEmpty(strUsername)) {
            l_user!!.error = "Input required"
            l_user!!.isErrorEnabled = true
        } else if (TextUtils.isEmpty(password)) {
            l_user!!.error = "Input required"
            l_pass!!.isErrorEnabled = true
            l_user!!.isErrorEnabled = false
        } else {
            l_pass!!.isErrorEnabled = false
            l_user!!.isErrorEnabled = false
        }


    }

    private fun fetchLogin(strUsername: String, password: String) {


        mService.checkLogin(strUsername, password)
            .enqueue(object : Callback<EmployeeDetail> {
                override fun onResponse(call: Call<EmployeeDetail>, response: Response<EmployeeDetail>) {
                    if (response.isSuccessful && response.body() != null) {
                        Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_LONG).show()
                        mLoginPreference.savStringeData("id", strUsername)
                        mLoginPreference.savStringeData("password", password)
                        startActivity(
                            Intent(
                                this@LoginActivity, MainActivity
                                ::class.java
                            )
                        )
                    } else Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_LONG).show()
                }

                override fun onFailure(call: Call<EmployeeDetail>, t: Throwable) {
                    Log.i(TAG, "Error: ${t.message}")
                    Toast.makeText(
                        this@LoginActivity, "Login Failed Error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            })

    }
}

