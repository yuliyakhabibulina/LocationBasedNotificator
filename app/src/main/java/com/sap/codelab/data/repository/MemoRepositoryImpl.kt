package com.sap.codelab.data.repository

import com.sap.codelab.data.database.MemoDao
import com.sap.codelab.data.mapper.fromEntity
import com.sap.codelab.data.mapper.toEntity
import kotlinx.coroutines.flow.map
import com.sap.codelab.domain.model.Memo
import com.sap.codelab.domain.repository.MemoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The repository is used to retrieve data from a data source.
 */
@Singleton
class MemoRepositoryImpl @Inject constructor(
    private val memoDao: MemoDao
) : MemoRepository {

    override suspend fun saveMemo(memo: Memo) : Long {
        return memoDao.insert(memo.toEntity())
    }

    override fun getOpen(): Flow<List<Memo>> = memoDao.getOpen().map { memoEntity ->
        memoEntity.map { it.fromEntity() }
    }

    override fun getAll(): Flow<List<Memo>> = memoDao.getAll().map { memoEntity ->
        memoEntity.map { it.fromEntity() }
    }

    override suspend fun getMemoById(id: Long): Memo? = memoDao.getMemoById(id)?.fromEntity()
}