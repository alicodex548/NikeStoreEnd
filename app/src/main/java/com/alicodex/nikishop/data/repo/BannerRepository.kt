package com.alicodex.nikishop.data.repo

import com.alicodex.nikishop.data.Banner
import io.reactivex.Single

interface BannerRepository {
    fun getBanners():Single<List<Banner>>
}