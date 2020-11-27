package mx.itesm.rano.eduCards.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_home.*
import mx.itesm.rano.eduCards.R

class FragmentHome : Fragment() {

    private val p = mutableListOf<String>("Buen día","suerte!","queso")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        changePhrases()
        return inflater.inflate(R.layout.fragment_home, container, false)


    }

    private fun changePhrases(){
        val random = (0 until (p.size)).random()
        tvSubtitle.text = p[random]

    }






}