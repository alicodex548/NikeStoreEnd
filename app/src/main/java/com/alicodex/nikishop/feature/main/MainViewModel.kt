package com.alicodex.nikishop.feature.main

import com.alicodex.nikishop.common.NikeSingleObserver
import com.alicodex.nikishop.common.NikeViewModel
import com.alicodex.nikishop.data.CartItemCount
import com.alicodex.nikishop.data.TokenContainer
import com.alicodex.nikishop.data.repo.CartRepository
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus

class MainViewModel(private val cartRepository: CartRepository) : NikeViewModel() {
    fun getCartItemsCount(){
        if (!TokenContainer.token.isNullOrEmpty()){
            cartRepository.getCartItemsCount()
                .subscribeOn(Schedulers.io())
                .subscribe(object :NikeSingleObserver<CartItemCount>(compositeDisposable){
                    override fun onSuccess(t: CartItemCount) {
                        EventBus.getDefault().postSticky(t)
                    }
                })
        }
    }
}