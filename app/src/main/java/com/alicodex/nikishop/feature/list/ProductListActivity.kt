package com.alicodex.nikishop.feature.list

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import com.alicodex.nikishop.R
import com.alicodex.nikishop.R.layout.view_toolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.alicodex.nikishop.common.EXTRA_KEY_DATA
import com.alicodex.nikishop.common.NikeActivity
import com.alicodex.nikishop.data.Product
import com.alicodex.nikishop.databinding.ActivityProductListBinding
import com.alicodex.nikishop.feature.common.ProductListAdapter
import com.alicodex.nikishop.feature.common.VIEW_TYPE_LARGE
import com.alicodex.nikishop.feature.common.VIEW_TYPE_SMALL
import com.alicodex.nikishop.feature.product.ProductDetailActivity
import com.alicodex.nikishop.view.NikeToolbar
import com.google.android.material.card.MaterialCardView
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class ProductListActivity : NikeActivity(),ProductListAdapter.ProductEventListener {

    lateinit var binding: ActivityProductListBinding
    val lay = layoutInflater.inflate(R.layout.view_toolbar, null)


    val viewModel: ProductListViewModel by viewModel {
        parametersOf(
            intent.extras!!.getInt(
                EXTRA_KEY_DATA
            )
        )
    }

    val productListAdapter: ProductListAdapter by inject { parametersOf(VIEW_TYPE_SMALL) }


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.getProducts()
        val gridLayoutManager = GridLayoutManager(this, 2)

        if (::binding.isInitialized)
            binding.productsRv.layoutManager = gridLayoutManager
        if (::binding.isInitialized)
            binding.productsRv.adapter = productListAdapter
        productListAdapter.productEventListener = this
        if (::binding.isInitialized)
            binding.viewTypeChangerBtn.setOnClickListener {
                if (productListAdapter.viewType == VIEW_TYPE_SMALL) {
                    if (::binding.isInitialized)
                        binding.viewTypeChangerBtn.setImageResource(R.drawable.ic_view_type_large)
                    productListAdapter.viewType = VIEW_TYPE_LARGE
                    gridLayoutManager.spanCount = 1
                    productListAdapter.notifyDataSetChanged()
                } else {

                    if (::binding.isInitialized)
                        binding.viewTypeChangerBtn.setImageResource(R.drawable.ic_grid)
                    productListAdapter.viewType = VIEW_TYPE_SMALL
                    gridLayoutManager.spanCount = 2
                    productListAdapter.notifyDataSetChanged()
                }
            }
        viewModel.selectedSortTitleLiveData.observe(this) {
            if (::binding.isInitialized)
                binding.selectedSortTitleTv.text = getString(it)
        }

        viewModel.progressBarLiveData.observe(this) {
            setProgressIndicator(it)
        }
        viewModel.productLiveData.observe(this) {
            Timber.i(it.toString())
            productListAdapter.products = it as ArrayList<Product>
        }

        val toolbarView = findViewById<NikeToolbar>(R.id.toolbarView)!!
        toolbarView.onBackButtonClickListener = View.OnClickListener {
                finish()
            }



        if (::binding.isInitialized)
            binding.sortBtn.setOnClickListener {
                val dialog = MaterialAlertDialogBuilder(this)
                    .setSingleChoiceItems(
                        R.array.sortTitlesArray, viewModel.sort
                    ) { dialog, selectedSortIndex ->
                        dialog.dismiss()
                        viewModel.onSelectedSortChangedByUser(selectedSortIndex)
                    }.setTitle(getString(R.string.sort))

                dialog.show()
            }


    }

    override fun onProductClick(product: Product) {
        startActivity(Intent(this,ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA,product)
        })
    }

    override fun onFavoriteBtnClick(product: Product) {

    }
}