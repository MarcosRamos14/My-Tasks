package com.marcos.mytasks.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.marcos.mytasks.R
import com.marcos.mytasks.databinding.FragmentFormTaskBinding
import com.marcos.mytasks.framework.firebase.FirebaseHelper
import com.marcos.mytasks.model.Task

class FormTaskFragment : Fragment() {

    private lateinit var binding: FragmentFormTaskBinding

    private lateinit var task: Task
    private var newTask: Boolean = true
    private var statusTask: Int = STATUS_TASK_TODO

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentFormTaskBinding.inflate(
        layoutInflater,
        container,
        false
    ).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
    }

    private fun setupListener() {
        binding.btnSave.setOnClickListener {
            validateData()
        }
        binding.radioGroup.setOnCheckedChangeListener { _, id ->
            statusTask = when (id) {
                R.id.rdo_btn_todo -> STATUS_TASK_TODO
                R.id.rdo_btn_doing -> STATUS_TASK_DOING
                else -> STATUS_TASK_DONE
            }
        }
    }

    private fun validateData() {
        val description = binding.editDescription.text.toString().trim()
        if (description.isNotEmpty()) {
            binding.progressBar.isVisible = true

            if (newTask) task = Task()
            task.description = description
            task.status = statusTask

            saveTask()
        } else {
            Toast.makeText(
                requireContext(), R.string.generic_error_description, Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun saveTask() {
        FirebaseHelper
            .getDatabase()
            .child("task")
            .child(FirebaseHelper.getIdUser() ?: "")
            .child(task.id)
            .setValue(task)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    if (newTask) { // new task
                        findNavController().popBackStack()
                        Toast.makeText(
                            requireContext(), R.string.generic_salve_task_success, Toast.LENGTH_SHORT
                        ).show()
                    } else { // edit task
                        binding.progressBar.isVisible = false
                        Toast.makeText(
                            requireContext(), R.string.generic_att_task_success, Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {
                    Toast.makeText(
                        requireContext(), R.string.generic_salve_task_error, Toast.LENGTH_SHORT
                    ).show()
                }
            }.addOnFailureListener {
                binding.progressBar.isVisible = false
                Toast.makeText(
                    requireContext(), R.string.generic_salve_task_error, Toast.LENGTH_SHORT
                ).show()
            }
    }

    companion object {
        private const val STATUS_TASK_TODO = 0
        private const val STATUS_TASK_DOING = 1
        private const val STATUS_TASK_DONE = 2
    }
}