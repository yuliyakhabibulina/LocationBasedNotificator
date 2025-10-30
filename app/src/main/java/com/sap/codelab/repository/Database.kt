package com.sap.codelab.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sap.codelab.model.Memo

/**
 * That database that is used to store information.
 */
@Database(entities = [Memo::class], version = 1, exportSchema = false)
abstract class MemoDatabase : RoomDatabase() {
    abstract fun getMemoDao(): MemoDao
}