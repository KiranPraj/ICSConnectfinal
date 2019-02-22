package org.icspl.icsconnect.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.icspl.icsconnect.R
import org.icspl.icsconnect.fragments.GrpNameFragmentFragment
import org.icspl.icsconnect.fragments.MasterAdminFragment

class GroupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        supportFragmentManager.beginTransaction()
            .add(R.id.group_container, MasterAdminFragment(), getString(R.string.master_admin_frag))
            .commit()

    }
}
