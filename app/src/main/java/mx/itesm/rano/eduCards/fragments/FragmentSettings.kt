package mx.itesm.rano.eduCards.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_main.view.*
import mx.itesm.rano.eduCards.R

class FragmentSettings : Fragment() {
    lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_settings, container, false)
        setButtons()
        return root
    }

    private fun setButtons() {
        setProfileActionsButtons()
    }

    private fun setProfileActionsButtons() {
        setSignInButton()
        setAbouButton()
    }

    private fun setSignInButton() {
        val button = root.findViewById<View>(R.id.btnSignInOut) as Button
        button.setOnClickListener {
            val fragment = FragmentSignIn()
            fragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentContainer, fragment)
                ?.addToBackStack(fragment.toString())
                ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                ?.commit()
        }
    }

    private fun setAbouButton(){
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