package org.icspl.icsconnect

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.Log.i
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import org.icspl.icsconnect.fragments.OpenedCountFragment


class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = MainActivity::class.java.canonicalName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result!!.token

                Log.d(TAG, token)
            })

        i("TOKEN", FirebaseInstanceId.getInstance().token.toString())
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, OpenedCountFragment(), getString(R.string.count_fragment))
            .commit()

    }
}
