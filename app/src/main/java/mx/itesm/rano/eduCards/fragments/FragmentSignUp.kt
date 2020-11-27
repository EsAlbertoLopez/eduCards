package mx.itesm.rano.eduCards.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
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
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_about.*
import kotlinx.android.synthetic.main.fragment_sign_in.*
import mx.itesm.rano.eduCards.R
import mx.itesm.rano.eduCards.activities.MainActivity
import mx.itesm.rano.eduCards.models.Instructor


class FragmentSignUp : Fragment() {
    private lateinit var root: View
    private lateinit var inflater: LayoutInflater
    private lateinit var mainActivity: MainActivity
    private lateinit var tvSubtitle : TextView
    private lateinit var etInstitute: EditText
    private lateinit var etInstructor: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignUp: Button
    private lateinit var connectivityChangeReceiver: BroadcastReceiver
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var database: FirebaseDatabase
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        //updateUI(currentUser)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.inflater = inflater
        root = inflater.inflate(R.layout.fragment_sign_up, container, false)
        mainActivity = context as MainActivity
        connectivityManager = mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        setLayoutVariables()
        setConnectivityChangeReceiver()
        checkConnectivityStatus()
        setSignUpButton()
        setReportDetails()
        return root
    }

    private fun setLayoutVariables() {
        tvSubtitle = root.findViewById<View>(R.id.tvSubtitle) as TextView
        etInstitute = root.findViewById<View>(R.id.editTextTextInstitute) as EditText
        etInstitute.error = "Blank Institute Field"
        etInstructor = root.findViewById<View>(R.id.editTextTextInstructorName) as EditText
        etInstructor.error = "Blank Instructor Field"
        etEmail = root.findViewById<View>(R.id.editTextTextEmail) as EditText
        etEmail.error = "Blank Email Field"
        etPassword = root.findViewById<View>(R.id.editTextTextPassword) as EditText
        etPassword.error = "Blank Password Field"
    }

    private fun setSignUpButton() {
        btnSignUp = root.findViewById<View>(R.id.btnSignUp) as Button
        btnSignUp.setOnClickListener {
            val institute = etInstitute.text.toString()
            val instructor = etInstructor.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            if (etInstitute.error == null
                && etInstructor.error == null
                && etEmail.error == null
                && etPassword.error == null) {
                signUpAccount(email, instructor, institute, password, email)
            } else {
                if (institute.isNotEmpty()) {
                    etInstitute.error == null
                } else {
                    etInstitute.error = "Institute is an Empty Field"
                }
                if (instructor.isNotEmpty()) {
                    etInstructor.error = null
                } else {
                    etInstructor.error = "Instructor is an Empty Field"
                }
                if (email.isNotEmpty()) {
                    etEmail.error = "Email is an Empty Field"
                } else {
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
                }
                if (password.isNotEmpty()) {
                    etPassword.error = "Error : Password is an Empty Field"
                } else {
                    if (password.length >= 6) {
                        etPassword.error = null
                    } else {
                        etPassword.error = "Password length cannot be less than 6 characters"
                    }
                }
            }
        }
        btnSignUp.isEnabled = false
    }

    private fun signUpAccount(username: String, instructor: String, institute: String, password: String, email: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this.context as MainActivity,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        println("createUserWithEmail:success")
                        val user = mAuth.currentUser
                        writeDataToCloud(username.toLowerCase(), instructor, institute, email)
                        user?.sendEmailVerification()
                        updateUI(user)
                        Toast.makeText(
                            this.context as MainActivity, "An email has been sent to verify your account",
                            Toast.LENGTH_LONG
                        ).show()
                        mAuth.signOut()
                    } else {
                        // If sign in fails, display a message to the user.
                        println("createUserWithEmail:failure ${task.exception}")
                        Toast.makeText(
                            this.context as MainActivity, "Sign Up failed: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                        updateUI(null)
                    }
                    // ...
                })
    }

    private fun writeDataToCloud(username: String, instructor: String, institute: String, email: String) {
        val instructor = Instructor(username, instructor, institute, email)
        val emailNoDots = (email.replace(".", "__dot__")).toLowerCase()
        val reference = database.getReference("/Instructors/$emailNoDots")
        reference.setValue(instructor)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            println("Successful Sign Up Attempt!")
            println("Waiting for Email Confirmation for Signed Up User : ${currentUser?.displayName}")
            etInstitute.isEnabled = false
            etInstructor.isEnabled = false
            etEmail.isEnabled = false
            etPassword.isEnabled = false
            btnSignUp.isEnabled = false
        } else {
            print("Failed Sign Up Attempt")
        }
    }

    private fun setReportDetails() {
        etInstitute.addTextChangedListener (object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkConnectivityStatus()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Fires right before text is changing
            }

            override fun afterTextChanged(s: Editable) {
                val institute = etInstitute.text.toString()
                if (institute.isNotEmpty()) {
                    etInstitute.error == null
                } else {
                    etInstitute.error = "Institute cannot be an Empty Field"
                }
                verifyUserInputs()
            }
        })
        etInstitute.isEnabled = true
        etInstructor.addTextChangedListener (object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkConnectivityStatus()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Fires right before text is changing
            }

            override fun afterTextChanged(s: Editable) {
                val instructor = etInstructor.text.toString()
                if (instructor.isNotEmpty()) {
                    etInstructor.error = null
                } else {
                    etInstructor.error = "Instructor cannot be an Empty Field"
                }
                verifyUserInputs()
            }
        })
        etInstructor.isEnabled = true
        etEmail.addTextChangedListener (object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkConnectivityStatus()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Fires right before text is changing
            }

            override fun afterTextChanged(s: Editable) {
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
                    etEmail.error = "Email cannot be an Empty Field"
                }
                verifyUserInputs()
            }
        })
        etEmail.isEnabled = true
        etPassword.isEnabled = true
        etPassword.addTextChangedListener (object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkConnectivityStatus()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Fires right before text is changing
            }

            override fun afterTextChanged(s: Editable) {
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
        btnSignUp.isEnabled = !(etInstitute.error != null
                || etInstructor.error != null
                || etEmail.error != null
                || etPassword.error != null
                || !checkConnectivityStatus())
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