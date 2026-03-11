package com.tech.pokedex.data.repository

import com.tech.pokedex.data.local.AppDatabase
import com.tech.pokedex.data.local.dao.FavoriteDao
import com.tech.pokedex.data.local.entity.FavoriteEntity
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FavoriteRepositoryTest {

    private lateinit var repository: FavoriteRepository
    private lateinit var database: AppDatabase
    private lateinit var favoriteDao: FavoriteDao

    @Before
    fun setUp() {
        database = mockk()
        favoriteDao = mockk(relaxed = true)
        every { database.favoriteDao() } returns favoriteDao
        repository = FavoriteRepository(database)
    }

    @Test
    fun `getFavorites returns flow from dao`() = runTest {
        val dummyFavorites = listOf(mockk<FavoriteEntity>())
        every { favoriteDao.getAllFavorites() } returns flowOf(dummyFavorites)

        repository.getFavorites().collect { result ->
            assertEquals(dummyFavorites, result)
        }
    }

    @Test
    fun `isFavorite returns flow from dao`() = runTest {
        val id = 1
        every { favoriteDao.isFavorite(id) } returns flowOf(true)

        repository.isFavorite(id).collect { result ->
            assertEquals(true, result)
        }
    }

    @Test
    fun `toggleFavorite adds to favorite when not already favorite`() = runTest {
        val dummyFavorite = FavoriteEntity(id = 1, name = "Pikachu", types = "electric")
        repository.toggleFavorite(dummyFavorite, isAlreadyFavorite = false)
        coVerify { favoriteDao.addFavorite(dummyFavorite) }
    }

    @Test
    fun `toggleFavorite removes from favorite when already favorite`() = runTest {
        val dummyFavorite = FavoriteEntity(id = 1, name = "Pikachu", types = "electric")
        repository.toggleFavorite(dummyFavorite, isAlreadyFavorite = true)
        coVerify { favoriteDao.removeFavorite(dummyFavorite.id) }
    }
}
