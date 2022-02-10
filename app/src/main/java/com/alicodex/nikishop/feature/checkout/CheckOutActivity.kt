@file:Suppress("DEPRECATION")

package com.alicodex.nikishop.feature.checkout

import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.alicodex.nikishop.R
import com.alicodex.nikishop.common.EXTRA_KEY_ID
import com.alicodex.nikishop.common.formatPrice
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

@Suppress("COMPATIBILITY_WARNING")
class CheckOutActivity : AppCompatActivity() {
    val viewModel: CheckoutViewModel by viewModel {
        val uri: Uri? = intent.data
        if (uri != null)
            parametersOf(uri.getQueryParameter("order_id")!!.toInt())
        else
            parametersOf(intent.extras!!.getInt(EXTRA_KEY_ID))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out)

        viewModel.checkoutLiveData.observe(this) {
            val orderPriceTv:TextView = findViewById(R.id.orderPriceTv)!!
            val orderStatusTv: TextView = findViewById(R.id.orderStatusTv)!!
            val purchaseStatusTv: TextView = findViewById(R.id.purchaseStatusTv)!!

            orderPriceTv.text = formatPrice(it.payable_price)
            orderStatusTv.text = it.payment_status
            purchaseStatusTv.text =
                if (it.purchase_success) "خرید با موفقیت انجام شد" else "خرید ناموفق"
        }
    }
}