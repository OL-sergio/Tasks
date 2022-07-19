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


class PersonRepository(val context: Context) : BaseRepository(context){

    private val mRemote = RetrofitClient.createService(PersonService::class.java)

    fun login (email: String, password: String, listener: APIListener<PersonModel>){

        if (!isConnectionAvailable(context)){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call: Call<PersonModel> = mRemote.login(email, password)
        call.enqueue(object : Callback<PersonModel> {
            override fun onResponse(call: Call<PersonModel>, response: Response<PersonModel>) {
                if (response.code() != TaskConstants.HTTP.SUCCESS){
                    listener.onFailure(failResponse(response.errorBody()!!.string()))

                }else {
                    response.body()?.let { listener.onSuccess(it) }
                }
            }

            override fun onFailure(call: Call<PersonModel>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })
    }

    fun create (name: String, email: String, password: String, listener: APIListener<PersonModel>){

        if (!isConnectionAvailable(context)){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call: Call<PersonModel> = mRemote.create(name, email, password, true)
        call.enqueue(object : Callback<PersonModel> {
            override fun onResponse(call: Call<PersonModel>, response: Response<PersonModel>) {
                if (response.code() != TaskConstants.HTTP.SUCCESS){
                    listener.onFailure(failResponse(response.errorBody()!!.string()))
                }else {
                    response.body()?.let { listener.onSuccess(it) }
                }
            }

            override fun onFailure(call: Call<PersonModel>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })
    }

    private fun failResponse(validattion: String): String {
        return  Gson().fromJson(validattion, String::class.java)
    }
}
