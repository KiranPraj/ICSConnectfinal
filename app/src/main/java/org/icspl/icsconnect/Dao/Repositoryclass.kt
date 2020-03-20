/*
package org.icspl.icsconnect.Dao

import android.content.Context
import android.service.autofill.UserData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.icspl.icsconnect.models.user

@Database(entities = arrayOf(user::class), version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun chapterDao(): DaoAccess
    companion object {
        private var INSTANCE: UserDatabase? = null
        fun getDatabase(context: Context): UserDatabase? {
            if (INSTANCE == null) {
                synchronized(UserDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        UserDatabase::class.java, "User.db"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}*/
