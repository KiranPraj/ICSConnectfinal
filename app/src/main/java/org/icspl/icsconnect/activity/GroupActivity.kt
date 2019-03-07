package org.icspl.icsconnect.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.icspl.icsconnect.R
import org.icspl.icsconnect.fragments.GrpNameFragmentFragment
import org.icspl.icsconnect.fragments.MasterAdminFragment
import android.content.Intent




class GroupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(org.icspl.icsconnect.R.layout.activity_group)


        supportFragmentManager.beginTransaction()
            .add(org.icspl.icsconnect.R.id.group_container, MasterAdminFragment(), getString(org.icspl.icsconnect.R.string.master_admin_frag))
            .commit()

    }


}
