package com.tech.pokedex.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class PasswordUtilTest {

    @Test
    fun `hashing produces consistent result for same input`() {
        val password = "pokedex_password"
        val hash1 = PasswordUtil.hashing(password)
        val hash2 = PasswordUtil.hashing(password)
        
        assertEquals(hash1, hash2)
    }

    @Test
    fun `hashing produces different results for different inputs`() {
        val hash1 = PasswordUtil.hashing("password123")
        val hash2 = PasswordUtil.hashing("password124")
        
        assertNotEquals(hash1, hash2)
    }

    @Test
    fun `hashing produces expected SHA-256 string length`() {
        val hash = PasswordUtil.hashing("test")
        // SHA-256 produces 32 bytes, which is 64 characters in hex
        assertEquals(64, hash.length)
    }
}
