package com.sap.codelab.domain.usecases
import com.sap.codelab.domain.repository.GeofenceRepository
import com.sap.codelab.domain.repository.MemoRepository
import com.sap.codelab.presentation.mapper.fromUI
import com.sap.codelab.presentation.model.MemoUI
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
class SaveMemoUseCaseTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var memoRepository: MemoRepository

    @Mock
    private lateinit var geofenceRepository: GeofenceRepository

    private lateinit var saveMemoUseCase: SaveMemoUseCase

    @Before
    fun setUp() {
        saveMemoUseCase = SaveMemoUseCase(memoRepository, geofenceRepository)
    }

    @Test
    fun `invoke should save memo via repo and add geofence with returned id`() = runTest {
        val inputMemoUI = MemoUI(id = 0, title = "Test Title", description = "Desc", 10.0, 20.0, false)
        val domainMemo = inputMemoUI.fromUI()
        val expectedMemoId = 123L
        whenever(memoRepository.saveMemo(domainMemo)).thenReturn(expectedMemoId)
        saveMemoUseCase.invoke(inputMemoUI)
        verify(memoRepository).saveMemo(domainMemo)
        val expectedMemoForGeofence = domainMemo.copy(id = expectedMemoId)
        verify(geofenceRepository).addGeofence(
            expectedMemoForGeofence,
            SaveMemoUseCase.GEOFENCE_RADIUS
        )
    }
}