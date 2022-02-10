package com.alicodex.nikishop.feature.product.comment

import androidx.lifecycle.MutableLiveData
import com.alicodex.nikishop.common.NikeSingleObserver
import com.alicodex.nikishop.common.NikeViewModel
import com.alicodex.nikishop.common.asyncNetworkRequest
import com.alicodex.nikishop.data.Comment
import com.alicodex.nikishop.data.repo.CommentRepository

class CommentListViewModel(productId: Int, commentRepository: CommentRepository) : NikeViewModel() {
    val commentsLiveData = MutableLiveData<List<Comment>>()

    init {
        progressBarLiveData.value = true
        commentRepository.getAll(productId)
            .asyncNetworkRequest()
            .doFinally { progressBarLiveData.value = false }
            .subscribe(object : NikeSingleObserver<List<Comment>>(compositeDisposable) {
                override fun onSuccess(t: List<Comment>) {
                    commentsLiveData.value = t
                }
            })
    }
}