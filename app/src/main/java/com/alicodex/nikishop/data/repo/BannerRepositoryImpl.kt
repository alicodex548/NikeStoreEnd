package com.alicodex.nikishop.data.repo

import com.alicodex.nikishop.data.Banner
import com.alicodex.nikishop.data.repo.source.BannerDataSource
import io.reactivex.Single

class BannerRepositoryImpl(val bannerRemoteDataSource: BannerDataSource) : BannerRepository {
    override fun getBanners(): Single<List<Banner>> = bannerRemoteDataSource.getBanners()
}