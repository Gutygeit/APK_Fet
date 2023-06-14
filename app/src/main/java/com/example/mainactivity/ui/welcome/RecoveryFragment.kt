package com.example.mainactivity.ui.welcome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.mainactivity.R
import com.google.firebase.auth.FirebaseAuth

class RecoveryFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_recovery, container, false)

        val email = view.findViewById<EditText>(R.id.email)
        val recovery = view.findViewById<Button>(R.id.recovery)

        auth = FirebaseAuth.getInstance()

        recovery.setOnClickListener {
            if (email.text.toString() != "") {
                auth.sendPasswordResetEmail(email.text.toString()).addOnCompleteListener(
                    requireActivity()
                ) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            activity,
                            "Un e-mail de réinitialisation a été envoyé à ${email.text}",
                            Toast.LENGTH_LONG
                        ).show()
                        view.findNavController()
                            .navigate(R.id.action_recoveryFragment_to_loginFragment)
                    } else {
                        Toast.makeText(
                            activity,
                            "Erreur lors de l'envoi de l'e-mail de réinitialisation",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    activity,
                    "Veuillez remplir le champ",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return view
    }
}