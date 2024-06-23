package com.example.roomdemo.base

import android.app.Application
import com.example.roomdemo.database.EmployeeDatabase

/**
 * Custom Application class for initializing the Room database.
 */
class EmployeeApp : Application() {
    val database by lazy {
        EmployeeDatabase.getInstance(this)
    }
}