package com.alicodex.nikishop.data.repo.source

import com.alicodex.nikishop.data.Product
import io.reactivex.Completable
import io.reactivex.Single

interface ProductDataSource {
    fun getProducts(sort:Int): Single<List<Product>>

    fun getFavoriteProducts(): Single<List<Product>>

    fun addToFavorites(product: Product): Completable

    fun deleteFromFavorites(product: Product): Completable
}