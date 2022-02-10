package com.alicodex.nikishop.services

import com.alicodex.nikishop.view.NikeImageView

interface ImageLoadingService {
    fun load(imageView: NikeImageView, imageUrl: String)
}