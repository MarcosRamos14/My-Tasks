package com.marcos.mytasks.ui.authorization.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.marcos.mytasks.R
import com.marcos.mytasks.databinding.FragmentLoginBinding
import com.marcos.mytasks.ui.extension.showBottomSheet
import com.marcos.mytasks.ui.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentLoginBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
        setupListenerLoginUser()
        observerLoginUserUiState()
    }

    private fun setupListener() {
        binding.loginBtnCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.loginBtnRecoverAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_recoverAccountFragment)
        }
    }

    private fun setupListenerLoginUser() {
        binding.loginBtnLogin.setOnClickListener {
            validateFieldForLogin()
        }
    }

    private fun validateFieldForLogin() {
        val email = binding.loginEditEmail.text.toString().trim()
        val password = binding.loginEditPassword.text.toString().trim()

        if (email.isNotEmpty()) {
            if (password.isNotEmpty()) {
                hideKeyboard()
                viewModel.loginUser(email, password)
            } else showBottomSheet(message = R.string.app_message_password)
        } else showBottomSheet(message = R.string.app_message_email)
    }

    private fun observerLoginUserUiState() {
        viewModel.state.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is LoginViewModel.UiState.Success -> {
                    findNavController().navigate(R.id.action_global_homeFragment)
                }
                is LoginViewModel.UiState.Error -> {
                    showBottomSheet(message = uiState.errorMessage)
                }
            }
        }
    }
}