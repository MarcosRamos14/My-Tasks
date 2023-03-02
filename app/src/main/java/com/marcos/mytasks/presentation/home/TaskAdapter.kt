package com.marcos.mytasks.presentation.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.marcos.mytasks.R
import com.marcos.mytasks.databinding.ItemAdapterBinding
import com.marcos.mytasks.domain.model.Task

class TaskAdapter(
    private val context: Context,
    private val taskList: List<Task>,
    val taskSelected: (Task, Int) -> Unit
) : RecyclerView.Adapter<TaskAdapter.MyViewHolder>() {

    companion object {
        val SELECT_BACK: Int = 1
        val SELECT_REMOVE: Int = 2
        val SELECT_EDIT: Int = 3
        val SELECT_DETAILS: Int = 4
        val SELECT_NEXT: Int = 5
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val task = taskList[position]

        holder.binding.txtTitle.text = task.description

        holder.binding.btnDelete.setOnClickListener {
            taskSelected(task, SELECT_REMOVE)
        }

        holder.binding.btnEdit.setOnClickListener {
            taskSelected(task, SELECT_EDIT)
        }

        holder.binding.btnDetails.setOnClickListener {
            taskSelected(task, SELECT_DETAILS)
        }

        when (task.status) {
            0 -> {
                holder.binding.imgBtnBack.isVisible = false
                holder.binding.imgBtnNext.setColorFilter(
                    ContextCompat.getColor(context, R.color.mr_color_doing)
                )
                holder.binding.imgBtnNext.setOnClickListener {
                    taskSelected(task, SELECT_NEXT)
                }
            }
            1 -> {
                holder.binding.imgBtnBack.setColorFilter(
                    ContextCompat.getColor(context, R.color.mr_color_todo)
                )
                holder.binding.imgBtnNext.setColorFilter(
                    ContextCompat.getColor(context, R.color.mr_color_done)
                )
                holder.binding.imgBtnNext.setOnClickListener {
                    taskSelected(task, SELECT_NEXT)
                }
                holder.binding.imgBtnBack.setOnClickListener {
                    taskSelected(task, SELECT_BACK)
                }
            }
            else -> {
                holder.binding.imgBtnNext.isVisible = false
                holder.binding.imgBtnBack.setColorFilter(
                    ContextCompat.getColor(context, R.color.mr_color_doing)
                )
                holder.binding.imgBtnBack.setOnClickListener {
                    taskSelected(task, SELECT_BACK)
                }
            }
        }
    }

    override fun getItemCount() = taskList.size

    inner class MyViewHolder(val binding: ItemAdapterBinding) :
        RecyclerView.ViewHolder(binding.root)
}