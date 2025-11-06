package com.sap.codelab.domain.usecases

import com.sap.codelab.domain.model.Memo
import com.sap.codelab.domain.repository.MemoRepository
import com.sap.codelab.presentation.mapper.toUI
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class GetMemoByIdUseCaseTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var memoRepository: MemoRepository

    private lateinit var getMemoByIdUseCase: GetMemoByIdUseCase

    @Before
    fun setUp() {
        getMemoByIdUseCase = GetMemoByIdUseCase(memoRepository)
    }

    @Test
    fun `invoke should return mapped UI model when memo is found`() = runTest {
        val memoId = 1L
        val domainMemo = Memo(id = memoId, title = "Domain Title", description = "Desc", 0.0, 0.0, false)
        val expectedUiMemo = domainMemo.toUI()
        whenever(memoRepository.getMemoById(memoId)).thenReturn(domainMemo)
        val result = getMemoByIdUseCase.invoke(memoId)
        verify(memoRepository).getMemoById(memoId)
        assertEquals(expectedUiMemo, result)
    }

    @Test
    fun `invoke should return null when memo is not found`() = runTest {
        val memoId = 99L
        whenever(memoRepository.getMemoById(memoId)).thenReturn(null)
        val result = getMemoByIdUseCase.invoke(memoId)
        verify(memoRepository).getMemoById(memoId)
        assertNull(result)
    }
}