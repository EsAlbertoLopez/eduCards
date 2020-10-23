package mx.itesm.rano.eduCards.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mx.itesm.rano.eduCards.R
import mx.itesm.rano.eduCards.activities.MainActivity


class FragmentSignIn : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var mainActivity = context as MainActivity
        mainActivity.printPug()
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }
}