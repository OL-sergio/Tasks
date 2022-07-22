package com.example.tasks.service.model

class ValidationModel (message: String = "")  {

    private var status: Boolean = true
    private var validationMessasage: String =  ""

    init {
        if (message != "") {
            status = false
            validationMessasage = message

        }
    }

    fun success() = status
    fun message() = validationMessasage

}