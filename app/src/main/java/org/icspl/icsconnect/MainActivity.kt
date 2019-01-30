package org.icspl.icsconnect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import org.icspl.icsconnect.activity.RaiseQueryActivity
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

                Log.d(TAG, "Firebase Token: " + token)
            })

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, OpenedCountFragment(), getString(R.string.count_fragment))
            .commit()

        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, RaiseQueryActivity::class.java)
            startActivity(intent)
        }
    }


}
