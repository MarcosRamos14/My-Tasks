package com.marcos.mytasks.ui.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.marcos.mytasks.R
import com.marcos.mytasks.databinding.FragmentRegisterBinding
import com.marcos.mytasks.framework.firebase.FirebaseHelper
import com.marcos.mytasks.ui.extension.initToolbar
import com.marcos.mytasks.ui.extension.showBottomSheet
import com.marcos.mytasks.ui.utils.BaseFragment

class RegisterFragment : BaseFragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentRegisterBinding.inflate(
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
        binding.btnRegister.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        val email = binding.editEmail.text.toString().trim()
        val password = binding.editPassword.text.toString().trim()

        if (email.isNotEmpty()) {

            if (password.isNotEmpty()) {
                hideKeyboard()
                binding.progressBar.isVisible = true
                registerUser(email, password)
            } else showBottomSheet(message = R.string.app_message_password)

        } else showBottomSheet(message = R.string.app_message_email)
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    findNavController().navigate(R.id.action_global_homeFragment)
                    Toast.makeText(requireContext(), R.string.app_toast_create, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    showBottomSheet(
                        message = FirebaseHelper.validError(task.exception?.message ?: "")
                    )
                    binding.progressBar.isVisible = false
                }
            }
    }
}