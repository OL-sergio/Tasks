package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.model.ValidationModel
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.repository.TaskRepository

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {

    private val priorityRepository = PriorityRepository(application.applicationContext)
    private val taskRepository = TaskRepository(application.applicationContext)

    private val _priorityList = MutableLiveData<List<PriorityModel>>()
    val priorityList: LiveData<List<PriorityModel>> = _priorityList

    private val _taskSave = MutableLiveData<ValidationModel>()
    val taskSave: LiveData<ValidationModel> = _taskSave

    private val _task = MutableLiveData<TaskModel>()
    val task: LiveData<TaskModel> = _task

    fun listPriorities() {
        _priorityList.value = priorityRepository.list()

    }

    fun save(task: TaskModel) {
        if (task.id == 0) {
            taskRepository.create(task, object : APIListener<Boolean>{
                override fun onSuccess(result: Boolean) {
                    _taskSave.value = ValidationModel()
                }

                override fun onFailure(message: String) {
                    _taskSave.value = ValidationModel(message)
                }
            })
        }else {
            taskRepository.update(task, object : APIListener<Boolean>{
                override fun onSuccess(result: Boolean) {
                    _taskSave.value = ValidationModel()
                }

                override fun onFailure(message: String) {
                    _taskSave.value = ValidationModel(message)
                }
            })
        }
    }

    fun load(taskId: Int) {
        taskRepository.load(taskId, object : APIListener<TaskModel>{
            override fun onSuccess(result: TaskModel) {
                _task.value = result
            }

            override fun onFailure(message: String) {
                _taskSave.value = ValidationModel(message)
            }


        })

    }
}