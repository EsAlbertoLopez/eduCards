package mx.itesm.rano.eduCards.fragments

import android.R.attr
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


class FragmentSignIn : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    lateinit var root: View
    lateinit var inflater: LayoutInflater
    lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth=FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser != null) {
            print("User: ${currentUser?.displayName}")
        }
        else{
            print("No has hecho log in")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.inflater = inflater
        root = inflater.inflate(R.layout.fragment_sign_in, container, false)
        mainActivity = context as MainActivity
        makeLogIn()
        return root
    }
    private fun makeLogIn(){
        val email = root.findViewById<View>(R.id.editTextTextOrganizationID) as EditText
        val password=root.findViewById<View>(R.id.editTextTextPassword) as EditText
        val btnSignIn=root.findViewById<View>(R.id.btnSignIn) as Button
        btnSignIn.setOnClickListener{
            if(mainActivity.loginFlag==false) {
                btnSignIn.setText("SIGN IN")
                if (email != null) {
                    if (password != null) {
                        signIn(email.text.toString(), password.text.toString())
                    }
                }
            }
        }
    }


    private fun signIn(email: String, password: String){

        mAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this.context as MainActivity,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        println("signInWithEmail:success")
                        mainActivity.activateApplication("Settings")
                        mainActivity.loginFlag = true
                        Toast.makeText(mainActivity,
                        "Authentication succeeded",
                        Toast.LENGTH_SHORT).show()
                        val user: FirebaseUser? = mAuth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        println("signInWithEmail:failure ${task.exception}")
                        Toast.makeText(mainActivity,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }

                    // ...
                })

    }


}