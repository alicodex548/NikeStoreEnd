package com.alicodex.nikishop.feature.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alicodex.nikishop.R
import io.reactivex.disposables.CompositeDisposable
import com.alicodex.nikishop.common.NikeCompletableObserver
import com.alicodex.nikishop.databinding.FragmentLoginBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    val viewModel: AuthViewModel by viewModel()
    val compositeDisposable = CompositeDisposable()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentLoginBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        if (::binding.isInitialized)
            binding.loginBtn.setOnClickListener {
                viewModel.login(binding.emailEt.text.toString(), binding.passwordEt.text.toString())
                    .subscribeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                        override fun onComplete() {
                            requireActivity().finish()
                        }
                    })
            }
        if (::binding.isInitialized)

            binding.signUpLinkBtn.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainer, SignUpFragment())
                }.commit()
            }
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }
}