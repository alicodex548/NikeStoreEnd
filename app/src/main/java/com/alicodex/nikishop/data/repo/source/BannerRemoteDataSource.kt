package com.alicodex.nikishop.data.repo.source

import com.alicodex.nikishop.data.Banner
import com.alicodex.nikishop.services.http.ApiService
import io.reactivex.Single

class BannerRemoteDataSource(val apiService: ApiService) : BannerDataSource {
    override fun getBanners(): Single<List<Banner>> = apiService.getBanners()
}