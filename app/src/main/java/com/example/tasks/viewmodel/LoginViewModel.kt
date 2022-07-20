package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.helper.FingerprintHelper
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.model.ValidationModel
import com.example.tasks.service.model.PersonModel
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.repository.PersonRepository
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.repository.SecurityPreferences
import com.example.tasks.service.repository.remote.RetrofitClient


class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val personRepository = PersonRepository(application.applicationContext)
    private val priorityRepository = PriorityRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _login = MutableLiveData<ValidationModel>()
    val login: LiveData<ValidationModel> = _login


    private val _loggedUser = MutableLiveData<Boolean>()
    val loggedUser: LiveData<Boolean> = _loggedUser


    private val _fingerprint = MutableLiveData<Boolean>()
    val fingerprint: LiveData<Boolean> = _fingerprint
    /**
     * Faz login usando API
     */
    fun doLogin(email: String, password: String) {
        personRepository.login(email, password, object : APIListener<PersonModel> {
            override fun onSuccess(result: PersonModel) {

                securityPreferences.store(TaskConstants.SHARED.TOKEN_KEY,result.token)
                securityPreferences.store(TaskConstants.SHARED.PERSON_KEY,result.personKey)
                securityPreferences.store(TaskConstants.SHARED.PERSON_NAME,result.name)

                RetrofitClient.addHeader(result.token, result.personKey)

                _login.value = ValidationModel()
            }

            override fun onFailure(message: String) {
                _login.value =  ValidationModel(message)
            }

        })
    }

    /**
     * Verifica se usuário está logado
     */
    /*fun verifyLoggedUser() {

        val token = mSharedPreferences.get(TaskConstants.SHARED.TOKEN_KEY)
        val person = mSharedPreferences.get(TaskConstants.SHARED.PERSON_KEY)

        RetrofitClient.addHeader(token, person)

        val logged = ( token != "" && person != "" )

        if (!logged){
            priorityRepository.all(object : APIListener<List<PriorityModel>>{
                override fun onSuccess(model: List<PriorityModel>) {
                    val s = ""
                }

                override fun onFailure(message: String) {
                    val S = ""
                }

            })
        }
        _loggedUser.value  = logged

    }*/

    fun isAuthenticationAvailable() {

        val token = securityPreferences.get(TaskConstants.SHARED.TOKEN_KEY)
        val person = securityPreferences.get(TaskConstants.SHARED.PERSON_KEY)

        RetrofitClient.addHeader(token, person)

        val everLogged = ( token != "" && person != "" )

        if (!everLogged){
            priorityRepository.all(object : APIListener<List<PriorityModel>>{
                override fun onSuccess(result: List<PriorityModel>) {
                    priorityRepository.save(result)
                }

                override fun onFailure(message: String) {
                    val S = ""
                }

            })
        }
        _loggedUser.value  = everLogged


        if (FingerprintHelper.isAuthenticationAvailable(getApplication())){
            _fingerprint.value = everLogged
        }
    }
}