package com.marcos.mytasks.presentation.home.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.marcos.mytasks.R
import com.marcos.mytasks.databinding.FragmentDoneBinding
import com.marcos.mytasks.framework.firebase.FirebaseHelper
import com.marcos.mytasks.model.Task
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
            .child("task")
            .child(FirebaseHelper.getIdUser() ?: "")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        taskList.clear()

                        for (snap in snapshot.children) {
                            val task = snap.getValue(Task::class.java) as Task
                            if (task.status == STATUS_TASK_DONE) taskList.add(task)
                        }

                        binding.txtInfo.text = ""
                        taskList.reverse()
                        initAdapter()
                    } else {
                        binding.txtInfo.text = getString(R.string.home_empty_task)
                    }
                    binding.progressBar.isVisible = false
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        requireContext(), R.string.home_error_list_task, Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun initAdapter() {
        binding.recyclerHome.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerHome.setHasFixedSize(true)
        taskAdapter = TaskAdapter(requireContext(), taskList) { task, int ->
        }
        binding.recyclerHome.adapter = taskAdapter
    }

    companion object {
        private const val STATUS_TASK_DONE = 2
    }
}