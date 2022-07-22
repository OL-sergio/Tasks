package com.example.tasks.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.R
import com.example.tasks.databinding.ActivityLoginBinding
import com.example.tasks.viewmodel.LoginViewModel
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mViewModel: LoginViewModel

    private var _binding: ActivityLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(_binding!!.root)

        mViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        mViewModel. isAuthenticationAvailable()

        // Inicializa eventos
        setListeners()
        observe()

        // Verifica se usuário está logado
        //verifyLoggedUser()

        showAuthentication()

    }

    override fun onClick(v: View) {
        if (v.id == R.id.button_login) {
            handleLogin()
        } else if (v.id == R.id.text_register) {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun showAuthentication() {
        //Executer
        val executor: Executor = ContextCompat.getMainExecutor(this)

        //BiometricPrompt
        val biometricPrompt = BiometricPrompt(this@LoginActivity, executor,
            object : BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }
        })

        //BiometricPrompt INFO
        val info: BiometricPrompt.PromptInfo = BiometricPrompt.PromptInfo.Builder()
            .setNegativeButtonText("Cancelar")
            .setDescription("Description")
            .setSubtitle("Subtitle")
            .setTitle("Title")
            .build()

        biometricPrompt.authenticate(info)
    }

    /**
     * Inicializa os eventos de click
     */
    private fun setListeners() {
        _binding!!.buttonLogin.setOnClickListener(this)
        _binding!!.textRegister.setOnClickListener(this)
    }

    /**
     * Verifica se usuário está logado
     */
    /*
    private fun verifyLoggedUser() {
        mViewModel.verifyLoggedUser()
    }
     */
    /**
     * Observa ViewModel
     */
    private fun observe() {
        mViewModel.login.observe(this, Observer {
            if (it.success()){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{

                Toast.makeText(this, it.message(), Toast.LENGTH_SHORT).show()
            }
        })

        /*
        mViewModel.loggedUser.observe(this, Observer {
            if (it){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        })
        */

        mViewModel.fingerprint.observe(this, Observer {
            if (it){
               showAuthentication()
            }
        })
    }

    /**
     * Autentica usuário
     */
    private fun handleLogin() {
        val email = _binding!!.editEmail.text.toString()
        val password = _binding!!.editPassword.text.toString()

        mViewModel.doLogin(email, password)
    }

}
