package com.tech.pokedex.data.repository

import com.tech.pokedex.data.local.SessionManager
import com.tech.pokedex.data.local.dao.UserDao
import com.tech.pokedex.data.local.entity.UserEntity
import com.tech.pokedex.util.PasswordUtil
import com.tech.pokedex.util.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.UUID

class UserRepositoryTest {

    private lateinit var userDao: UserDao
    private lateinit var sessionManager: SessionManager

    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        userDao = mockk(relaxed = true)
        sessionManager = mockk(relaxed = true)

        userRepository = UserRepository(userDao, sessionManager)

        mockkObject(PasswordUtil)
        coEvery { PasswordUtil.hashing(any()) } returns "hashed_password_123"
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `login with correct credentials returns Success and saves session`() = runTest {
        val dummyTrainerId = "Ash_01"
        val dummyPassword = "password_123"
        val dummyUser = UserEntity(
            userId = "user_id_123",
            fullName = "Ash Ketchum",
            trainerId = dummyTrainerId,
            passwordHash = "hashed_password_123",
            isUsingBiometric = false,
            biometricKey = "",
            createdAt = System.currentTimeMillis(),
            updatedAt = 0L
        )

        coEvery { userDao.login(dummyTrainerId, "hashed_password_123") } returns dummyUser

        val result = userRepository.login(dummyTrainerId, dummyPassword)

        assertTrue(result is Resource.Success)
        assertEquals(dummyUser, (result as Resource.Success).data)

        coVerify { sessionManager.saveSession(dummyUser.userId, "PASSWORD") }

    }

    @Test
    fun `logging in with incorrect credentials returns Error`() = runTest {
        val dummyTrainerId = "Salah_01"
        val dummyPassword = "salahpassword"

        coEvery { userDao.login(dummyTrainerId, "hashed_password_123") } returns null

        val result = userRepository.login(dummyTrainerId, dummyPassword)

        assertTrue(result is Resource.Error)
        assertEquals("Trainer ID atau Password salah!", (result as Resource.Error).message)

        coVerify(exactly = 0) { sessionManager.saveSession(any(), any()) }
    }

    @Test
    fun `loginByBiometric is successful if the user exists and biometrics are active`() = runTest {
        val dummyUserId = UUID.randomUUID().toString()
        val dummyUser = UserEntity(
            userId = dummyUserId,
            fullName = "Brock",
            trainerId = "Brock_02",
            passwordHash = "hash",
            isUsingBiometric = true,
            biometricKey = "secret_key",
            createdAt = System.currentTimeMillis(),
            updatedAt = 0L
        )

        coEvery { userDao.getUserById(dummyUserId) } returns dummyUser

        val result = userRepository.loginByBiometric(dummyUserId)

        assertTrue(result is Resource.Success)

        coVerify(exactly = 1) { sessionManager.saveSession(dummyUserId, "BIOMETRIC") }
    }

    @Test
    fun `login By Biometric fails if the user's biometric feature is not active`() = runTest {
        val dummyUserId = UUID.randomUUID().toString()
        val dummyUser = UserEntity(
            userId = dummyUserId,
            fullName = "Misty",
            trainerId = "Misty_03",
            passwordHash = "hash",
            isUsingBiometric = false,
            biometricKey = "",
            createdAt = 123L,
            updatedAt = 123L
        )

        coEvery { userDao.getUserById(dummyUserId) } returns dummyUser

        val result = userRepository.loginByBiometric(dummyUserId)

        assertTrue(result is Resource.Error)
        assertEquals("Setup biometrik tidak valid atau pengguna tidak ditemukan.", (result as Resource.Error).message)

        coVerify(exactly = 0) { sessionManager.saveSession(any(), any()) }
    }


}