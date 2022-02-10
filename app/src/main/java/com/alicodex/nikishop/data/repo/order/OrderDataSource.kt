package com.alicodex.nikishop.data.repo.order

import com.alicodex.nikishop.data.Checkout
import com.alicodex.nikishop.data.SubmitOrderResult
import io.reactivex.Single

interface OrderDataSource {

    fun submit(
        firstName: String,
        lastName: String,
        postalCode: String,
        phoneNumber: String,
        address: String,
        paymentMethod: String
    ): Single<SubmitOrderResult>

    fun checkout(orderId: Int): Single<Checkout>
}