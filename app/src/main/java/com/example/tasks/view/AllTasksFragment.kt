package com.example.tasks.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasks.R
import com.example.tasks.databinding.FragmentAllTasksBinding
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.TaskListener
import com.example.tasks.view.adapter.TaskAdapter
import com.example.tasks.viewmodel.AllTasksViewModel

class AllTasksFragment : Fragment() {

    private var _binding : FragmentAllTasksBinding? = null
    private val binding get() = _binding!!

    private lateinit var mViewModel: AllTasksViewModel
    private lateinit var listener: TaskListener
    private val adapter = TaskAdapter()
    private var taskFilter  = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        mViewModel = ViewModelProvider(this).get(AllTasksViewModel::class.java)

        _binding = FragmentAllTasksBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Filter Tasks
        taskFilter = arguments!!.getInt(TaskConstants.BUNDLE.TASKFILTER, 0)

        val recycler = _binding!!.recyclerAllTasks
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter

        // Eventos disparados ao clicar nas linhas da RecyclerView
        listener = object : TaskListener {
            override fun onListClick(id: Int) {
                val intent = Intent(context, TaskFormActivity::class.java)
                val bundle = Bundle()
                bundle.putInt(TaskConstants.BUNDLE.TASKID, id)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            override fun onDeleteClick(id: Int) {
                mViewModel.delete(id)
            }

            override fun onCompleteClick(id: Int) {
                mViewModel.upadateStatus(id, true)
            }

            override fun onUndoClick(id: Int) {
                mViewModel.upadateStatus(id, false)
            }
        }

        // Cria os observadores
        observe()

        // Retorna view
        return root
    }

    override fun onResume() {
        super.onResume()
        adapter.attachListener(listener)
        mViewModel.list(taskFilter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observe() {
        mViewModel.tasksList.observe(viewLifecycleOwner) {
                adapter.updateListener(it)
        }

        mViewModel.delete.observe(viewLifecycleOwner){
            if (it.success()){
                Toast.makeText(context,getString(R.string.task_removed) ,Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context,it.message() ,Toast.LENGTH_SHORT).show()
            }
        }

        mViewModel.updateStatus.observe(viewLifecycleOwner){
            if (it.success()){
                Toast.makeText(context,getString(R.string.task_updated) ,Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context,it.message() ,Toast.LENGTH_SHORT).show()
            }
        }
    }
}
