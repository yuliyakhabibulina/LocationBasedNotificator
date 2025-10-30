package com.sap.codelab.repository

import androidx.room.Room
import android.content.Context
import androidx.annotation.WorkerThread
import com.sap.codelab.model.Memo
import javax.inject.Inject

private const val DATABASE_NAME: String = "codelab"

/**
 * The repository is used to retrieve data from a data source.
 */
internal class IMomoRepositoryImpl @Inject constructor(
    private val memoDao: MemoDao
) : IMemoRepository {

    @WorkerThread
    override fun saveMemo(memo: Memo) {
        memoDao.insert(memo)
    }

    @WorkerThread
    override fun getOpen(): List<Memo> = memoDao.getOpen()

    @WorkerThread
    override fun getAll(): List<Memo> = memoDao.getAll()

    @WorkerThread
    override fun getMemoById(id: Long): Memo = memoDao.getMemoById(id)
}