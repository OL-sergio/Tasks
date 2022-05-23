package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.repository.TaskRepository

class AllTasksViewModel(application: Application) : AndroidViewModel(application) {

    private val mTaskRepository = TaskRepository(application)
    private var mTaskFilter = 0

    private val mValidation = MutableLiveData<ValidationListener>()
    val validation: LiveData<ValidationListener> = mValidation

    private val mTaskList = MutableLiveData<List<TaskModel>>()
    val tasks: LiveData<List<TaskModel>> = mTaskList

    fun list(taskFilter: Int) {
        mTaskFilter = taskFilter

        val listner = object :APIListener<List<TaskModel>>{
            override fun onSuccess(model: List<TaskModel>) {
                mTaskList.value = model
            }

            override fun onFailure(str: String) {
                mTaskList.value = arrayListOf()
                mValidation.value = ValidationListener(str)
            }
        }

        if (mTaskFilter == TaskConstants.FILTER.ALL){
            mTaskRepository.all(listner)
        } else if (mTaskFilter == TaskConstants.FILTER.NEXT){
            mTaskRepository.nextSevenDays(listner)
        } else if (mTaskFilter == TaskConstants.FILTER.EXPIRED) {
            mTaskRepository.overdue(listner)
        }
    }

    fun delete(id: Int) {
        mTaskRepository.delete(id, object : APIListener<Boolean>{
            override fun onSuccess(model: Boolean) {
                list(mTaskFilter)
                mValidation.value = ValidationListener()
            }

            override fun onFailure(str: String) {
                mValidation.value = ValidationListener(str)
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
                list(mTaskFilter)
            }

            override fun onFailure(str: String) {
            }

        })

    }

}