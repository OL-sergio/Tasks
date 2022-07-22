package com.example.tasks.service.repository

import android.content.Context
import com.example.tasks.R
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.remote.RetrofitClient
import com.example.tasks.service.repository.remote.TaskService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository (context: Context) : BaseRepository(context) {

    private val remote = RetrofitClient.createService(TaskService::class.java)

    private fun list(call: Call <List<TaskModel>>, listener: APIListener<List<TaskModel>>){
        if (!isConnectionAvailable(context)){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        executeCall(call, listener)
    }


    fun all(listener: APIListener<List<TaskModel>>) {
        if (!isConnectionAvailable(context)){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        val call = remote.all()
       executeCall(call, listener)
    }

    fun nextSevenDays(listener: APIListener<List<TaskModel>>) {
        val call = remote.nextSevenDays()
        executeCall(call, listener)
    }

    fun overdue(listener: APIListener<List<TaskModel>>) {
        val call = remote.overdue()
        executeCall(call, listener)
    }

    fun create (task: TaskModel, listener: APIListener<Boolean>) {
        if (!isConnectionAvailable(context)){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        val call = remote.create(task.priorityId, task.description, task.dueDate, task.complete)
        executeCall(call, listener)
    }

    fun update (task: TaskModel, listener: APIListener<Boolean>) {
        if (!isConnectionAvailable(context)){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        val call = remote.update( task.id, task.priorityId, task.description, task.dueDate, task.complete)
        executeCall(call, listener)
    }

    fun load (id: Int, listener: APIListener<TaskModel>) {
        if (!isConnectionAvailable(context)){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        val call = remote.select(id)
        executeCall(call, listener)
    }

    fun delete(id: Int, listener: APIListener<Boolean>){
        if (!isConnectionAvailable(context)){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        val call = remote.delete(id)
        executeCall(call, listener)
    }

    fun complete(id: Int, listener: APIListener<Boolean>){
        if (!isConnectionAvailable(context)){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        val call = remote.complete(id)
        executeCall(call, listener)
    }

    fun undo(id: Int, listener: APIListener<Boolean>){
        if (!isConnectionAvailable(context)){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        val call = remote.update(id)
        executeCall(call, listener)
    }
}