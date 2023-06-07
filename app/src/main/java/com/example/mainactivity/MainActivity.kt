package com.example.mainactivity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var logout: Button


    override fun onCreate(savedInstanceState: Bundle?) {

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        super.onCreate(savedInstanceState)

        /*setContentView(R.layout.activity_main)

        logout = findViewById(R.id.logout)

        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this@MainActivity, "Logged out", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@MainActivity, StartActivity::class.java))
        }

         */
    }
}