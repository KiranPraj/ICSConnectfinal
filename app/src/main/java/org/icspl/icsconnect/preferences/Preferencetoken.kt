package org.icspl.icsconnect.preferences

import android.content.Context
import android.content.SharedPreferences
import org.icspl.icsconnect.R

class Preferencetoken (context: Context) {
    var sharedPreferences: SharedPreferences? = null

    init {
        // sharedPreferences = context.getSharedPreferences("YourCustomNamedPreference", Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.login), Context.MODE_PRIVATE)
    }

    // save string
    fun savStringeData(key: String, value: String) {
        val prefsEditor = sharedPreferences!!.edit()
        prefsEditor.putString(key, value)
        prefsEditor.apply()
    }

    // save int
    fun savInteData(key: String, value: Int) {
        val prefsEditor = sharedPreferences!!.edit()
        prefsEditor.putInt(key, value)
        prefsEditor.apply()
    }

    // fetch int
    fun getIntData(key: String, defValue: Int): Int {
        return if (sharedPreferences != null) {
            sharedPreferences!!.getInt(key, defValue)
        } else 0
    }


    // fetch string
    fun getStringData(key: String, defValue: String): String? {
        return if (sharedPreferences != null) {
            sharedPreferences!!.getString(key, defValue)
        } else ""
    }

    // save boolean
    fun saveBooleanData(key: String, value: Boolean?) {
        val prefsEditor = sharedPreferences!!.edit()
        prefsEditor.putBoolean(key, value!!)
        prefsEditor.apply()
    }

    // fetch boolean
    fun getBooleanData(key: String, defValue: Boolean?): Boolean? {
        return if (sharedPreferences != null) {
            sharedPreferences!!.getBoolean(key, defValue!!)
        } else null
    }

    companion object {

        private var ioclPrefrence: Preferencetoken? = null

        fun getInstance(context: Context): Preferencetoken {
            if (ioclPrefrence == null) {
                ioclPrefrence = Preferencetoken(context)
            }
            return ioclPrefrence as Preferencetoken
        }
    }

}