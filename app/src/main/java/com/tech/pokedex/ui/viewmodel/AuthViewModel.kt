package com.tech.pokedex.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tech.pokedex.data.local.entity.UserEntity
import com.tech.pokedex.data.repository.UserRepository
import com.tech.pokedex.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    val isLoggedIn = userRepository.isLoggedIn.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = false
    )

    val lastLoggedInUserId = userRepository.lastLoggedInUserId.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    private val _authState = MutableStateFlow<Resource<UserEntity>?>(null)
    val authState = _authState.asStateFlow()

    fun register(fullName: String, password: String) {
        viewModelScope.launch {
            _authState.value = Resource.Loading
            _authState.value = userRepository.registerUser(fullName, password)
        }
    }

    fun login(trainerId: String, password: String) {
        viewModelScope.launch {
            _authState.value = Resource.Loading
            _authState.value = userRepository.login(trainerId, password)
        }
    }

    fun loginByBiometric(userId: String) {
        viewModelScope.launch {
            _authState.value = Resource.Loading
            _authState.value = userRepository.loginByBiometric(userId)
        }
    }

    fun resetAuthState() {
        _authState.value = null
    }

    fun updateBiometric(userId: String, isUsingBiometric: Boolean, biometricKey: String) {
        viewModelScope.launch {
            userRepository.updateBiometricSetup(userId, isUsingBiometric, biometricKey)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentUserProfile = userRepository.activeUserId.flatMapLatest { userId ->
        if (userId != null) {
            flow {
                emit(userRepository.getUserProfile(userId))
            }
        } else {
            flowOf<Resource<UserEntity>?>(null)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )
}