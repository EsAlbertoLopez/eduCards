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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import mx.itesm.rano.eduCards.R
import mx.itesm.rano.eduCards.activities.MainActivity
import mx.itesm.rano.eduCards.models.Instructor


class FragmentSignIn : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    lateinit var root: View
    lateinit var inflater: LayoutInflater
    lateinit var mainActivity: MainActivity
    lateinit var database: FirebaseDatabase

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
        database= FirebaseDatabase.getInstance()
        makeLogIn()
        return root
    }

    private fun makeLogIn(){
        val etEmail = root.findViewById<View>(R.id.editTextTextEmail) as EditText
        val etUsername=root.findViewById<View>(R.id.editTextTextUsername) as EditText
        val etPassword=root.findViewById<View>(R.id.editTextTextPassword) as EditText
        val btnSignIn=root.findViewById<View>(R.id.btnSignIn) as Button
        btnSignIn.setOnClickListener{
            if(mainActivity.loginFlag==false) {
                btnSignIn.setText("SIGN IN")
                if (etEmail != null) {
                    if (etPassword != null) {
                        verifyAccount(etUsername.text.toString(), etEmail.text.toString(), etPassword.text.toString())
                    }
                }
            }
        }
    }

    private fun verifyAccount(username: String, email: String, password: String) {
        var reference = database.getReference("/Instructors/$username")
        reference.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                for (record in snapshot.children) {
                    val instructor = record.getValue() as String
                    print(instructor)
                    if (instructor != null) {
                        if (instructor.contains(email)) {
                            signIn(email, password)
                        } else {
                            print("Failed to verify")
                        }
                    }
                }
            }

        })
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