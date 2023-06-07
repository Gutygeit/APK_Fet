package com.example.mainactivity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.example.mainactivity.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var logout: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        if(auth.currentUser==null){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        /*setContentView(R.layout.activity_main)

        logout = findViewById(R.id.logout)

        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this@MainActivity, "Logged out", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@MainActivity, StartActivity::class.java))
        }*/


    }
}