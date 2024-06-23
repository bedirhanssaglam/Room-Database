package com.example.roomdemo.base

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

/**
 * Base Data Access Object (DAO) interface defining common database operations.
 * @param T: Type of entity to perform operations on.
 */
interface BaseDao<T> {
    @Insert
    suspend fun insert(entity: T)

    @Update
    suspend fun update(entity: T)

    @Delete
    suspend fun delete(entity: T)
}