package com.example.tasks.service.repository

import android.content.Context
import com.example.tasks.R
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.repository.local.TaskDatabase
import com.example.tasks.service.repository.remote.PriorityService
import com.example.tasks.service.repository.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepository (val context: Context): BaseRepository(context) {

    private val remote = RetrofitClient.createService(PriorityService::class.java)
    private val priorityDatabase = TaskDatabase.getDatabase(context).priorityDAO()

    fun all(listener : APIListener<List<PriorityModel>>) {
        if (!isConnectionAvailable(context)){
            return
        }

        val call  = remote.list()
        call.enqueue(object :Callback<List<PriorityModel>>{
            override fun onResponse(
                call: Call<List<PriorityModel>>,
                response: Response<List<PriorityModel>>
            ) {
                if (response.code() == TaskConstants.HTTP.SUCCESS){
                    response.body()?.let { listener.onSuccess(it) }
                } else {
                    listener.onFailure(failResponse(response.errorBody()!!.string()))
                }
            }

            override fun onFailure(call: Call<List<PriorityModel>>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })
    }
    fun save(list: List<PriorityModel>) {
        priorityDatabase.clear()
        priorityDatabase.save(list)
    }


    fun list() = priorityDatabase.list()

    fun getDescrition(id: Int) = priorityDatabase.getDescription(id)

    private fun failResponse(validattion: String): String {
        return  Gson().fromJson(validattion, String::class.java)
    }
}