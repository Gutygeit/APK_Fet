package com.example.mainactivity

import android.content.ContentValues
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_register, container, false)


        val email = view.findViewById<EditText>(R.id.email)
        val password = view.findViewById<EditText>(R.id.password)
        val confirmPassword = view.findViewById<EditText>(R.id.confirmPassword)

        val prenom = view.findViewById<EditText>(R.id.prenom)
        val nom = view.findViewById<EditText>(R.id.nom)

        val register = view.findViewById<Button>(R.id.register)

        auth = FirebaseAuth.getInstance()

        register.setOnClickListener{
            val txtEmail = email.text.toString()
            val txtPassword = password.text.toString()
            val txtConfirmPassword = confirmPassword.text.toString()
            val txtPrenom = prenom.text.toString()
            val txtNom = nom.text.toString()

            if (TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)) //Vérification que les champs ne sont pas vides
                Toast.makeText(
                    activity,
                    "Veuillez remplir les champs",
                    Toast.LENGTH_SHORT
                ).show()
            else if (!isValidEmail(txtEmail)) //Vérification que l'adresse mail est valide
                Toast.makeText(
                    activity,
                    "Une adresse UHA est requise",
                    Toast.LENGTH_SHORT
                ).show()
            else if (txtPassword.length < 6)  //Vérification que le mot de passe est assez long
                Toast.makeText(
                    activity,
                    "Le mot de passe doit contenir au moins 6 caractères",
                    Toast.LENGTH_SHORT
                ).show()
            else if (txtPassword != txtConfirmPassword)  //Vérification que les deux mots de passe sont identiques
                Toast.makeText(
                    activity,
                    "Les mots de passe ne sont pas les mêmes",
                    Toast.LENGTH_SHORT
                ).show()
            else registerUser(txtEmail, txtPassword, txtPrenom, txtNom, view.findNavController())
        }

        return view
    }

    private fun registerUser(email: String, password: String, prenom: String, nom: String, navController: NavController) {

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
            requireActivity()
        ) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                user?.sendEmailVerification()?.addOnCompleteListener { verificationTask ->
                    if (verificationTask.isSuccessful) {
                        Toast.makeText(
                            activity,
                            "Un e-mail de confirmation a été envoyé à votre adresse e-mail.",
                            Toast.LENGTH_SHORT,
                        ).show()

                        //Ajout des données de l'utilisateur dans Firestore.
                        val db = Firebase.firestore
                        val data = hashMapOf(
                            "Mail" to auth.currentUser?.email.toString(),
                            "FirstName" to prenom,
                            "LastName" to nom,
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
                        println("IL S'EST PASSE DES CHOSES")
                        val doc  = FirebaseFirestore.getInstance().collection("User").whereEqualTo("Mail",auth.currentUser?.email.toString())

                        //Delai pour que le serveur ait le temps de traiter les données avant de changer de page
                        Handler().postDelayed({
                            navController.navigate(R.id.action_registerFragment_to_loginFragment)
                        }, 2000)

                    } else {
                        Toast.makeText(
                            activity,
                            "Erreur lors de l'envoi de l'e-mail de confirmation.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

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
}