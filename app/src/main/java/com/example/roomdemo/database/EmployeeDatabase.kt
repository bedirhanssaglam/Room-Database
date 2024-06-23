package com.example.roomdemo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.roomdemo.model.EmployeeEntity
import com.example.roomdemo.constants.DatabaseConstants

/**
 * Room Database singleton class for managing [EmployeeEntity].
 */
@Database(entities = [EmployeeEntity::class], version = 1)
abstract class EmployeeDatabase : RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao

    companion object {
        @Volatile
        private var INSTANCE: EmployeeDatabase? = null

        fun getInstance(context: Context): EmployeeDatabase {
            synchronized(this) {
                var instance: EmployeeDatabase? = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        EmployeeDatabase::class.java,
                        name = DatabaseConstants.EMPLOYEE_DATABASE
                    ).fallbackToDestructiveMigration().build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}