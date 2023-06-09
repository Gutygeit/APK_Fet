package com.example.mainactivity.ui.welcome

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.mainactivity.MainActivity
import com.example.mainactivity.R
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_login, container, false)

        val email = view.findViewById<EditText>(R.id.email)
        val password = view.findViewById<EditText>(R.id.password)

        val login = view.findViewById<Button>(R.id.login)
        val change = view.findViewById<Button>(R.id.change)
        val recovery = view.findViewById<TextView>(R.id.recovery)

        auth = FirebaseAuth.getInstance()

        change.setOnClickListener {
            view.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        recovery.setOnClickListener {
            view.findNavController().navigate(R.id.action_loginFragment_to_recoveryFragment)
        }

        login.setOnClickListener {
            if (email.text.toString() != "" && password.text.toString() != "") {
                loginUser(email.text.toString(), password.text.toString())
                email.text = null
                password.text = null
            } else {
                Toast.makeText(
                    activity,
                    "Veuillez remplir les champs",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return view
    }

    /**
     * Fonction permettant de connecter l'utilisateur à l'application à condition que le mot de passe soit correct
     * et l'adresse mail soit vérifiée
     * @param email : adresse mail de l'utilisateur
     * @param password : mot de passe de l'utilisateur
     * @return : rien
     */
    fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
            requireActivity()
        ) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                if (user != null && user.isEmailVerified) {
                    Toast.makeText(
                        activity,
                        "Connecté à $email",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(activity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    Toast.makeText(
                        activity,
                        "Veuillez vérifier votre adresse e-mail avant de vous connecter",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    activity,
                    "Identifiants inconnus ou incorrects",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}