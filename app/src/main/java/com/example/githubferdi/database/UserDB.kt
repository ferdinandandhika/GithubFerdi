package com.example.githubferdi.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [UserFav::class],
    version = 1
)
abstract class UserDB : RoomDatabase() {

    companion object {
        private var Instance: UserDB? = null

        fun getDatabase(context: Context): UserDB?{
            if (Instance==null) {
                synchronized(UserDB::class){
                    Instance = Room.databaseBuilder(context.applicationContext, UserDB::class.java, "user_database").build()
                }
            }
            return Instance
        }
    }

    abstract fun userDao(): UserDao
}
