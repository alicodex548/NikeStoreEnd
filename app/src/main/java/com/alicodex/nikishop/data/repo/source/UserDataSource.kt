package com.alicodex.nikishop.data.repo.source

import com.alicodex.nikishop.data.MessageResponse
import com.alicodex.nikishop.data.TokenResponse
import io.reactivex.Single

interface UserDataSource {

    fun login(username: String, password: String): Single<TokenResponse>
    fun signUp(username: String, password: String): Single<MessageResponse>
    fun loadToken()
    fun saveToken(token: String, refreshToken: String)
    fun saveUsername(username: String)
    fun getUsername(): String
    fun signOut()
}