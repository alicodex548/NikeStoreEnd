package com.alicodex.nikishop.data.repo.source

import com.alicodex.nikishop.data.Comment
import com.alicodex.nikishop.services.http.ApiService
import io.reactivex.Single

class CommentRemoteDataSource(val apiService: ApiService):CommentDataSource {
    override fun getAll(productId:Int): Single<List<Comment>> = apiService.getComments(productId)

    override fun insert(): Single<Comment> {
        TODO("Not yet implemented")
    }
}