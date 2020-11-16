package mx.itesm.rano.eduCards.fragments

import android.content.BroadcastReceiver
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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
    private lateinit var root: View
    private lateinit var mainActivity: MainActivity
    private lateinit var tvSubtitle : TextView
    private lateinit var etEmail: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignIn: Button
    private lateinit var connectivityChangeReceiver: BroadcastReceiver
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var inflater: LayoutInflater
    private lateinit var database: FirebaseDatabase
    private lateinit var mAuth: FirebaseAuth
    var signedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth=FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.inflater = inflater
        root = inflater.inflate(R.layout.fragment_sign_in, container, false)
        mainActivity = context as MainActivity
        signedIn = mainActivity.loginFlag
        database = FirebaseDatabase.getInstance()
        setLayoutVariables()
        setLogInButton()
        return root
    }

    private fun setLayoutVariables() {
        tvSubtitle = root.findViewById<View>(R.id.tvSubtitle) as TextView
        etEmail = root.findViewById<View>(R.id.editTextTextEmail) as EditText
        etUsername = root.findViewById<View>(R.id.editTextTextUsername) as EditText
        etPassword = root.findViewById<View>(R.id.editTextTextPassword) as EditText
    }

    private fun setLogInButton() {
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
                Toast.makeText(
                    mainActivity,
                    "Error: $error",
                    Toast.LENGTH_LONG
                ).show()
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val instructor = snapshot.getValue(Instructor::class.java)
                if (instructor != null) {
                    var recordedEmail = instructor.email
                    var recordedName = instructor.name
                    if (recordedEmail == email) {
                        println("recordedEmail $recordedEmail")
                        if (signedIn == false) {
                            signIn(username, recordedName, email, password)
                        }
                    } else {
                        print("Failed to verify")
                    }
                }
        }

        })
    }

    private fun signIn(username: String, name: String, email: String, password: String){
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this.context as MainActivity,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user: FirebaseUser? = mAuth.currentUser
                        if (user != null) {
                            if (user.isEmailVerified) {
                                println("signInWithEmail:success")
                                mainActivity.activateApplication("Settings")
                                mainActivity.loginFlag = true
                                Toast.makeText(mainActivity,
                                    "Authentication succeeded",
                                    Toast.LENGTH_LONG).show()
                                mainActivity.username = username
                                mainActivity.instructor = name
                                mainActivity.printPug()
                                updateUI(user)
                                signedIn = true
                                var fragment = FragmentHome()
                                fragmentManager?.beginTransaction()
                                    ?.replace(R.id.fragmentContainer, fragment)
                                    ?.addToBackStack(fragment.toString())
                                    ?.replace(R.id.fragmentContainer, fragment)
                                    ?.commit()
                            } else {
                                Toast.makeText(
                                    mainActivity,
                                    "Verify your account to fire up eduCards",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        println("signInWithEmail:failure ${task.exception}")
                        Toast.makeText(mainActivity,
                            "Authentication failed: ${task.exception?.localizedMessage}",
                            Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }

                    // ...
                })

    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser != null) {
            print("Signed In User: ${currentUser?.displayName}")
        }
        else{
            print("No Sign In Account has been foud")
        }
    }

}