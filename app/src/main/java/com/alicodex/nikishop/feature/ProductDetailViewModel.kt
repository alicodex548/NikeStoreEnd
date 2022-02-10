package com.alicodex.nikishop.feature

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.alicodex.nikishop.common.EXTRA_KEY_DATA
import com.alicodex.nikishop.common.NikeSingleObserver
import com.alicodex.nikishop.common.NikeViewModel
import com.alicodex.nikishop.common.asyncNetworkRequest
import com.alicodex.nikishop.data.Comment
import com.alicodex.nikishop.data.Product
import com.alicodex.nikishop.data.repo.CartRepository
import com.alicodex.nikishop.data.repo.CommentRepository
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers



class ProductDetailViewModel(
    bundle: Bundle, commentRepository: CommentRepository,
    val cartRepository: CartRepository
) :
    NikeViewModel() {
    val productLiveData = MutableLiveData<Product>()
    val commentsLiveData = MutableLiveData<List<Comment>>()

    init {
        productLiveData.value = bundle.getParcelable(EXTRA_KEY_DATA)
        progressBarLiveData.value = true
        commentRepository.getAll(productLiveData.value!!.id)
            .asyncNetworkRequest()
            .doFinally { progressBarLiveData.value = false }
            .subscribe(object : NikeSingleObserver<List<Comment>>(compositeDisposable) {
                override fun onSuccess(t: List<Comment>) {
                    commentsLiveData.value = t
                }
            })
    }

    fun onAddToCartBtn(): Completable = cartRepository.addToCart(productLiveData.value!!.id).ignoreElement()


}