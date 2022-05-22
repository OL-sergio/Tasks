package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.repository.TaskRepository

class AllTasksViewModel(application: Application) : AndroidViewModel(application) {

    private val mTaskRepository = TaskRepository(application)

    private val mTaskList = MutableLiveData<List<TaskModel>>()
    val tasks: LiveData<List<TaskModel>> = mTaskList

    fun list() {
        mTaskRepository.all(object :APIListener<List<TaskModel>>{
            override fun onSuccess(model: List<TaskModel>) {
                mTaskList.value = model
            }

            override fun onFailure(str: String) {
                mTaskList.value = arrayListOf()
            }

        })
    }

    fun delete(id: Int) {
        mTaskRepository.delete(id, object : APIListener<Boolean>{
            override fun onSuccess(model: Boolean) {
                list()
            }

            override fun onFailure(str: String) {
            }

        })
    }

    fun complete(id: Int) {
      upadateStatus(id, true)
    }

    fun undo(id: Int) {
        upadateStatus(id, false)
    }

    private fun upadateStatus(id: Int, complete: Boolean){
        mTaskRepository.upadteStatus(id, complete, object : APIListener<Boolean>{
            override fun onSuccess(model: Boolean) {
                list()
            }

            override fun onFailure(str: String) {
            }

        })

    }

}