package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.model.PersonModel
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.model.ValidationModel
import com.example.tasks.service.repository.PersonRepository
import com.example.tasks.service.repository.SecurityPreferences

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val personRepository = PersonRepository(application.applicationContext)
    private val sharedPreferences = SecurityPreferences(application.applicationContext)

    private val _user = MutableLiveData<ValidationModel>()
    val user: LiveData<ValidationModel> = _user

    fun create(name: String, email: String, password: String) {

        personRepository.create(name, email, password, object : APIListener<PersonModel>{
            override fun onSuccess(result: PersonModel) {
                sharedPreferences.store(TaskConstants.SHARED.TOKEN_KEY,result.token)
                sharedPreferences.store(TaskConstants.SHARED.PERSON_KEY,result.personKey)
                sharedPreferences.store(TaskConstants.SHARED.PERSON_NAME,result.name)

                _user.value  = ValidationModel()
            }

            override fun onFailure(message: String) {
                _user.value = ValidationModel(message)
            }


        })
    }
}