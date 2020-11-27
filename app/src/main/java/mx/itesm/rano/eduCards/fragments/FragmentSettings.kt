package mx.itesm.rano.eduCards.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import mx.itesm.rano.eduCards.R
import mx.itesm.rano.eduCards.activities.MainActivity

class FragmentSettings : Fragment() {
    lateinit var root: View
    lateinit var mainActivity: MainActivity
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_settings, container, false)
        mainActivity = context as MainActivity
        setButtons()
        return root
    }

    private fun setButtons() {
        setProfileActionsButtons()
    }

    private fun setProfileActionsButtons() {
        setSignInButton()
        setSignUpButton()
        setAboutButton()
    }

    private fun setSignInButton() {
        val button = root.findViewById<View>(R.id.btnSignInOut) as Button
        if (FirebaseAuth.getInstance().currentUser == null) {
            button.setText("Sign In")
        } else {
            button.setText("Sign Out")
        }
        button.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser == null) {
                val fragment = FragmentSignIn()
                fragmentManager?.beginTransaction()
                    ?.replace(R.id.fragmentContainer, fragment)
                    ?.addToBackStack(fragment.toString())
                    ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    ?.commit()
                setSignUpButton()
            } else {
                mAuth = FirebaseAuth.getInstance()
                mAuth.signOut()
                button.setText("Sign In")
                mainActivity.loginFlag=false
                mainActivity.deactivateApplication("Settings")
                setSignUpButton()
            }

        }
    }

    private fun setSignUpButton() {
        val button = root.findViewById<View>(R.id.btnSignUp) as Button
        button.setOnClickListener {
            val fragment = FragmentSignUp()
            fragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentContainer, fragment)
                ?.addToBackStack(fragment.toString())
                ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                ?.commit()
        }
        button.isEnabled = FirebaseAuth.getInstance().currentUser == null
    }

    private fun setAboutButton(){
        val button = root.findViewById<View>(R.id.btnAbout) as Button
        button.setOnClickListener {
            val fragment = FragmentAbout()
            fragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentContainer,fragment)
                ?.addToBackStack(fragment.toString())
                ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                ?.commit()
        }
    }
}