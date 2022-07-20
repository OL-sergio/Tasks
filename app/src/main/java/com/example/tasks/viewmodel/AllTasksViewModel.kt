package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.model.ValidationModel
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.TaskRepository

class AllTasksViewModel(application: Application) : AndroidViewModel(application) {

    private val taskRepository = TaskRepository(application.applicationContext)
    private var taskFilterID = 0

    private val mValidation = MutableLiveData<ValidationModel>()
    val validation: LiveData<ValidationModel> = mValidation

    private val mTaskList = MutableLiveData<List<TaskModel>>()
    val tasks: LiveData<List<TaskModel>> = mTaskList

    fun list(taskFilter: Int) {
        taskFilterID = taskFilter

        val listner = object :APIListener<List<TaskModel>>{
            override fun onSuccess(result: List<TaskModel>) {
                mTaskList.value = result
            }

            override fun onFailure(message: String) {
                mTaskList.value = arrayListOf()
                mValidation.value = ValidationModel(message)
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
                mValidation.value = ValidationModel()
            }

            override fun onFailure(message: String) {
                mValidation.value = ValidationModel(message)
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
                mValidation.value = ValidationModel(message)
            }

        })

    }

}