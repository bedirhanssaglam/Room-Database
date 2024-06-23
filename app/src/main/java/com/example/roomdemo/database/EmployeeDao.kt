package com.example.roomdemo.database

import androidx.room.*
import com.example.roomdemo.model.EmployeeEntity
import com.example.roomdemo.base.BaseDao
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for [EmployeeEntity]. Defines database operations.
 */
@Dao
interface EmployeeDao : BaseDao<EmployeeEntity> {
    @Query("SELECT * FROM 'employees'")
    fun fetchAllEmployees(): Flow<List<EmployeeEntity>>

    @Query("SELECT * FROM 'employees' where id=:id")
    fun fetchEmployeeByID(id: Int): Flow<EmployeeEntity>
}