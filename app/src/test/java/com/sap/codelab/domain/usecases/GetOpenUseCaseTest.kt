package com.sap.codelab.domain.usecases

import app.cash.turbine.test
import com.sap.codelab.domain.model.Memo
import com.sap.codelab.domain.repository.MemoRepository
import com.sap.codelab.presentation.mapper.toUI
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
class GetOpenUseCaseTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var memoRepository: MemoRepository

    private lateinit var getOpenUseCase: GetOpenUseCase

    @Before
    fun setUp() {
        getOpenUseCase = GetOpenUseCase(memoRepository)
    }

    @Test
    fun `invoke should call repository and map open memos to UI models`() = runTest {
        val domainMemos = listOf(
            Memo(id = 1, title = "Open Memo 1", description = "Desc 1", 0.0, 0.0, isDone = false),
            Memo(id = 3, title = "Open Memo 2", description = "Desc 3", 2.2, 2.2, isDone = false)
        )
        val expectedUiMemos = domainMemos.map { it.toUI() }
        whenever(memoRepository.getOpen()).thenReturn(flowOf(domainMemos))
        val resultFlow = getOpenUseCase.invoke()

        verify(memoRepository).getOpen()

        resultFlow.test {
            val emittedList = awaitItem()
            assertEquals(expectedUiMemos, emittedList)
            awaitComplete()
        }
    }
}