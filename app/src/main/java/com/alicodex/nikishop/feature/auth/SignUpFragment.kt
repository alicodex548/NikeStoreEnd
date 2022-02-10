package com.alicodex.nikishop.feature.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alicodex.nikishop.R
import com.alicodex.nikishop.common.NikeCompletableObserver
import com.alicodex.nikishop.databinding.FragmentSignUpBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    val compositeDisposable = CompositeDisposable()
    val viewModel: AuthViewModel by inject()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentSignUpBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
            binding.signUpBtn.setOnClickListener {
                viewModel.signUp(
                    binding.emailEt.text.toString(),
                    binding.passwordEt.text.toString()
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                        override fun onComplete() {
                            requireActivity().finish()
                        }
                    })
            }
            binding.loginLinkBtn.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainer, LoginFragment())
                }.commit()
            }
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }
}