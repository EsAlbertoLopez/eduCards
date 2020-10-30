package mx.itesm.rano.eduCards.fragments

import android.content.Context
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
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignUp: Button
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
        checkConnectivityStatus()
        setSignUpButton()
        setReportDetails()
        return root

    }

    private fun setLayoutVariables() {
        tvSubtitle = root.findViewById<View>(R.id.tvSubtitle) as TextView
        etInstitute = root.findViewById<View>(R.id.editTextTextInstitute) as EditText
        etInstructor = root.findViewById<View>(R.id.editTextTextInstructorName) as EditText
        etEmail = root.findViewById<View>(R.id.editTextTextEmail) as EditText
        etUsername = root.findViewById<View>(R.id.editTextTextUsername) as EditText
        etPassword = root.findViewById<View>(R.id.editTextTextPassword) as EditText
    }

    private fun setSignUpButton() {
        btnSignUp = root.findViewById<View>(R.id.btnSignUp) as Button
        btnSignUp.setOnClickListener {
            if (etEmail != null) {
                if (etPassword != null) {
                    val institute = etInstitute.text.toString()
                    val instructor = etInstructor.text.toString()
                    val email = etEmail.text.toString()
                    val username = etUsername.text.toString()
                    val password = etPassword.text.toString()
                    if (institute.isNotEmpty()
                        || instructor.isNotEmpty()
                        || email.isNotEmpty()
                        || username.isNotEmpty()
                        || password.isNotEmpty()) {
                        if (username.contains(".")
                            || username.contains("#")
                            || username.contains("$")
                            || username.contains("[")
                            || username.contains("]")
                            || username.contains(" ")) {
                            Toast.makeText(context, "Error: Username cannot contain ., #, $, [, ] and ' '", Toast.LENGTH_LONG)
                                .show()
                        } else {
                            if (password.length >= 6) {
                                signUpAccount(username, instructor, institute, password, email)
                            } else {
                                Toast.makeText(context, "Error: Password length cannot be less than 6 characters", Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Error: Empty Sign Up field(s)", Toast.LENGTH_LONG)
                            .show()
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
        val reference = database.getReference("/Instructors/$username")
        reference.setValue(instructor)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            println("Successful Sign Up Attempt!")
            println("Waiting for Email Confirmation for Signed Up User : ${currentUser?.displayName}")
            etInstitute.isEnabled = false
            etInstructor.isEnabled = false
            etEmail.isEnabled = false
            etUsername.isEnabled = false
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
                btnSignUp.isEnabled = !((etInstitute.text.isEmpty()
                        || etInstitute.text.isEmpty()
                        || etInstructor.text.isEmpty()
                        || etEmail.text.isEmpty()
                        || etUsername.text.isEmpty()
                        || etPassword.text.isEmpty())
                        || !checkConnectivityStatus())
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
                btnSignUp.isEnabled = !((etInstitute.text.isEmpty()
                        || etInstitute.text.isEmpty()
                        || etInstructor.text.isEmpty()
                        || etEmail.text.isEmpty()
                        || etUsername.text.isEmpty()
                        || etPassword.text.isEmpty())
                        || !checkConnectivityStatus())
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
                btnSignUp.isEnabled = !((etInstitute.text.isEmpty()
                        || etInstitute.text.isEmpty()
                        || etInstructor.text.isEmpty()
                        || etEmail.text.isEmpty()
                        || etUsername.text.isEmpty()
                        || etPassword.text.isEmpty())
                        || !checkConnectivityStatus())
            }
        })
        etEmail.isEnabled = true
        etUsername.addTextChangedListener (object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkConnectivityStatus()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Fires right before text is changing
            }

            override fun afterTextChanged(s: Editable) {
                btnSignUp.isEnabled = !((etInstitute.text.isEmpty()
                        || etInstitute.text.isEmpty()
                        || etInstructor.text.isEmpty()
                        || etEmail.text.isEmpty()
                        || etUsername.text.isEmpty()
                        || etPassword.text.isEmpty())
                        || !checkConnectivityStatus())
            }
        })
        etPassword.isEnabled = true
        etPassword.addTextChangedListener (object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkConnectivityStatus()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Fires right before text is changing
            }

            override fun afterTextChanged(s: Editable) {
                btnSignUp.isEnabled = !((etInstitute.text.isEmpty()
                        || etInstitute.text.isEmpty()
                        || etInstructor.text.isEmpty()
                        || etEmail.text.isEmpty()
                        || etUsername.text.isEmpty()
                        || etPassword.text.isEmpty())
                        || !checkConnectivityStatus())
            }
        })
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
        }
        return isConnected
    }
}