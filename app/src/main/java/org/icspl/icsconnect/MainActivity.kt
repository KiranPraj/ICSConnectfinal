package org.icspl.icsconnect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import org.icspl.icsconnect.activity.CLosedQueryActivity
import org.icspl.icsconnect.activity.GroupActivity
import org.icspl.icsconnect.activity.RaiseQueryActivity
import org.icspl.icsconnect.fragments.OpenedCountFragment
import org.icspl.icsconnect.preferences.LoginPreference


class MainActivity : AppCompatActivity() {
    private val mLoginPreference by lazy { LoginPreference.getInstance(this@MainActivity) }
    private lateinit var mToolbar: Toolbar
    private lateinit var menu: Menu
    private var item: MenuItem? = null


    companion object {
        val TAG = MainActivity::class.java.canonicalName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(mToolbar)
        mToolbar.title = "Opend Query"

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result!!.token
                mLoginPreference.savStringeData("token", token)

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(org.icspl.icsconnect.R.menu.menu_open, menu)
        this.menu = menu
        item = menu.findItem(org.icspl.icsconnect.R.id.menu_close)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_close -> {
                startActivity(Intent(this@MainActivity, CLosedQueryActivity::class.java))
            }
            R.id.menu_group -> {
                startActivity(Intent(this@MainActivity, GroupActivity::class.java))
            }
        }
        return true
    }


}
