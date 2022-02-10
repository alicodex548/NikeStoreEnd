package com.alicodex.nikishop.data.repo

import com.alicodex.nikishop.data.AddToCartResponse
import com.alicodex.nikishop.data.CartItemCount
import com.alicodex.nikishop.data.CartResponse
import com.alicodex.nikishop.data.MessageResponse
import com.alicodex.nikishop.data.repo.source.CartDataSource
import io.reactivex.Single

class CartRepositoryImpl(val remoteDataSource: CartDataSource) : CartRepository {
    override fun addToCart(productId: Int): Single<AddToCartResponse> =
        remoteDataSource.addToCart(productId)

    override fun get(): Single<CartResponse> =remoteDataSource.get()

    override fun remove(cartItemId: Int): Single<MessageResponse> =remoteDataSource.remove(cartItemId)

    override fun changeCount(cartItemId: Int, count: Int): Single<AddToCartResponse> = remoteDataSource.changeCount(cartItemId,count)

    override fun getCartItemsCount(): Single<CartItemCount> =remoteDataSource.getCartItemsCount()
}