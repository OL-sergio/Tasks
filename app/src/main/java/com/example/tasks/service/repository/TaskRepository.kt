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

class TaskRepository (val context: Context) : BaseRepository(context) {

    private val remote = RetrofitClient.createService(TaskService::class.java)

    private fun list(call: Call <List<TaskModel>>, listener: APIListener<List<TaskModel>>){

        if (!isConnectionAvailable(context)){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        call.enqueue(object : Callback<List<TaskModel>>{
            override fun onResponse(call: Call<List<TaskModel>>, response: Response<List<TaskModel>>) {
                handleResponse(response, listener)
            }

            override fun onFailure(call: Call<List<TaskModel>>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })
    }


    fun all(listener: APIListener<List<TaskModel>>) {
        val call: Call<List<TaskModel>> = remote.all()
        list(call, listener)
    }

    fun nextSevenDays(listener: APIListener<List<TaskModel>>) {
        val call: Call<List<TaskModel>> = remote.nextSevenDays()
        list(call, listener)
    }

    fun overdue(listener: APIListener<List<TaskModel>>) {
        val call: Call<List<TaskModel>> = remote.overdue()
        list(call, listener)
    }

    fun upadteStatus(id: Int, complete: Boolean, listener: APIListener<Boolean>){

        if (!isConnectionAvailable(context)){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call: Call<Boolean> = if (complete){
            remote.complete(id)

        }else{
            remote.update(id)
        }
        call.enqueue(object : Callback<Boolean>{
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                handleResponse(response, listener)
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })
    }

    fun create (task: TaskModel, listener: APIListener<Boolean>) {
        if (!isConnectionAvailable(context)){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        val call = remote.create(task.priorityId, task.description, task.dueDate, task.complete)
        call.enqueue(object : Callback<Boolean>{
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                handleResponse(response, listener)
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })
    }

    fun update (task: TaskModel, listener: APIListener<Boolean>) {
        if (!isConnectionAvailable(context)){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        val call = remote.update( task.id, task.priorityId, task.description, task.dueDate, task.complete)
        call.enqueue(object : Callback<Boolean>{
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                handleResponse(response, listener)
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })
    }

    fun load (id: Int, listener: APIListener<TaskModel>) {
        if (!isConnectionAvailable(context)){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        val call = remote.select(id)
        call.enqueue(object : Callback<TaskModel>{
            override fun onResponse(call: Call<TaskModel>, response: Response<TaskModel>) {
                handleResponse(response, listener)
            }

            override fun onFailure(call: Call<TaskModel>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }


        })
    }

    fun delete(id: Int, listener: APIListener<Boolean>){
        if (!isConnectionAvailable(context)){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        val call = remote.delete(id)
        call.enqueue(object : Callback<Boolean>{
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                handleResponse(response, listener)
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })
    }
}