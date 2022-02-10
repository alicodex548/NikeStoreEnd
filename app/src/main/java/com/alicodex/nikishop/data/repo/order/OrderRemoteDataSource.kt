package com.alicodex.nikishop.data.repo.order

import com.google.gson.JsonObject
import com.alicodex.nikishop.data.Checkout
import com.alicodex.nikishop.data.SubmitOrderResult
import com.alicodex.nikishop.services.http.ApiService
import io.reactivex.Single

class OrderRemoteDataSource(val apiService: ApiService) : OrderDataSource {
    override fun submit(
        firstName: String,
        lastName: String,
        postalCode: String,
        phoneNumber: String,
        address: String,
        paymentMethod: String
    ): Single<SubmitOrderResult> {
        return apiService.submitOrder(JsonObject().apply {
            addProperty("first_name", firstName)
            addProperty("last_name", lastName)
            addProperty("postal_code", postalCode)
            addProperty("mobile", phoneNumber)
            addProperty("address", address)
            addProperty("payment_method", paymentMethod)
        })
    }

    override fun checkout(orderId: Int): Single<Checkout> {
        return apiService.checkout(orderId)
    }
}