package com.example.mainactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var register: Button
    private lateinit var change: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        register = findViewById(R.id.register)
        change = findViewById(R.id.change)
        auth = FirebaseAuth.getInstance()

        register.setOnClickListener {
            val txtEmail = email.text.toString()
            val txtPassword = password.text.toString()

            if (TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)) {
                Toast.makeText(
                    this@RegisterActivity,
                    "Empty Credentials!",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (txtPassword.length < 6) {
                Toast.makeText(
                    this@RegisterActivity,
                    "Password too short!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                registerUser(txtEmail, txtPassword)
            }
        }
        change.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser(username: String, password: String) {

        auth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(this@RegisterActivity
        ) { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    this@RegisterActivity,
                    "Registering Successful!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this@RegisterActivity,
                    "Registration Failed!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
}