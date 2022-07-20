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

    private val mPersonRepository = PersonRepository(application)
    private val mSharedPreferences = SecurityPreferences(application)

    private val mCreate = MutableLiveData<ValidationModel>()
    val create: LiveData<ValidationModel> = mCreate

    fun create(name: String, email: String, password: String) {

        mPersonRepository.create(name, email, password, object : APIListener<PersonModel>{
            override fun onSuccess(model: PersonModel) {
                mSharedPreferences.store(TaskConstants.SHARED.TOKEN_KEY,model.token)
                mSharedPreferences.store(TaskConstants.SHARED.PERSON_KEY,model.personKey)
                mSharedPreferences.store(TaskConstants.SHARED.PERSON_NAME,model.name)

                mCreate.value  = ValidationModel()
            }

            override fun onFailure(str: String) {
                mCreate.value = ValidationModel(str)
            }


        })
    }

    fun listPriorities() {

    }

}