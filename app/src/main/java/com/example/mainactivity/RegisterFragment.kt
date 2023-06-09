package com.example.mainactivity

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var prenom: EditText
    private lateinit var nom: EditText
    private lateinit var register: Button
    private lateinit var change: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_register, container, false)

        email = view.findViewById(R.id.email)
        password = view.findViewById(R.id.password)
        confirmPassword = view.findViewById(R.id.password2)
        prenom = view.findViewById(R.id.prenom)
        nom = view.findViewById(R.id.nom)
        register = view.findViewById(R.id.register)
        change = view.findViewById(R.id.change)
        auth = FirebaseAuth.getInstance()

        change.setOnClickListener{
            view.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        register.setOnClickListener{
            val txtEmail = email.text.toString()
            val txtPassword = password.text.toString()
            val txtPassword2 = confirmPassword.text.toString()

            if (TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)) //Vérification que les champs ne sont pas vides
                Toast.makeText(
                    activity,
                    "Champs vides !",
                    Toast.LENGTH_SHORT
                ).show()
            else if (txtPassword.length < 6)  //Vérification que le mot de passe est assez long
                Toast.makeText(
                    activity,
                    "Mot de passe trop court !",
                    Toast.LENGTH_SHORT
                ).show()
            else if (txtPassword != txtPassword2)  //Vérification que les deux mots de passe sont identiques
                Toast.makeText(
                    activity,
                    "Les mots de passe ne correspondent pas !",
                    Toast.LENGTH_SHORT
                ).show()

            else if (!isValidEmail(txtEmail)) //Vérification que l'adresse mail est valide
                Toast.makeText(
                    activity,
                    "Adresse mail uha attendue !",
                    Toast.LENGTH_SHORT
                ).show()
            else registerUser(txtEmail, txtPassword)
        }

        return view
    }

    private fun registerUser(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
            requireActivity()
        ) { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    activity,
                    "Compte enregistré avec succès !",
                    Toast.LENGTH_SHORT,
                ).show()

                //Ajout des données de l'utilisateur dans firestore.
                val db = Firebase.firestore
                val data = hashMapOf(
                    "Mail" to auth.currentUser?.email.toString(),
                    "FirstName" to prenom.text.toString(),
                    "LastName" to nom.text.toString(),
                    "PP" to "images/tele.jpeg",
                    "Role" to db.collection("Role").document("role_Student")
                )
                //ID généré automatiquement
                db.collection("User")
                    .add(data)
                    .addOnSuccessListener { documentReference ->
                        Log.d(ContentValues.TAG, "Document généré avec ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w(ContentValues.TAG, "Erreur lors de l'ajout du Document", e)
                    }

                //TESTS SUR L'UPDATE DES DONNEES
                val ref = FirebaseFirestore.getInstance().collection("User").document("Test")
                ref.update("FirstName", "GIGA")
                ref.update("LastName", "GOGIGA")
                ref.update("Role", "GAGAGIGO")
                println("IL SEST PASSE DES CHOSES")
                val doc  = FirebaseFirestore.getInstance().collection("User").whereEqualTo("Mail",auth.currentUser?.email.toString())


                //Delai pour que le serveur ait le temps de traiter les données avant de changer de page
                Handler().postDelayed({
                   loginUser(email, password)
                }, 2000)

            } else {
                Toast.makeText(
                    activity,
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

    fun loginUser(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
            requireActivity()
        ) { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    activity,
                    "Connexion réussie !",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(
                    activity,
                    "Connexion échouée !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}