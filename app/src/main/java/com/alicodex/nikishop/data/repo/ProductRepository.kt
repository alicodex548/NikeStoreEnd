package com.alicodex.nikishop.data.repo

import com.alicodex.nikishop.data.Product
import io.reactivex.Completable
import io.reactivex.Single

interface ProductRepository {

    fun getProducts(sort:Int): Single<List<Product>>

    fun getFavoriteProducts(): Single<List<Product>>

    fun addToFavorites(product: Product): Completable

    fun deleteFromFavorites(product: Product): Completable
}