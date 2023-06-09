package com.example.mainactivity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var change: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_login, container, false)

        email = view.findViewById(R.id.email)
        password = view.findViewById(R.id.password)
        login = view.findViewById(R.id.register)
        change = view.findViewById(R.id.change)
        auth = FirebaseAuth.getInstance()

        login.setOnClickListener {
            val txtEmail = email.text.toString()
            val txtPassword = password.text.toString()

            loginUser(txtEmail, txtPassword)
        }

        change.setOnClickListener {
            view.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        return view
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