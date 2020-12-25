package com.codinginflow.mvvmtodo.ui.tasks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.codinginflow.mvvmtodo.R
import com.codinginflow.mvvmtodo.data.Task
import com.codinginflow.mvvmtodo.databinding.FragmentTasksBinding
import com.codinginflow.mvvmtodo.utils.exhaustive
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class TasksFragment : Fragment(R.layout.fragment_tasks), TaskAdapter.OnItemClickListener {

    private val tasksViewModel: TasksViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val taskAdapter = TaskAdapter(this)
        val binding = FragmentTasksBinding.bind(view)
        binding.apply {
            rvTasks.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = taskAdapter
                setHasFixedSize(true)
            }
            fabAddTask.setOnClickListener {
                tasksViewModel.addNewTaskClick()
            }
        }
        tasksViewModel.tasks.observe(
            viewLifecycleOwner
        ) {
            taskAdapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            tasksViewModel.tasksEvent.collect { event ->
            when(event){
                is TasksViewModel.TasksEvent.NavigateToEditTaskScreen -> {
                    val action = TasksFragmentDirections.actionTasksFragmentToAddEditTaskFragment(task = event.task)
                    findNavController().navigate(action)
                }
                is TasksViewModel.TasksEvent.NavigateToAddTaskScreen -> {
                    val action = TasksFragmentDirections.actionTasksFragmentToAddEditTaskFragment()
                    findNavController().navigate(action)
                }
            }.exhaustive

            }
        }

    }

    override fun onItemClick(task: Task) {
        tasksViewModel.onTaskSelected(task)
    }

    override fun onCheckBoxClick(task: Task, isChecked: Boolean) {
        tasksViewModel.onTaskCheckChanged(task, isChecked)
    }
}