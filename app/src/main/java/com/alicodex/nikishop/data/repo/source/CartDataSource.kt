package com.alicodex.nikishop.data.repo.source

import com.alicodex.nikishop.data.AddToCartResponse
import com.alicodex.nikishop.data.CartItemCount
import com.alicodex.nikishop.data.CartResponse
import com.alicodex.nikishop.data.MessageResponse
import io.reactivex.Single

interface CartDataSource {

    fun addToCart(productId: Int): Single<AddToCartResponse>
    fun get(): Single<CartResponse>
    fun remove(cartItemId: Int): Single<MessageResponse>
    fun changeCount(cartItemId: Int, count: Int): Single<AddToCartResponse>
    fun getCartItemsCount(): Single<CartItemCount>
}