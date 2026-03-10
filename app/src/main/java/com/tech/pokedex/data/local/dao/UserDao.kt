package com.tech.pokedex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tech.pokedex.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun registerUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE trainer_id = :trainerId AND password_hash = :password LIMIT 1")
    suspend fun login(trainerId: String, password: String): UserEntity?

    @Query("SELECT COUNT(*) FROM users WHERE trainer_id = :trainerId")
    suspend fun checkTrainerExists(trainerId: String): Int

    @Query("SELECT * FROM users WHERE user_id = :userId LIMIT 1")
    suspend fun getUserById(userId: String): UserEntity?

    @Query("""
        UPDATE users 
        SET is_using_biometric = :isUsingBiometric, 
            biometric_key = :biometricKey, 
            updated_at = :updatedAt 
        WHERE user_id = :userId
    """)
    suspend fun updateBiometricSetup(
        userId: String,
        isUsingBiometric: Boolean,
        biometricKey: String,
        updatedAt: Long
    )
}