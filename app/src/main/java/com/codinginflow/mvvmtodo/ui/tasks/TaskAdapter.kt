package com.codinginflow.mvvmtodo.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codinginflow.mvvmtodo.R
import com.codinginflow.mvvmtodo.data.Task
import com.codinginflow.mvvmtodo.databinding.ItemTaskBinding
import kotlin.coroutines.coroutineContext

class TaskAdapter(private val listener: OnItemClickListener) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(DiffCallback()) {
    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                cbCompleted.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task=getItem(position)
                        listener.onCheckBoxClick(task, cbCompleted.isChecked)
                    }
                }
                binding.root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task=getItem(position)
                        listener.onItemClick(task)
                    }
                }
            }
        }
        fun bind(task: Task) {
            binding.apply {
                cbCompleted.isChecked = task.isCompleted
                tvTaskName.text = task.name
                tvTaskName.paint.isStrikeThruText = task.isCompleted
                ivImportant.isVisible = task.isImportant
                if(task.isImportant) {
                    viewPriority.setBackgroundColor(root.context.resources.getColor(R.color.design_default_color_secondary_variant))
                } else {
                    viewPriority.setBackgroundColor(root.context.resources.getColor(R.color.design_default_color_secondary))
                }


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean =
            oldItem == newItem

    }

    interface OnItemClickListener{
        fun onItemClick(task: Task)
        fun onCheckBoxClick(task: Task, isChecked: Boolean)
    }
}