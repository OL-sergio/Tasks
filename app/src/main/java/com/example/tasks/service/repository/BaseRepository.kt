package com.example.tasks.service.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIListener
import com.google.gson.Gson
import retrofit2.Response


open class BaseRepository (context: Context){

    private fun failResponse(validattion: String): String {
        return  Gson().fromJson(validattion, String::class.java)
    }

    fun <T> handleResponse(response: Response<T>, listener: APIListener<T>) =
        if (response.code() == TaskConstants.HTTP.SUCCESS){
            response.body()?.let { listener.onSuccess(it) }
        }else {
            listener.onFailure(failResponse(response.errorBody()!!.string()))
        }


    fun isConnectionAvailable(context: Context): Boolean {
        var result = false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = cm.activeNetwork ?: return false
            val actNw = cm.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            cm.run {
                cm.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }
        return result
    }
}