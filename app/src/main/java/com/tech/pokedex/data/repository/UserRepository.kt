package com.tech.pokedex.data.repository

import android.os.Build
import com.tech.pokedex.data.local.SessionManager
import com.tech.pokedex.data.local.dao.UserDao
import com.tech.pokedex.data.local.entity.UserEntity
import com.tech.pokedex.util.PasswordUtil
import com.tech.pokedex.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.security.SecureRandom
import java.util.UUID
import kotlin.random.Random

class UserRepository(
    private val userDao: UserDao,
    private val sessionManager: SessionManager
) {

    val isLoggedIn: Flow<Boolean> = sessionManager.isLoggedIn
    val activeUserId: Flow<String?> = sessionManager.activeUserId
    val loginMethod: Flow<String?> = sessionManager.loginMethod
    val lastLoggedInUserId: Flow<String?> = sessionManager.lastLoggedInUserId

    suspend fun registerUser(
        fullname: String,
        password: String
    ) : Resource<UserEntity> = withContext(Dispatchers.IO) {
        try {
            val cleanName = fullname.replace("\\s".toRegex(), "")
            var generatedTrainerId = ""
            var isUnique = false

            while (!isUnique) {
                val secRandom = SecureRandom()
                val randNum = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                    secRandom.nextInt(1, 1000)
                } else {
                    Random.nextInt(1, 1000)
                }
                generatedTrainerId = StringBuilder("${cleanName}_${randNum}").toString()
                val existCount = userDao.checkTrainerExists(generatedTrainerId)
                if (existCount == 0) isUnique = true
            }

            val currentTime = System.currentTimeMillis()
            val hashedPassword = PasswordUtil.hashing(password)

            val newUser = UserEntity(
                userId = UUID.randomUUID().toString(),
                fullName = fullname,
                trainerId = generatedTrainerId,
                passwordHash = hashedPassword,
                isUsingBiometric = false,
                biometricKey = "",
                createdAt = currentTime,
                updatedAt = 0L
            )

            userDao.registerUser(newUser)
            Resource.Success(newUser)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Gagal melakukan registrasi")
        }
    }

    suspend fun login(trainerId: String, password: String): Resource<UserEntity> = withContext(Dispatchers.IO) {
        try {
            val hashedPassword = PasswordUtil.hashing(password)
            val user = userDao.login(trainerId, hashedPassword)
            if (user != null) {
                sessionManager.saveSession(userId = user.userId, loginMethod = "PASSWORD")
                Resource.Success(user)
            } else {
                Resource.Error("Trainer ID atau Password salah!")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Terjadi kesalahan sistem")
        }
    }

    suspend fun loginByBiometric(userId: String): Resource<UserEntity> = withContext(Dispatchers.IO) {
        try {
            val user = userDao.getUserById(userId)

            // Validasi ganda: Pastikan user ada dan fitur biometriknya memang aktif
            if (user != null && user.isUsingBiometric) {

                // Opsional: Jika Anda menggunakan CryptoObject dari BiometricPrompt,
                // Anda bisa memvalidasi biometricKey di sini.

                // Simpan session dengan flag khusus (dibahas di poin 2)
                sessionManager.saveSession(userId = user.userId, loginMethod = "BIOMETRIC")

                Resource.Success(user)
            } else {
                Resource.Error("Setup biometrik tidak valid atau pengguna tidak ditemukan.")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Terjadi kesalahan sistem")
        }
    }

    suspend fun logout() {
        sessionManager.clearSession()
    }

    suspend fun getUserProfile(userId: String): Resource<UserEntity> = withContext(Dispatchers.IO) {
        try {
            val user = userDao.getUserById(userId)
            if (user != null) {
                Resource.Success(user)
            } else {
                Resource.Error("Data pengguna tidak ditemukan")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Gagal memuat profil")
        }
    }

    suspend fun updateBiometricSetup(
        userId: String,
        isUsingBiometric: Boolean,
        biometricKey: String
    ): Resource<Boolean> = withContext(Dispatchers.IO) {
        try {
            val currentTime = System.currentTimeMillis()

            userDao.updateBiometricSetup(
                userId = userId,
                isUsingBiometric = isUsingBiometric,
                biometricKey = biometricKey,
                updatedAt = currentTime
            )

            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Gagal memperbarui pengaturan biometrik")
        }
    }
}