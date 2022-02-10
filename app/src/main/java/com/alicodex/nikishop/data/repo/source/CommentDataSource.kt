package com.alicodex.nikishop.data.repo.source

import com.alicodex.nikishop.data.Comment
import io.reactivex.Single

interface CommentDataSource {

    fun getAll(productId:Int): Single<List<Comment>>

    fun insert(): Single<Comment>
}