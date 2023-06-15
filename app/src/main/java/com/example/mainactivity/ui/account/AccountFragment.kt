package com.example.mainactivity.ui.account

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.Visibility
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.mainactivity.R
import com.example.mainactivity.WelcomeActivity
import com.example.mainactivity.databinding.FragmentAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase



class AccountFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentAccountBinding? = null
    private var pp:String = ""

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var deconnect : Button
    private lateinit var confirmer : Button
    private lateinit var admin : Button

    //private val pickImage = 100
    //private var imageUri: Uri? = null
    private lateinit var prenom: EditText
    private lateinit var nom: EditText
    private var isChangingPp : Boolean = false

    /**
     * This function is called when the user wants to change his profile picture
     * @return A new instance of fragment AccountFragment.
     */
    @SuppressLint("WrongThread", "MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = FirebaseAuth.getInstance()
        val view =  inflater.inflate(R.layout.fragment_account, container, false)
        prenom = view.findViewById(R.id.prenom)
        nom = view.findViewById(R.id.nom)


        val userDocRef = Firebase.firestore.collection("User")
        userDocRef.whereEqualTo("Mail",auth.currentUser?.email.toString()).get().addOnSuccessListener {
            result->
            for (document in result){
                if(document.data["Role"].toString().contentEquals("/Role/role_Dev")){
                    binding.admin.visibility = View.VISIBLE
                }
                val gg = document.data["pp"].toString()
                gg.let {
                    // Assign the value to the global pp variable
                    pp = it

                    val n = document.data["LastName"].toString()
                    val p = document.data["FirstName"].toString()
                    binding.prenom.setText(p)
                    binding.nom.setText(n)


                    var bitmap : Bitmap
                    bitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.pp1)
                    when (pp) {

                        "1" -> bitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.pp1)
                        "2" -> bitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.pp2)
                        "3" -> bitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.pp3)
                        "4" -> bitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.pp4)
                        "5" -> bitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.pp5)
                        "6" -> bitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.pp6)
                        "7" -> bitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.pp7)
                        "8" -> bitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.pp8)
                        "9" -> bitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.pp9)
                    }
                    binding.imageViewpp.setImageBitmap(bitmap)
                }
            }
        }




        /*
        val accountViewModel =
            ViewModelProvider(this)[AccountViewModel::class.java]

         */

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.imageViewpp.setOnClickListener{
            if(!isChangingPp){
                binding.scrollLayout.isVisible = true
                binding.horizontalScroll.layoutParams.height = resources.getDimensionPixelSize(R.dimen.profilPicture)
                binding.horizontalScroll.requestLayout()
                isChangingPp = true
            }
            else{
                binding.scrollLayout.isVisible = false
                binding.horizontalScroll.layoutParams.height = 1
                binding.horizontalScroll.requestLayout()
                isChangingPp = false
            }
        }

        for(i in 0 until binding.scrollLayout.childCount){
            binding.scrollLayout.children.elementAt(i).setOnClickListener {
                binding.horizontalScroll.layoutParams.height = 1
                binding.scrollLayout.isVisible = false
                binding.horizontalScroll.requestLayout()
                var bitmap : Bitmap
                bitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.pp1)

                when (i) {


                    //binding.imageViewpp.setImageBitmap(bitmap)
                    0 -> bitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.pp1)
                    1 -> bitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.pp2)
                    2 -> bitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.pp3)
                    3 -> bitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.pp4)
                    4 -> bitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.pp5)
                    5 -> bitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.pp6)
                    6 -> bitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.pp7)
                    7 -> bitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.pp8)
                    8 -> bitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.pp9)
                }

                binding.imageViewpp.setImageBitmap(bitmap)

                val docRef = Firebase.firestore.collection("User")
                docRef.whereEqualTo("Mail",auth.currentUser?.email.toString()).get().addOnSuccessListener { result ->
                    for (document in result) {
                        docRef.document(document.id).update("pp", i+1)
                    }
                }

                //bdd ici !


            }
        }

        deconnect = binding.deconnect
        deconnect.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(
                activity,
                "Déconnexion réussie",
                Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(activity, WelcomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            requireActivity().finish()

        }

        //TESTS
        confirmer = binding.confirmer
        confirmer.setOnClickListener{
            val txtPrenom = binding.prenom.text
            val txtNom = binding.nom.text
            val docRef = Firebase.firestore.collection("User")

            if (TextUtils.isEmpty(txtNom) || TextUtils.isEmpty(txtPrenom)) //Vérification que les champs ne sont pas vides
                Toast.makeText(
                    activity,
                    "Champs vides !",
                    Toast.LENGTH_SHORT
                ).show()

            else
                docRef.whereEqualTo("Mail",auth.currentUser?.email.toString()).get().addOnSuccessListener { result ->
                    for (document in result) {
                        docRef.document(document.id).update("FirstName", txtPrenom.toString())
                        docRef.document(document.id).update("LastName", txtNom.toString())

                }

                    //val intent = Intent(activity, MainActivity::class.java)
                    //startActivity(intent)
            }
        }

        admin = binding.admin
        admin.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main).navigate(R.id.action_navigation_account_to_adminFragment)
        }

        return root
    }

    /**
     * When the view is destroyed, the binding is set to null
     * to avoid memory leaks
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}