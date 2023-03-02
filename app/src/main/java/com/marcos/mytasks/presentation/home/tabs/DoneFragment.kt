package com.marcos.mytasks.presentation.home.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.marcos.mytasks.R
import com.marcos.mytasks.databinding.FragmentDoneBinding
import com.marcos.mytasks.framework.firebase.FirebaseHelper
import com.marcos.mytasks.domain.model.Task
import com.marcos.mytasks.presentation.extension.showBottomSheet
import com.marcos.mytasks.presentation.home.HomeFragmentDirections
import com.marcos.mytasks.presentation.home.TaskAdapter

class DoneFragment : Fragment() {

    private lateinit var binding: FragmentDoneBinding
    private val taskList = mutableListOf<Task>()
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentDoneBinding.inflate(
        inflater,
        container,
        false
    ).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getTask()
    }

    private fun getTask() {
        FirebaseHelper
            .getDatabase()
            .child(getString(R.string.form_task))
            .child(FirebaseHelper.getIdUser() ?: getString(R.string.empty_string))
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        taskList.clear()

                        for (snap in snapshot.children) {
                            val task = snap.getValue(Task::class.java) as Task
                            if (task.status == STATUS_TASK_DONE) taskList.add(task)
                        }

                        taskList.reverse()
                        initAdapter()
                    }

                    taskEmpty()
                    binding.progressBar.isVisible = false
                }

                override fun onCancelled(error: DatabaseError) {
                    showBottomSheet(message = R.string.home_error_list_task)
                }
            })
    }

    private fun taskEmpty() {
        binding.txtInfo.text = if (taskList.isEmpty()) {
            getString(R.string.home_empty_task)
        } else getString(R.string.empty_string)
    }

    private fun initAdapter() {
        binding.recyclerHome.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerHome.setHasFixedSize(true)
        taskAdapter = TaskAdapter(requireContext(), taskList) { task, select ->
            optionSelect(task, select)
        }
        binding.recyclerHome.adapter = taskAdapter
    }

    private fun optionSelect(task: Task, select: Int) {
        when (select) {
            TaskAdapter.SELECT_REMOVE -> deleteTask(task)
            TaskAdapter.SELECT_EDIT -> {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToFormTaskFragment(task)
                findNavController().navigate(action)
            }
            TaskAdapter.SELECT_BACK -> {
                task.status = STATUS_TASK_DOING
                updateTask(task)
            }
        }
    }

    private fun updateTask(task: Task) {
        FirebaseHelper
            .getDatabase()
            .child(getString(R.string.form_task))
            .child(FirebaseHelper.getIdUser() ?: getString(R.string.empty_string))
            .child(task.id)
            .setValue(task)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        requireContext(), R.string.generic_att_task_success, Toast.LENGTH_SHORT
                    ).show()
                } else showBottomSheet(message = R.string.generic_salve_task_error)

            }.addOnFailureListener {
                binding.progressBar.isVisible = false
                showBottomSheet(message = R.string.generic_salve_task_error)
            }
    }

    private fun deleteTask(task: Task) {
        FirebaseHelper
            .getDatabase()
            .child(getString(R.string.form_task))
            .child(FirebaseHelper.getIdUser() ?: getString(R.string.empty_string))
            .child(task.id)
            .removeValue()

        taskList.remove(task)
        taskAdapter.notifyDataSetChanged()
    }

    companion object {
        private const val STATUS_TASK_DOING = 1
        private const val STATUS_TASK_DONE = 2
    }
}