package com.alicodex.nikishop.data.repo.order

import com.alicodex.nikishop.data.Checkout
import com.alicodex.nikishop.data.SubmitOrderResult
import io.reactivex.Single

class OrderRepositoryImpl(val orderDataSource: OrderDataSource) : OrderRepository {
    override fun submit(
        firstName: String,
        lastName: String,
        postalCode: String,
        phoneNumber: String,
        address: String,
        paymentMethod: String
    ): Single<SubmitOrderResult> {
        return orderDataSource.submit(
            firstName,
            lastName,
            postalCode,
            phoneNumber,
            address,
            paymentMethod
        )
    }

    override fun checkout(orderId: Int): Single<Checkout> {
        return orderDataSource.checkout(orderId)
    }
}