package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.model.ValidationModel
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.repository.TaskRepository

class AllTasksViewModel(application: Application) : AndroidViewModel(application) {

    private val taskRepository = TaskRepository(application.applicationContext)
    private val priorityRepository = PriorityRepository(application.applicationContext)
    private var taskFilterID = 0

    private val _delete = MutableLiveData<ValidationModel>()
    val delete: LiveData<ValidationModel> = _delete

    private val _taskList = MutableLiveData<List<TaskModel>>()
    val tasksList: LiveData<List<TaskModel>> = _taskList

    fun list(taskFilter: Int) {
        taskFilterID = taskFilter

        val listner = object :APIListener<List<TaskModel>>{
            override fun onSuccess(result: List<TaskModel>) {
                result.forEach {
                    it.priorityDescrition = priorityRepository.getDescrition(it.priorityId)
                }

                _taskList.value = result
            }

            override fun onFailure(message: String) {
                _taskList.value = arrayListOf()
                _delete.value = ValidationModel(message)
            }
        }

        if (taskFilterID == TaskConstants.FILTER.ALL){
            taskRepository.all(listner)
        } else if (taskFilterID == TaskConstants.FILTER.NEXT){
            taskRepository.nextSevenDays(listner)
        } else if (taskFilterID == TaskConstants.FILTER.EXPIRED) {
            taskRepository.overdue(listner)
        }
    }

    fun delete(id: Int) {
        taskRepository.delete(id, object : APIListener<Boolean>{
            override fun onSuccess(result: Boolean) {
                list(taskFilterID)
                _delete.value = ValidationModel()
            }

            override fun onFailure(message: String) {
                _delete.value = ValidationModel(message)
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
        taskRepository.upadteStatus(id, complete, object : APIListener<Boolean>{
            override fun onSuccess(result: Boolean) {
                list(taskFilterID)
            }

            override fun onFailure(message: String) {
                _delete.value = ValidationModel(message)
            }
        })
    }
}