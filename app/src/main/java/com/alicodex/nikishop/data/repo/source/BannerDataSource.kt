package com.alicodex.nikishop.data.repo.source

import com.alicodex.nikishop.data.Banner
import io.reactivex.Single

interface BannerDataSource {
    fun getBanners():Single<List<Banner>>
}