package com.alicodex.nikishop.feature.auth

import com.alicodex.nikishop.common.NikeViewModel
import com.alicodex.nikishop.data.repo.UserRepository
import io.reactivex.Completable

class AuthViewModel(private val userRepository: UserRepository) : NikeViewModel() {

    fun login(email: String, password: String): Completable {
        progressBarLiveData.value = true
        return userRepository.login(email, password).doFinally {
            progressBarLiveData.postValue(false)
        }
    }

    fun signUp(email: String, password: String): Completable {
        progressBarLiveData.value = true
        return userRepository.signUp(email, password).doFinally {
            progressBarLiveData.postValue(false)
        }
    }
}