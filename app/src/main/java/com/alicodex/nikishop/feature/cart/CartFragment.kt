package com.alicodex.nikishop.feature.cart

import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.alicodex.nikishop.R
import com.google.android.material.button.MaterialButton
import com.alicodex.nikishop.common.EXTRA_KEY_DATA
import com.alicodex.nikishop.common.NikeCompletableObserver
import com.alicodex.nikishop.common.NikeFragment
import com.alicodex.nikishop.data.CartItem
import com.alicodex.nikishop.databinding.FragmentCartBinding
import com.alicodex.nikishop.databinding.ViewCartEmptyStateBinding
import com.alicodex.nikishop.feature.auth.AuthActivity
import com.alicodex.nikishop.feature.product.ProductDetailActivity
import com.alicodex.nikishop.feature.shipping.ShippingActivity
import com.alicodex.nikishop.services.ImageLoadingService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class CartFragment : NikeFragment(), CartItemAdapter.CartItemViewCallbacks {



    private var _binding: FragmentCartBinding? = null
    private var emptyS: ViewCartEmptyStateBinding? = null
    private var emptySV: ViewBinding? = null

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
    private val emptySBind get() = emptyS!!
    private val emptySVBind get() = emptySV!!

    val viewModel: CartViewModel by viewModel()
    var cartItemAdapter: CartItemAdapter? = null
    val imageLoadingService: ImageLoadingService by inject()
    val compositeDisposable = CompositeDisposable()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        emptyS = ViewCartEmptyStateBinding.inflate(inflater, container, false)
        return binding.root

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
        }

        viewModel.cartItemsLiveData.observe(viewLifecycleOwner) {
            Timber.i(it.toString())
            binding.cartItemsRv.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            cartItemAdapter =
                CartItemAdapter(it as MutableList<CartItem>, imageLoadingService, this)
            binding.cartItemsRv.adapter = cartItemAdapter
        }


        viewModel.emptyStateLiveData.observe(viewLifecycleOwner) {
            if (it.mustShow) {
                val emptyState = showEmptyState(R.layout.view_cart_empty_state)
                val emptyStateMessageTv: TextView = emptyState
                    ?.findViewById(R.id.emptyStateMessageTv)!!
                val emptyStateCtaBtn: MaterialButton = emptyState.findViewById(R.id.emptyStateCtaBtn)!!

                emptyState.let { _ ->
                    emptyStateMessageTv.text = getString(it.messageResId)
                    emptyStateCtaBtn.visibility =
                        if (it.mustShowCallToActionButton) View.VISIBLE else View.GONE
                    emptyStateCtaBtn.setOnClickListener {
                        startActivity(Intent(requireContext(), AuthActivity::class.java))
                    }
                }
            } else
                emptySBind.emptyStateRootView.visibility = View.GONE
        }
        binding.payBtn.setOnClickListener {
            startActivity(Intent(requireContext(),ShippingActivity::class.java).apply {
                putExtra(EXTRA_KEY_DATA,viewModel.purchaseDetailLiveData.value)
            })
        }
    }
    override fun onStart() {
        super.onStart()
        viewModel.refresh()
    }

    override fun onRemoveCartItemButtonClick(cartItem: CartItem) {
        viewModel.removeItemFromCart(cartItem)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                override fun onComplete() {
                    cartItemAdapter?.removeCartItem(cartItem)
                }
            })
    }

    override fun onIncreaseCartItemButtonClick(cartItem: CartItem) {
        viewModel.increaseCartItemCount(cartItem)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                override fun onComplete() {
                    cartItemAdapter?.increaseCount(cartItem)
                }
            })
    }

    override fun onDecreaseCartItemButtonClick(cartItem: CartItem) {
        viewModel.decreaseCartItemCount(cartItem)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                override fun onComplete() {
                    cartItemAdapter?.decreaseCount(cartItem)
                }
            })
    }

    override fun onProductImageClick(cartItem: CartItem) {
        startActivity(Intent(requireContext(), ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, cartItem.product)
        })
    }
}