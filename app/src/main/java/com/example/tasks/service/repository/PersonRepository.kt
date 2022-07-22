package com.example.tasks.service.repository

import android.content.Context
import com.example.tasks.R
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.model.PersonModel
import com.example.tasks.service.repository.remote.PersonService
import com.example.tasks.service.repository.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PersonRepository( context: Context) : BaseRepository(context){

    private val remote = RetrofitClient.createService(PersonService::class.java)

    fun login (email: String, password: String, listener: APIListener<PersonModel>){

        if (!isConnectionAvailable(context)){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call = remote.login(email, password)
        executeCall(call, listener)
    }

    fun create (name: String, email: String, password: String, listener: APIListener<PersonModel>){

        if (!isConnectionAvailable(context)){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call = remote.create(name, email, password, true)
        executeCall(call, listener)
    }
}
