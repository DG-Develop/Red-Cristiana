package com.david.redcristianauno.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.david.redcristianauno.domain.models.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun userDao(): UserDao
}