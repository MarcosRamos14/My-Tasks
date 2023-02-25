package com.marcos.mytasks.presentation.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.marcos.mytasks.R
import com.marcos.mytasks.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

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
                binding.progressBar.isVisible = true
                registerUser(email, password)
            } else {
                Toast.makeText(requireContext(), R.string.app_toast_password, Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(requireContext(), R.string.app_toast_email, Toast.LENGTH_SHORT).show()
        }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                    Toast.makeText(requireContext(), R.string.app_toast_create, Toast.LENGTH_SHORT).show()
                } else {
                    binding.progressBar.isVisible = false
                }
            }
    }
}