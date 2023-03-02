package com.marcos.mytasks.presentation.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.marcos.mytasks.R
import com.marcos.mytasks.databinding.FragmentRecoverAccountBinding
import com.marcos.mytasks.framework.firebase.FirebaseHelper
import com.marcos.mytasks.presentation.extension.initToolbar
import com.marcos.mytasks.presentation.extension.showBottomSheet
import com.marcos.mytasks.presentation.utils.BaseFragment

class RecoverAccountFragment : BaseFragment() {

    private lateinit var binding: FragmentRecoverAccountBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentRecoverAccountBinding.inflate(
        inflater,
        container,
        false
    ).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)
        auth = Firebase.auth
        setupListener()
    }

    private fun setupListener() {
        binding.btnSend.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        val email = binding.editEmail.text.toString().trim()

        if (email.isNotEmpty()) {
            hideKeyboard()
            binding.progressBar.isVisible = true
            recoverAccountUser(email)
        } else showBottomSheet(message = R.string.app_message_email)
    }

    private fun recoverAccountUser(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    showBottomSheet(message = R.string.app_message_recover)
                } else showBottomSheet(
                    message = FirebaseHelper.validError(task.exception?.message ?: "")
                )
                binding.progressBar.isVisible = false
            }
    }
}