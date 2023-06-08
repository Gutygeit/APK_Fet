package com.example.mainactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mainactivity.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var change: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        login = findViewById(R.id.register)
        change = findViewById(R.id.change)
        auth = FirebaseAuth.getInstance()

        login.setOnClickListener {
            val txtEmail = email.text.toString()
            val txtPassword = password.text.toString()

            loginUser(txtEmail, txtPassword)
        }
        change.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }

    fun loginUser(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
            this@LoginActivity
        ) { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    this@LoginActivity,
                    "Connexion réussie !",
                    Toast.LENGTH_SHORT
                ).show()
                /*val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)*/
                binding = ActivityMainBinding.inflate(layoutInflater)
                setContentView(binding.root)
                val navView: BottomNavigationView = binding.navView
                val navController = findNavController(R.id.nav_host_fragment_activity_main)
                navView.setupWithNavController(navController)
                finish()
            } else {
                Toast.makeText(
                    this@LoginActivity,
                    "Connexion échouée !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}