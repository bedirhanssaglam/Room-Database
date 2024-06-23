package com.example.roomdemo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.roomdemo.constants.ColumnNames
import com.example.roomdemo.constants.DatabaseConstants

/**
 * Represents an employee entity with fields mapped to database columns.
 * @property id: Unique identifier for the employee (auto-generated).
 * @property name: Name of the employee.
 * @property email: Email address of the employee.
 */
@Entity(tableName = DatabaseConstants.EMPLOYEES)
data class EmployeeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    @ColumnInfo(name = ColumnNames.EMAIL_ID)
    val email: String = ""
)
