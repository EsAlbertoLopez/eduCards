package mx.itesm.rano.eduCards.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
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
        mAuth = FirebaseAuth.getInstance()
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
        connectivityManager = mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        signedIn = mainActivity.loginFlag
        database = FirebaseDatabase.getInstance()
        setLayoutVariables()
        setConnectivityChangeReceiver()
        checkConnectivityStatus()
        setLogInButton()
        setReportDetails()
        return root
    }

    private fun setLayoutVariables() {
        tvSubtitle = root.findViewById<View>(R.id.tvSubtitle) as TextView
        etEmail = root.findViewById<View>(R.id.editTextTextEmail) as EditText
        etEmail.error = "Blank Email Field"
        etPassword = root.findViewById<View>(R.id.editTextTextPassword) as EditText
        etPassword.error = "Blank Password Field"
    }

    private fun setLogInButton() {
        btnSignIn = root.findViewById<View>(R.id.btnSignIn) as Button
        btnSignIn.setOnClickListener{
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            if(mainActivity.loginFlag == false) {
                btnSignIn.setText("SIGN IN")
                if (etEmail != null) {
                    if (etPassword != null) {
                        verifyAccount(etEmail.text.toString(), etPassword.text.toString())
                    }
                }
                if (etEmail.error == null
                    && etPassword.error == null) {
                    verifyAccount(etEmail.text.toString(), etPassword.text.toString())
                } else {
                    if (email.isNotEmpty()) {
                        etEmail.error = null
                        if (email.contains("#")
                            || email.contains("$")
                            || email.contains("[")
                            || email.contains("]")
                            || email.contains(" ")
                        ) {
                            etEmail.error = "Email cannot contain ., #, $, [, ] and ' '"
                        } else {
                            etEmail.error = null
                        }
                    } else {
                        etEmail.error = "Email is an Empty Field"
                    }
                    if (password.isNotEmpty()) {
                        if (password.length >= 6) {
                            etPassword.error = null
                        } else {
                            etPassword.error = "Password length cannot be less than 6 characters"
                        }
                    } else {
                        etPassword.error = "Error : Password is an Empty Field"
                    }
                }
            }
        }
        btnSignIn.isEnabled = false
    }

    private fun setReportDetails() {
        etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                checkConnectivityStatus()
            }

            override fun afterTextChanged(p0: Editable?) {
                val email = etEmail.text.toString()
                if (email.isNotEmpty()) {
                    if (email.contains("#")
                        || email.contains("$")
                        || email.contains("[")
                        || email.contains("]")
                        || email.contains(" ")
                    ) {
                        etEmail.error = "Email cannot contain ., #, $, [, ] and ' '"
                    } else {
                        etEmail.error = null
                    }
                } else {
                    etEmail.error = "Email cannot ba an Empty Field"
                }
                verifyUserInputs()
            }
        })
        etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                checkConnectivityStatus()
            }

            override fun afterTextChanged(p0: Editable?) {
                val password = etPassword.text.toString()
                if (password.isNotEmpty()) {
                    if (password.length >= 6) {
                        etPassword.error = null
                    } else {
                        etPassword.error = "Password length cannot be less than 6 characters"
                    }
                } else {
                    etPassword.error = "Password is an Empty Field"
                }
                verifyUserInputs()
            }
        })
    }

    private fun verifyUserInputs() {
        btnSignIn.isEnabled = !(etEmail.error != null
                || etPassword.error != null
                || !checkConnectivityStatus())
    }

    private fun verifyAccount(email: String, password: String) {
        var emailNodots = (email.replace(".", "__dot__")).toLowerCase()
        var reference = database.getReference("/Instructors/$emailNodots")
        reference.addValueEventListener(object : ValueEventListener {
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
                    if (recordedEmail.toLowerCase() == email.toLowerCase()) {
                        println("recordedEmail $recordedEmail")
                        if (signedIn == false) {
                            signIn(recordedName, email, password)
                        }
                    } else {
                        print("Failed to verify")
                    }
                }
            }

        })
    }

    private fun signIn(name: String, email: String, password: String){
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
                                println("hola ${user.email}")

                                Toast.makeText(
                                    mainActivity,
                                    "Authentication succeeded",
                                    Toast.LENGTH_LONG
                                ).show()
                                mainActivity.instructor = name
                                mainActivity.printPug()
                                updateUI(user)
                                signedIn = true
                                mainActivity.bottomNavBar.menu.performIdentifierAction(
                                    R.id.settings,
                                    0
                                )
                                //var fragment = FragmentHome()
                                //fragmentManager?.beginTransaction()
                                //    ?.replace(R.id.fragmentContainer, fragment)
                                //    ?.commit()
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
                        Toast.makeText(
                            mainActivity,
                            "Authentication failed: ${task.exception?.localizedMessage}",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateUI(null)
                    }
                })
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser != null) {
            print("Signed In User: ${currentUser?.displayName}")
            etEmail.isEnabled = false
            etPassword.isEnabled = false
        }
        else{
            print("No Sign In Account has been found")
        }
    }

    private fun setConnectivityChangeReceiver() {
        connectivityChangeReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                if (p1?.action.equals("android.net.conn.CONNECTIVITY_CHANGE", false)) {
                    checkConnectivityStatus()
                }
            }
        }
    }

    private fun checkConnectivityStatus(): Boolean {
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        if (!isConnected) {
            tvSubtitle.text = "You are not connected"
            tvSubtitle.setTextColor(resources.getColor(R.color.colorWarning))
        } else {
            //tvSubtitle.setTextColor(resources.getColor(android:attr/textColorPrimary))
            tvSubtitle.text = "Create your Account"
            tvSubtitle.setTextColor(mainActivity.resolveColorAttr(mainActivity, android.R.attr.textColorPrimary))
        }
        return isConnected
    }
}