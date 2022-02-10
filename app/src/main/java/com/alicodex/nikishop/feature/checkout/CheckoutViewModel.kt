package com.alicodex.nikishop.feature.checkout

import androidx.lifecycle.MutableLiveData
import com.alicodex.nikishop.common.NikeSingleObserver
import com.alicodex.nikishop.common.NikeViewModel
import com.alicodex.nikishop.common.asyncNetworkRequest
import com.alicodex.nikishop.data.Checkout
import com.alicodex.nikishop.data.repo.order.OrderRepository

class CheckoutViewModel(orderId: Int, orderRepository: OrderRepository) :
    NikeViewModel() {
    val checkoutLiveData = MutableLiveData<Checkout>()

    init {
        orderRepository.checkout(orderId)
            .asyncNetworkRequest()
            .subscribe(object : NikeSingleObserver<Checkout>(compositeDisposable) {
                override fun onSuccess(t: Checkout) {
                    checkoutLiveData.value = t
                }
            })
    }
}