@file:Suppress("DEPRECATION")

package com.example.mainactivity.ui.welcome

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
import com.example.mainactivity.R
import com.google.firebase.auth.FirebaseAuth
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

    /**
     * Fonction permettant de créer un compte utilisateur à condition que l'adresse mail ne soit pas déjà utilisée et que le mot de passe soit assez long et identique à la confirmation.
     * @param email Adresse mail de l'utilisateur.
     * @param password Mot de passe de l'utilisateur.
     * @param prenom Prénom de l'utilisateur.
     * @param nom Nom de l'utilisateur.
     * @param navController Contrôleur de navigation.
     * @return Unit.
     */
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
                            "Un e-mail de confirmation a été envoyé à $email",
                            Toast.LENGTH_SHORT,
                        ).show()

                        //Ajout des données de l'utilisateur dans Firestore.
                        val db = Firebase.firestore
                        val data = hashMapOf(
                            "Mail" to auth.currentUser?.email.toString(),
                            "FirstName" to prenom,
                            "LastName" to nom,
                            "pp" to 1,
                            "Role" to "Etudiant",
                        )
                        val datatags = hashMapOf(
                            "Entre Etudiants" to true,
                            "BDE" to true,
                            "Gaming" to true,
                            "Cours" to true,
                            "Examens" to true,
                            "Emploi du temps" to true,
                            "Alternance" to true,
                            "Sport" to true
                            )

                        //ID généré automatiquement
                        db.collection("User")
                            .add(data)
                            .addOnSuccessListener { documentReference ->
                                Log.d(ContentValues.TAG, "Document généré avec ID: ${documentReference.id}")

                                // Ajout de la sous-collection datatags à l'user
                                documentReference.collection("Tags").document().set(datatags)
                                    .addOnSuccessListener {
                                        Log.d(ContentValues.TAG, "Sous-collection datatags ajoutée")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w(ContentValues.TAG, "Erreur lors de l'ajout de la sous-collection", e)
                                    }
                            }
                            .addOnFailureListener { e ->
                                Log.w(ContentValues.TAG, "Erreur lors de l'ajout du Document", e)
                            }



                        //Delai pour que le serveur ait le temps de traiter les données avant de changer de page
                        Handler().postDelayed({
                            navController.navigate(R.id.action_registerFragment_to_loginFragment)
                        }, 2000)

                    } else {
                        Toast.makeText(
                            activity,
                            "Erreur lors de l'envoi de l'e-mail de confirmation",
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

    /**
     * Fonction permettant de vérifier que l'adresse mail est bien une adresse UHA.
     * @param email Adresse mail de l'utilisateur.
     * @return Boolean.
     */
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@uha\\.fr$")
        return emailRegex.matches(email)
    }
}