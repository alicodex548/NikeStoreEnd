package com.alicodex.nikishop

import android.app.Application
import android.content.SharedPreferences
import android.os.Bundle
import androidx.room.Room
import com.facebook.drawee.backends.pipeline.Fresco
import com.alicodex.nikishop.data.db.AppDatabase
import com.alicodex.nikishop.data.repo.*
import com.alicodex.nikishop.data.repo.order.OrderRemoteDataSource
import com.alicodex.nikishop.data.repo.order.OrderRepository
import com.alicodex.nikishop.data.repo.order.OrderRepositoryImpl
import com.alicodex.nikishop.data.repo.source.*
import com.alicodex.nikishop.feature.ProductDetailViewModel
import com.alicodex.nikishop.feature.auth.AuthViewModel
import com.alicodex.nikishop.feature.cart.CartViewModel
import com.alicodex.nikishop.feature.checkout.CheckoutViewModel
import com.alicodex.nikishop.feature.common.ProductListAdapter
import com.alicodex.nikishop.feature.favorites.FavoriteProductsViewModel
import com.alicodex.nikishop.feature.list.ProductListViewModel
import com.alicodex.nikishop.feature.home.HomeViewModel
import com.alicodex.nikishop.feature.main.MainViewModel
import com.alicodex.nikishop.feature.product.comment.CommentListViewModel
import com.alicodex.nikishop.feature.profile.ProfileViewModel
import com.alicodex.nikishop.feature.shipping.ShippingViewModel
import com.alicodex.nikishop.services.FrescoImageLoadingService
import com.alicodex.nikishop.services.ImageLoadingService
import com.alicodex.nikishop.services.http.createApiServiceInstance
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        Fresco.initialize(this)

        val myModules = module {
            single { createApiServiceInstance() }
            single<ImageLoadingService> { FrescoImageLoadingService() }
            single { Room.databaseBuilder(this@App,AppDatabase::class.java,"db_app").build() }
            factory<ProductRepository> {
                ProductRepositoryImpl(
                    ProductRemoteDataSource(get()),
                    get<AppDatabase>().productDao()
                )
            }

            single<SharedPreferences> {
                this@App.getSharedPreferences(
                    "app_settings",
                    MODE_PRIVATE
                )
            }
            single { UserLocalDataSource(get()) }
            single<UserRepository> {
                UserRepositoryImpl(
                    UserLocalDataSource(get()),
                    UserRemoteDataSource(get())
                )
            }
            single<OrderRepository> { OrderRepositoryImpl(OrderRemoteDataSource(get())) }

            factory { (viewType: Int) -> ProductListAdapter(viewType, get()) }
            factory<BannerRepository> { BannerRepositoryImpl(BannerRemoteDataSource(get())) }
            factory<CommentRepository> { CommentRepositoryImpl(CommentRemoteDataSource(get())) }
            factory<CartRepository> { CartRepositoryImpl(CartRemoteDataSource(get())) }
            viewModel { HomeViewModel(get(), get()) }
            viewModel { (bundle: Bundle) -> ProductDetailViewModel(bundle, get(), get()) }
            viewModel { (productId: Int) -> CommentListViewModel(productId, get()) }
            viewModel { (sort: Int) -> ProductListViewModel(sort, get()) }
            viewModel { AuthViewModel(get()) }
            viewModel { CartViewModel(get()) }
            viewModel { MainViewModel(get()) }
            viewModel { ShippingViewModel(get()) }
            viewModel { (orderId: Int) -> CheckoutViewModel(orderId, get()) }
            viewModel { ProfileViewModel(get()) }
            viewModel { FavoriteProductsViewModel(get()) }
        }

        startKoin {
            androidContext(this@App)
            modules(myModules)
        }

        val userRepository: UserRepository = get()
        userRepository.loadToken()
    }
}