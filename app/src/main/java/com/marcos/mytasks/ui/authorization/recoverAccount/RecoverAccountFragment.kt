package com.marcos.mytasks.ui.authorization.recoverAccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.marcos.mytasks.R
import com.marcos.mytasks.databinding.FragmentRecoverAccountBinding
import com.marcos.mytasks.ui.authorization.recoverAccount.RecoverAccountViewModel.UiState
import com.marcos.mytasks.ui.extension.showBottomSheet
import com.marcos.mytasks.ui.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecoverAccountFragment : BaseFragment() {

    private lateinit var binding: FragmentRecoverAccountBinding
    private val viewModel: RecoverAccountViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentRecoverAccountBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
        observerRecoverAccountUiState()
    }

    private fun setupListener() {
        binding.recoverBtnSend.setOnClickListener {
            validateData()
        }
        binding.recoverBtnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun validateData() {
        val email = binding.recoverEditEmail.text.toString().trim()
        if (email.isNotEmpty()) {
            hideKeyboard()
            binding.recoverProgressBar.isVisible = true
            viewModel.recoverAccount(email)
        } else showBottomSheet(message = R.string.app_message_email)
    }

    private fun observerRecoverAccountUiState() {
        viewModel.state.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is UiState.Success -> {
                    showBottomSheet(message = uiState.successMessage)
                }
                is UiState.Error -> {
                    showBottomSheet(message = uiState.errorMessage)
                }
            }
        }
    }
}