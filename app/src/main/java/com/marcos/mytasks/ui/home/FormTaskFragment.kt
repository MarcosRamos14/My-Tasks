package com.marcos.mytasks.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.marcos.mytasks.R
import com.marcos.mytasks.databinding.FragmentFormTaskBinding
import com.marcos.mytasks.domain.model.Task
import com.marcos.mytasks.framework.firebase.FirebaseHelper
import com.marcos.mytasks.ui.extension.initToolbar
import com.marcos.mytasks.ui.extension.showBottomSheet
import com.marcos.mytasks.ui.utils.BaseFragment

class FormTaskFragment : BaseFragment() {

    private lateinit var binding: FragmentFormTaskBinding
    private val args: FormTaskFragmentArgs by navArgs()
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
        initToolbar(binding.toolbar)
        setupListener()
        getArgs()
    }

    private fun getArgs() {
        args.task.let {
            if (it != null) {
                task = it
                configTask()
            }
        }
    }

    private fun configTask() {
        newTask = false
        statusTask = task.status
        binding.txtToolbar.text = getString(R.string.form_task_txt_title_edit)
        binding.editDescription.setText(task.description)
        setStatus()
    }

    private fun setStatus() {
        binding.radioGroup.check(
            when (task.status) {
                STATUS_TASK_TODO -> R.id.rdo_btn_todo
                STATUS_TASK_DOING -> R.id.rdo_btn_doing
                else -> R.id.rdo_btn_done
            }
        )
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
            hideKeyboard()
            binding.progressBar.isVisible = true

            if (newTask) task = Task()
            task.description = description
            task.status = statusTask

            saveTask()
        } else showBottomSheet(message = R.string.generic_error_description)
    }

    private fun saveTask() {
        FirebaseHelper
            .getDatabase()
            .child(getString(R.string.form_task))
            .child(FirebaseHelper.getIdUser() ?: getString(R.string.empty_string))
            .child(task.id)
            .setValue(task)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    if (newTask) {
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
                } else showBottomSheet(message = R.string.generic_salve_task_error)

            }.addOnFailureListener {
                binding.progressBar.isVisible = false
                showBottomSheet(message = R.string.generic_salve_task_error)
            }
    }

    companion object {
        private const val STATUS_TASK_TODO = 0
        private const val STATUS_TASK_DOING = 1
        private const val STATUS_TASK_DONE = 2
    }
}