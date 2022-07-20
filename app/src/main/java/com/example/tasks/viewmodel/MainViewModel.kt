package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.repository.SecurityPreferences

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences = SecurityPreferences(application.applicationContext)

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val mLogout = MutableLiveData<Boolean>()
    val logout: LiveData<Boolean> = mLogout

    fun loadUserName() {
        _userName.value = sharedPreferences.get(TaskConstants.SHARED.PERSON_NAME)
    }

    fun logout() {
        sharedPreferences.remove(TaskConstants.SHARED.TOKEN_KEY)
        sharedPreferences.remove(TaskConstants.SHARED.PERSON_KEY)
        sharedPreferences.remove(TaskConstants.SHARED.PERSON_NAME)

        mLogout.value = true
    }
}