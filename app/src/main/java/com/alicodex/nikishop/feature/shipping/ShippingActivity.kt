package com.alicodex.nikishop.feature.shipping

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.alicodex.nikishop.common.EXTRA_KEY_DATA
import com.alicodex.nikishop.data.PurchaseDetail
import com.alicodex.nikishop.feature.cart.CartItemAdapter
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.viewmodel.ext.android.viewModel
import org.w3c.dom.Text
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
//------
import android.content.Intent
import com.alicodex.nikishop.R
import com.google.android.material.button.MaterialButton
import com.alicodex.nikishop.common.EXTRA_KEY_ID
import com.alicodex.nikishop.common.NikeSingleObserver
import com.alicodex.nikishop.common.openUrlInCustomTab
import com.alicodex.nikishop.data.SubmitOrderResult
import com.alicodex.nikishop.databinding.ActivityShippingBinding
import com.alicodex.nikishop.feature.checkout.CheckOutActivity

class ShippingActivity : AppCompatActivity() {
    lateinit var binding: ActivityShippingBinding
    val viewModel:ShippingViewModel by viewModel()
    val compositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shipping)

        val purchaseDetail = intent.getParcelableExtra<PurchaseDetail>(EXTRA_KEY_DATA)
            ?: throw IllegalAccessException("purchase detail cannot be null")
        val purchaseDetailView: LinearLayout = findViewById(R.id.purchaseDetailView)!!

        val viewHolder = CartItemAdapter.PurchaseDetailViewHolder(purchaseDetailView)
        viewHolder.bind(
            purchaseDetail.totalPrice,
            purchaseDetail.shipping_cost,
            purchaseDetail.payable_price
        )
        val firstNameEt: TextView = findViewById(R.id.firstNameEt)!!
        val lastNameEt: TextView = findViewById(R.id.lastNameEt)!!
        val phoneNumberEt: TextInputEditText = findViewById(R.id.phoneNumberEt)!!
        val addressEt: TextInputEditText = findViewById(R.id.addressEt)!!
        val postalCodeEt: TextInputEditText = findViewById(R.id.postalCodeEt)!!
        val onClickListener = View.OnClickListener {
            viewModel.submitOrder(
                firstNameEt.text.toString(),
                lastNameEt.text.toString(),
                postalCodeEt.text.toString(),
                phoneNumberEt.text.toString(),
                addressEt.text.toString(),
                if (it.id == R.id.onlinePaymentBtn) PAYMENT_METHOD_ONLINE else PAYMENT_METHOD_COD
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : NikeSingleObserver<SubmitOrderResult>(compositeDisposable) {
                    override fun onSuccess(t: SubmitOrderResult) {
                        if (t.bank_gateway_url.isNotEmpty()) {
                            openUrlInCustomTab(this@ShippingActivity, t.bank_gateway_url)
                        } else {
                            startActivity(
                                Intent(
                                    this@ShippingActivity,
                                    CheckOutActivity::class.java
                                ).apply {
                                    putExtra(EXTRA_KEY_ID, t.order_id)
                                }
                            )
                        }
                        finish()

                    }
                })
        }
        val onlinePaymentBtn: MaterialButton = findViewById(R.id.onlinePaymentBtn)
        val codBtn: MaterialButton = findViewById(R.id.codBtn)

        onlinePaymentBtn.setOnClickListener(onClickListener)
        codBtn.setOnClickListener(onClickListener)
    }
}