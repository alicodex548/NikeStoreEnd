package com.alicodex.nikishop.feature.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alicodex.nikishop.common.EXTRA_KEY_DATA
import com.alicodex.nikishop.common.NikeFragment
import com.alicodex.nikishop.common.convertDpToPixel
import com.alicodex.nikishop.data.Product
import com.alicodex.nikishop.data.SORT_LATEST
import com.alicodex.nikishop.databinding.FragmentHomeBinding
import com.alicodex.nikishop.feature.main.BannerSliderAdapter
import com.alicodex.nikishop.feature.common.ProductListAdapter
import com.alicodex.nikishop.feature.common.VIEW_TYPE_ROUND
import com.alicodex.nikishop.feature.list.ProductListActivity
import com.alicodex.nikishop.feature.product.ProductDetailActivity
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class HomeFragment : NikeFragment(), ProductListAdapter.ProductEventListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    val homeViewModel: HomeViewModel by viewModel()
    val productListAdapter: ProductListAdapter by inject { parametersOf(VIEW_TYPE_ROUND) }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // binding = FragmentHomeBinding.inflate(layoutInflater)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding.latestProductsRv.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.latestProductsRv.adapter = productListAdapter
        productListAdapter.productEventListener = this

        homeViewModel.productsLiveData.observe(viewLifecycleOwner) {
            Timber.i(it.toString())
            productListAdapter.products = it as ArrayList<Product>
        }
        binding.viewLatestProductsBtn.setOnClickListener {
            startActivity(Intent(requireContext(), ProductListActivity::class.java).apply {
                putExtra(EXTRA_KEY_DATA, SORT_LATEST)
            })
        }

        homeViewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
        }

        homeViewModel.bannersLiveData.observe(viewLifecycleOwner) {
            Timber.i(it.toString())
            val bannerSliderAdapter = BannerSliderAdapter(this, it)
            binding.bannerSliderViewPager.adapter = bannerSliderAdapter
            val viewPagerHeight = (((binding.bannerSliderViewPager.measuredWidth - convertDpToPixel(
                32f,
                requireContext()
            )) * 173) / 328).toInt()

            val layoutParams = binding.bannerSliderViewPager.layoutParams
            layoutParams.height = viewPagerHeight
            binding.bannerSliderViewPager.layoutParams = layoutParams
            binding.sliderIndicator.setViewPager2(binding.bannerSliderViewPager)

        }

    }

    override fun onProductClick(product: Product) {
        startActivity(Intent(requireContext(), ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, product)
        })
    }

    override fun onFavoriteBtnClick(product: Product) {
        homeViewModel.addProductToFavorites(product)
    }
}