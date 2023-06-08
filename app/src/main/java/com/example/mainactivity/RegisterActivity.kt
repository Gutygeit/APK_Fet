package com.example.mainactivity

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var prenom: EditText
    private lateinit var nom: EditText
    private lateinit var register: Button
    private lateinit var change: Button
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        prenom = findViewById(R.id.prenom)
        nom = findViewById(R.id.nom)
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

                //Ajout des données de l'utilisateur dans firestore
                val db = Firebase.firestore
                val data = hashMapOf(
                    "Mail" to auth.currentUser?.email.toString(),
                    "FirstName" to "PrénomDuFormulaire",
                    "LastName" to "NomDuFormulaire",
                    "Role" to db.collection("Role").document("role_Student")
                )
                //ID généré automatiquement
                db.collection("User")
                    .add(data)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "Document généré avec ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Erreur lors de l'ajout du Document", e)
                    }

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