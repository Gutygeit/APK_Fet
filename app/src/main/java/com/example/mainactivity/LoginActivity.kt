package com.example.mainactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var change: Button
    private lateinit var auth: FirebaseAuth


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
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }


    }

    private fun loginUser(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
            this@LoginActivity
        ) { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    this@LoginActivity,
                    "Login Successful!",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(
                    this@LoginActivity,
                    "Authentication Failed!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}