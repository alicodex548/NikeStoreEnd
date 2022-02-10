package com.alicodex.nikishop.data.repo

import com.alicodex.nikishop.data.TokenContainer
import com.alicodex.nikishop.data.TokenResponse
import com.alicodex.nikishop.data.repo.source.UserDataSource
import io.reactivex.Completable

class UserRepositoryImpl(
    val userLocalDataSource: UserDataSource,
    val userRemoteDataSource: UserDataSource
) : UserRepository {
    override fun login(username: String, password: String): Completable {
        return userRemoteDataSource.login(username, password).doOnSuccess {
            onSuccessfulLogin(username, it)
        }.ignoreElement()
    }

    override fun signUp(username: String, password: String): Completable {
        return userRemoteDataSource.signUp(username, password).flatMap {
            userRemoteDataSource.login(username, password)
        }.doOnSuccess {
            onSuccessfulLogin(username, it)
        }.ignoreElement()
    }

    override fun loadToken() {
        userLocalDataSource.loadToken()
    }

    override fun getUserName(): String = userLocalDataSource.getUsername()
    override fun signOut() {
        userLocalDataSource.signOut()
        TokenContainer.update(null, null)
    }

    fun onSuccessfulLogin(username: String, tokenResponse: TokenResponse) {
        TokenContainer.update(tokenResponse.access_token, tokenResponse.refresh_token)
        userLocalDataSource.saveToken(tokenResponse.access_token, tokenResponse.refresh_token)
        userLocalDataSource.saveUsername(username)
    }
}