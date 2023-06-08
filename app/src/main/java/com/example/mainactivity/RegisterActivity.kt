package com.example.mainactivity

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var prenom: EditText
    private lateinit var nom: EditText
    private lateinit var register: Button
    private lateinit var change: Button
    private lateinit var auth: FirebaseAuth

    // Dans RegisterActivity
    val loginActivity = LoginActivity()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        confirmPassword = findViewById(R.id.password2)
        prenom = findViewById(R.id.prenom)
        nom = findViewById(R.id.nom)
        register = findViewById(R.id.register)
        change = findViewById(R.id.change)
        auth = FirebaseAuth.getInstance()

        register.setOnClickListener {
            val txtEmail = email.text.toString()
            val txtPassword = password.text.toString()
            val txtPassword2 = confirmPassword.text.toString()

            if (TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)) //Vérification que les champs ne sont pas vides
                Toast.makeText(
                    this@RegisterActivity,
                    "Champs vides !",
                    Toast.LENGTH_SHORT
                ).show()
             else if (txtPassword.length < 6)  //Vérification que le mot de passe est assez long
                Toast.makeText(
                    this@RegisterActivity,
                    "Mot de passe trop court !",
                    Toast.LENGTH_SHORT
                ).show()
             else if (txtPassword != txtPassword2)  //Vérification que les deux mots de passe sont identiques
                Toast.makeText(
                    this@RegisterActivity,
                    "Les mots de passe ne correspondent pas !",
                    Toast.LENGTH_SHORT
                ).show()

            else if (!isValidEmail(txtEmail)) //Vérification que l'adresse mail est valide
                Toast.makeText(
                    this@RegisterActivity,
                    "Adresse mail uha attendue !",
                    Toast.LENGTH_SHORT
                ).show()

            else registerUser(txtEmail, txtPassword)

        }
        change.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this@RegisterActivity
        ) { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    this@RegisterActivity,
                    "Compte enregistré avec succès !",
                    Toast.LENGTH_SHORT,
                ).show()

                //Ajout des données de l'utilisateur dans firestore
                val db = Firebase.firestore
                val data = hashMapOf(
                    "Mail" to auth.currentUser?.email.toString(),
                    "FirstName" to prenom.text.toString(),
                    "LastName" to nom.text.toString(),
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

                //TESTS SUR L'UPDATE DES DONNEES
                val ref = FirebaseFirestore.getInstance().collection("User").document("Test")
                ref.update("FirstName", "GIGA")
                ref.update("LastName", "GOGIGA")
                ref.update("Role", "GAGAGIGO")
                println("IL SEST PASSE DES CHOSES")

                //Delai pour que le serveur ait le temps de traiter les données avant de changer de page
                Handler().postDelayed({
                    loginActivity.loginUser(email, password)
                }, 2000)

            } else {
                Toast.makeText(
                    this@RegisterActivity,
                    "Enregistrement échoué !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@uha\\.fr$")
        return emailRegex.matches(email)
    }

}