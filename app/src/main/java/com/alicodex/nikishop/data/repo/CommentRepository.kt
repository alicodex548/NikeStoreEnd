package com.alicodex.nikishop.data.repo

import com.alicodex.nikishop.data.Comment
import io.reactivex.Single

interface CommentRepository {

    fun getAll(productId:Int): Single<List<Comment>>

    fun insert(): Single<Comment>
}