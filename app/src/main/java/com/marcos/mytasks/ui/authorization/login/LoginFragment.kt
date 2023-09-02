package com.marcos.mytasks.ui.authorization.login

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.marcos.mytasks.R
import com.marcos.mytasks.databinding.FragmentLoginBinding
import com.marcos.mytasks.ui.extension.showBottomSheet
import com.marcos.mytasks.ui.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var googleSignInOptions: GoogleSignInOptions

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentLoginBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
        observerLoginUserUiState()
    }

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { resultActivity: ActivityResult ->
        if (resultActivity.resultCode == Activity.RESULT_OK) {
            val data = resultActivity.data
            val account = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val result = account.getResult(ApiException::class.java)
                val credentials = GoogleAuthProvider.getCredential(result.idToken, null)
                viewModel.signInGoogle(credentials)
            } catch (exception: ApiException) {
                val errorMessage = when (exception.statusCode) {
                    GoogleSignInStatusCodes.SIGN_IN_CANCELLED -> R.string.google_login_cancelled
                    GoogleSignInStatusCodes.SIGN_IN_FAILED -> R.string.google_error_login
                    else -> R.string.generic_error
                }
                showBottomSheet(message = errorMessage)
            }
        }
    }

    private fun setupListener() {
        with(binding) {
            loginBtnLogin.setOnClickListener {
                validateFieldForLogin()
            }
            imageBtnLoginGoogle.setOnClickListener {
                val googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions)
                val signInIntent = googleSignInClient.signInIntent
                launcher.launch(signInIntent)
            }
            loginBtnCreateAccount.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
            loginBtnRecoverAccount.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_recoverAccountFragment)
            }
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