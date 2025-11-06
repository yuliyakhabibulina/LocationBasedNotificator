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
class GetAllMemoUseCaseTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var memoRepository: MemoRepository

    private lateinit var getAllMemoUseCase: GetAllMemoUseCase

    @Before
    fun setUp() {
        getAllMemoUseCase = GetAllMemoUseCase(memoRepository)
    }

    @Test
    fun `invoke should call repository and map domain models to UI models`() = runTest {

        val domainMemos = listOf(
            Memo(id = 1, title = "Domain Title 1", description = "Desc 1", 0.0, 0.0, false),
            Memo(id = 2, title = "Domain Title 2", description = "Desc 2", 1.1, 1.1, true)
        )

        val expectedUiMemos = domainMemos.map { it.toUI() }
        whenever(memoRepository.getAll()).thenReturn(flowOf(domainMemos))
        val resultFlow = getAllMemoUseCase.invoke()
        verify(memoRepository).getAll()
        resultFlow.test {
            val emittedList = awaitItem()
            assertEquals(expectedUiMemos, emittedList)
            awaitComplete()
        }
    }
}
