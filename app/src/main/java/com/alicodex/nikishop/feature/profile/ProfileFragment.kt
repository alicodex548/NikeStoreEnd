package com.alicodex.nikishop.feature.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.alicodex.nikishop.R
import com.google.android.material.button.MaterialButton
import com.alicodex.nikishop.common.NikeFragment
import com.alicodex.nikishop.feature.auth.AuthActivity
import com.alicodex.nikishop.feature.favorites.FavoriteProductsActivity
import com.alicodex.nikishop.feature.profile.ProfileViewModel
import org.koin.android.ext.android.inject

class ProfileFragment : NikeFragment() {
    private val viewModel: ProfileViewModel by inject()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val favoriteProductsBtn: TextView = view.findViewById(R.id.favoriteProductsBtn)!!

        favoriteProductsBtn.setOnClickListener {
            startActivity(Intent(requireContext(), FavoriteProductsActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        checkAuthState()
    }

    private fun checkAuthState() {
        if (viewModel.isSignedIn) {
            val authBtn: TextView = view?.findViewById(R.id.authBtn)!!
            val usernameTv: TextView = view?.findViewById(R.id.usernameTv)!!

            authBtn.text = getString(R.string.signOut)
            authBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sign_out, 0)
            usernameTv.text = viewModel.username
            authBtn.setOnClickListener {
                viewModel.signOut()
                checkAuthState()
            }
        } else {
            val authBtn: TextView = view?.findViewById(R.id.authBtn)!!
            authBtn.text = getString(R.string.signIn)
            authBtn.setOnClickListener {
                startActivity(Intent(requireContext(), AuthActivity::class.java))
            }
            authBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sign_in, 0)
            val usernameTv: TextView = view?.findViewById(R.id.usernameTv)!!
            usernameTv.text = getString(R.string.guest_user)
        }
    }
}