package com.alicodex.nikishop.feature.product

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alicodex.nikishop.R
import com.google.android.material.snackbar.Snackbar
import com.alicodex.nikishop.common.EXTRA_KEY_ID
import com.alicodex.nikishop.common.NikeActivity
import com.alicodex.nikishop.common.NikeCompletableObserver
import com.alicodex.nikishop.common.formatPrice
import com.alicodex.nikishop.data.Comment
import com.alicodex.nikishop.databinding.ActivityProductDetailBinding
import com.alicodex.nikishop.feature.ProductDetailViewModel
import com.alicodex.nikishop.feature.product.comment.CommentListActivity
import com.alicodex.nikishop.services.ImageLoadingService
import com.alicodex.nikishop.view.scroll.ObservableScrollViewCallbacks
import com.alicodex.nikishop.view.scroll.ScrollState
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class ProductDetailActivity : NikeActivity() {
    private lateinit var binding: ActivityProductDetailBinding
    val productDetailViewModel: ProductDetailViewModel by viewModel { parametersOf(intent.extras) }
    val imageLoadingService: ImageLoadingService by inject()
    val commentAdapter = CommentAdapter()
    val compositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        productDetailViewModel.productLiveData.observe(this) {
            imageLoadingService.load(binding.productIv, it.image)
            binding.titleTv.text = it.title
            binding.previousPriceTv.text = formatPrice(it.previous_price)
            binding.previousPriceTv.paintFlags=Paint.STRIKE_THRU_TEXT_FLAG
            binding.currentPriceTv.text = formatPrice(it.price)
            binding.toolbarTitleTv.text = it.title
        }

        productDetailViewModel.progressBarLiveData.observe(this){
            setProgressIndicator(it)
        }

        productDetailViewModel.commentsLiveData.observe(this) {
            Timber.i(it.toString())
            commentAdapter.comments = it as ArrayList<Comment>
            if (it.size > 3) {
                binding.viewAllCommentsBtn.visibility = View.VISIBLE
                binding.viewAllCommentsBtn.setOnClickListener {
                    startActivity(Intent(this, CommentListActivity::class.java).apply {
                        putExtra(EXTRA_KEY_ID, productDetailViewModel.productLiveData.value!!.id)
                    })
                }
            }
        }

        initViews()

    }

    fun initViews() {
        binding.commentsRv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.commentsRv.adapter = commentAdapter
        binding.commentsRv.isNestedScrollingEnabled = false

        binding.productIv.post {
            val productIvHeight = binding.productIv.height
            val toolbar = binding.toolbarView
            val productImageView = binding.productIv
            binding.observableScrollView.addScrollViewCallbacks(object : ObservableScrollViewCallbacks {
                override fun onScrollChanged(
                    scrollY: Int,
                    firstScroll: Boolean,
                    dragging: Boolean
                ) {
                    Timber.i("productIv height is -> $productIvHeight")
                    toolbar.alpha = scrollY.toFloat() / productIvHeight.toFloat()
                    productImageView.translationY = scrollY.toFloat() / 2
                }

                override fun onDownMotionEvent() {
                }

                override fun onUpOrCancelMotionEvent(scrollState: ScrollState?) {
                }

            })
        }

        binding.addToCartBtn.setOnClickListener {
            productDetailViewModel.onAddToCartBtn()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :NikeCompletableObserver(compositeDisposable){
                    override fun onComplete() {
                      showSnackBar(getString(R.string.success_addToCart))
                    }
                })

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}