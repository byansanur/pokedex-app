package com.tech.pokedex.ui.viewmodel

import com.tech.pokedex.data.local.entity.UserEntity
import com.tech.pokedex.data.repository.UserRepository
import com.tech.pokedex.util.MainDispatcherRule
import com.tech.pokedex.util.Resource
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.UUID

class AuthViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var userRepository: UserRepository
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setup() {
        userRepository = mockk(relaxed = true)

        every { userRepository.isLoggedIn } returns flowOf(false)
        every { userRepository.lastLoggedInUserId } returns flowOf(null)

        viewModel = AuthViewModel(userRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `login with correct credentials update authState to Success`() = runTest {
        val dummyTrainerId = "Ash_123"
        val dummyPassword = "password123"
        val dummyUser = UserEntity(
            userId = UUID.randomUUID().toString(),
            fullName = "Ash Ketchum",
            trainerId = dummyTrainerId,
            passwordHash = "hash",
            isUsingBiometric = false,
            biometricKey = "",
            createdAt = 0L,
            updatedAt = 0L
        )

        coEvery { userRepository.login(dummyTrainerId, dummyPassword) } returns Resource.Success(dummyUser)

        viewModel.login(dummyTrainerId, dummyPassword)

        val state = viewModel.authState.value
        assertTrue(state is Resource.Success)
        assertEquals(dummyUser, (state as Resource.Success).data)
    }

    @Test
    fun `login with wrong credentials updates authState to Error`() = runTest {
        val dummyTrainerId = "Salah_123"
        val dummyPassword = "salah"
        val expectedErrorMessage = "Trainer ID atau Password salah!"

        coEvery { userRepository.login(dummyTrainerId, dummyPassword) } returns Resource.Error(expectedErrorMessage)

        viewModel.login(dummyTrainerId, dummyPassword)

        val state = viewModel.authState.value
        assertTrue(state is Resource.Error)
        assertEquals(expectedErrorMessage, (state as Resource.Error).message)
    }

    @Test
    fun `resetAuthState changes authState back to null`() = runTest {
        coEvery { userRepository.login(any(), any()) } returns Resource.Error("Error")
        viewModel.login("id", "pass")

        assertTrue(viewModel.authState.value != null)

        viewModel.resetAuthState()

        assertEquals(null, viewModel.authState.value)
    }
}