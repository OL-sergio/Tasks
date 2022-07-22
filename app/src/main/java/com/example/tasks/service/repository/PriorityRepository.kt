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

class PriorityRepository (context: Context): BaseRepository(context) {

    private val remote = RetrofitClient.createService(PriorityService::class.java)
    private val priorityDatabase = TaskDatabase.getDatabase(context).priorityDAO()

    companion object {
        private val cache = mutableMapOf<Int, String>()
        fun getDescription(id: Int): String {
            return cache[id] ?: ""
        }
        fun setDescription(id: Int, str: String) {
            cache[id] = str
        }
    }

    fun getDescrition(id: Int): String {
        val cached =  PriorityRepository.getDescription(id)
        return if (cached == ""){
          val description = priorityDatabase.getDescription(id)
            PriorityRepository.setDescription(id, description)
            description
        } else {
          cached
        }
    }

    fun all(listener : APIListener<List<PriorityModel>>) {
        if (!isConnectionAvailable(context)){
            return
        }
        val call = remote.list()
        executeCall(call, listener)
    }

    fun save(list: List<PriorityModel>) {
        priorityDatabase.clear()
        priorityDatabase.save(list)
    }


    fun list() = priorityDatabase.list()





}