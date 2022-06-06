package com.example.tasks.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.R
import com.example.tasks.databinding.ActivityRegisterBinding
import com.example.tasks.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityRegisterBinding? = null

    private lateinit var mViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(_binding!!.root)

        mViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        // Inicializa eventos
        listeners()
        observe()
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.button_save) {

            val name = _binding!!.editName.text.toString()
            val email = _binding!!.editEmail.text.toString()
            val password = _binding!!.editPassword.text.toString()

            mViewModel.create(name, email, password)
        }
    }

    private fun observe() {
        mViewModel.create.observe(this, Observer {
            if (it.success()){
                startActivity(Intent(this, LoginActivity::class.java))
            }else{

                Toast.makeText(this, it.failure(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun listeners() {
       _binding!!.buttonSave.setOnClickListener(this)
    }
}
