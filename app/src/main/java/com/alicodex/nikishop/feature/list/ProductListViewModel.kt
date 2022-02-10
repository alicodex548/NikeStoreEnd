package com.alicodex.nikishop.feature.list

import androidx.lifecycle.MutableLiveData
import com.alicodex.nikishop.R
import com.alicodex.nikishop.common.NikeSingleObserver
import com.alicodex.nikishop.common.NikeViewModel
import com.alicodex.nikishop.common.asyncNetworkRequest
import com.alicodex.nikishop.data.Product
import com.alicodex.nikishop.data.repo.ProductRepository

class ProductListViewModel(var sort:Int,val productRepository: ProductRepository):

    NikeViewModel() {

    val productLiveData = MutableLiveData<List<Product>>()
    val selectedSortTitleLiveData = MutableLiveData<Int>()
    val sortTitles = arrayOf(
        R.string.sortLatest,
        R.string.sortPopular,
        R.string.sortPriceHighToLow,
        R.string.sortPriceLowToHigh
    )

    init {
            getProducts()
        selectedSortTitleLiveData.value = sortTitles[sort]
    }


    fun getProducts() {
        progressBarLiveData.value = true
        productRepository.getProducts(sort)
            .asyncNetworkRequest()
            .doFinally { progressBarLiveData.value = false }
            .subscribe(object : NikeSingleObserver<List<Product>>(compositeDisposable) {
                override fun onSuccess(t: List<Product>) {
                    productLiveData.value = t
                }


            })
    }

    fun onSelectedSortChangedByUser(sort:Int){
        this.sort = sort
        this.selectedSortTitleLiveData.value = sortTitles[sort]
        getProducts()
    }
}