package mx.itesm.rano.eduCards.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import mx.itesm.rano.eduCards.R
import mx.itesm.rano.eduCards.activities.MainActivity


class FragmentSignUp : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    lateinit var root: View
    lateinit var inflater: LayoutInflater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()


    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser != null) {
            println("Login succsesfull")
            print("User: ${currentUser?.displayName}")
        }
        else{
            print("No has hecho log in")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.inflater = inflater
        root = inflater.inflate(R.layout.fragment_sign_up, container, false)
        createAccounts()
        return root

    }
    private fun createAccounts(){
        val email = root.findViewById<View>(R.id.editTextTextOrganizationID) as EditText
        val password=root.findViewById<View>(R.id.editTextTextPassword) as EditText
        val btnSignUp=root.findViewById<View>(R.id.btnSignUp) as Button
        btnSignUp.setOnClickListener{
            if (email != null) {
                if (password != null) {
                    createAccount(email.text.toString(),password.text.toString())
                }
            }
        }

    }






    fun createAccount(email: String, password: String){

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this.context as MainActivity,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        println("createUserWithEmail:success")
                        val user = mAuth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        println("createUserWithEmail:failure ${ task.exception}")
                        Toast.makeText(
                            this.context as MainActivity, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateUI(null)
                    }

                    // ...
                })

    }
}