package com.alicodex.nikishop.feature.profile

import com.alicodex.nikishop.common.NikeViewModel
import com.alicodex.nikishop.data.TokenContainer
import com.alicodex.nikishop.data.repo.UserRepository

class ProfileViewModel(private val userRepository: UserRepository) : NikeViewModel() {
    val username: String
        get() = userRepository.getUserName()

    val isSignedIn: Boolean
        get() = TokenContainer.token != null

    fun signOut() = userRepository.signOut()
}