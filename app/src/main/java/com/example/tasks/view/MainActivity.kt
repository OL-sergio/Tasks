package com.example.tasks.view

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.example.tasks.R
import com.example.tasks.databinding.ActivityMainBinding
import com.example.tasks.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {


    private var _binding:  ActivityMainBinding?  = null

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding!!.root)

        mViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
           startActivity(Intent(this, TaskFormActivity::class.java))
        }

        // Navegação
        setupNavigation()

        // Observadores
        observe()
    }

    override fun onResume() {
        mViewModel.loadUserName()
        super.onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setupNavigation() {

        val drawerLayout: DrawerLayout = _binding!!.drawerLayout
        val navView: NavigationView =  _binding!!.navView
        val navHostFragment  =  supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
   
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_all_tasks, R.id.nav_next_tasks, R.id.nav_expired, R.id.nav_logout),
            drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener {
                if (it.itemId == R.id.nav_logout){
                    mViewModel.logout()
                }else {
                    NavigationUI.onNavDestinationSelected(it, navController)
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
            true
        }

    }

    private fun observe() {
        mViewModel.userName.observe(this, Observer {
            val nav = _binding!!.navView
            val header = nav.getHeaderView(0)
                header.findViewById<TextView>(R.id.text_name).text = it
        })

        mViewModel.logout.observe(this, Observer {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        })
    }

}
