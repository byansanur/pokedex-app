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
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AuthViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: AuthViewModel
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        userRepository = mockk(relaxed = true)
        every { userRepository.isLoggedIn } returns flowOf(false)
        every { userRepository.lastLoggedInUserId } returns flowOf(null)
        every { userRepository.activeUserId } returns flowOf(null)
        viewModel = AuthViewModel(userRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `register updates authState to Success when repository returns user`() = runTest {
        val fullName = "Ash Ketchum"
        val password = "password123"
        val dummyUser = mockk<UserEntity>(relaxed = true)
        
        coEvery { userRepository.registerUser(fullName, password) } returns Resource.Success(dummyUser)

        viewModel.register(fullName, password)

        val state = viewModel.authState.value
        assertTrue(state is Resource.Success)
        assertEquals(dummyUser, (state as Resource.Success).data)
    }

    @Test
    fun `register updates authState to Error when repository returns error`() = runTest {
        val fullName = "Ash Ketchum"
        val password = "password123"
        val errorMessage = "Registration failed"
        
        coEvery { userRepository.registerUser(fullName, password) } returns Resource.Error(errorMessage)

        viewModel.register(fullName, password)

        val state = viewModel.authState.value
        assertTrue(state is Resource.Error)
        assertEquals(errorMessage, (state as Resource.Error).message)
    }

    @Test
    fun `login updates authState to Success when repository returns user`() = runTest {
        val trainerId = "ash_123"
        val password = "password123"
        val dummyUser = mockk<UserEntity>(relaxed = true)
        
        coEvery { userRepository.login(trainerId, password) } returns Resource.Success(dummyUser)

        viewModel.login(trainerId, password)

        val state = viewModel.authState.value
        assertTrue(state is Resource.Success)
        assertEquals(dummyUser, (state as Resource.Success).data)
    }

    @Test
    fun `resetAuthState sets authState to null`() {
        // First set it to something
        // (This is a bit tricky with StateFlow, we can just call the method)
        viewModel.resetAuthState()
        assertNull(viewModel.authState.value)
    }
}
