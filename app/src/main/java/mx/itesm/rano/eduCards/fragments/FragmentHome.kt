package mx.itesm.rano.eduCards.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_home.*
import mx.itesm.rano.eduCards.R

class FragmentHome : Fragment() {

    private val messages = mutableListOf<String>(
        "Buen día", "¡Suerte!", "¿Queso?",
        "Espero que estés bien", "Taquitos", "Godspeed, Watts Humphrey",
        "Bienvenido", "¿Dónde estabas?", "¿Con quién hablabas?",
        "¡Gracias, Tuxe!", "¡Gracias, Atena!", "¡Gracias, Frank!",
        "¡Gracias, Lola!", "Listo para trabajar", "Sasquatch",
        "Ameno", "Descarga D-DEF", "Gracias, RMRomán",
        "Hecho por Raíl Ortíz Mateos", "Hecho por Nataly P. López", "Hecho por Oswaldo Morales",
        "Hecho por Alberto López Reyes", "Hecho por RANO", "Quality Assurance, baby!"
    )
    lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_home, container, false)
        setLayoutVariables()
        return root
    }

    private fun setLayoutVariables() {
        setWelcomeMessage()
    }

    private fun setWelcomeMessage(){
        val random = (0 until (messages.size - 1)).random()
        val tvSubtitle = root.findViewById(R.id.tvSubtitle) as TextView
        tvSubtitle.text = messages.get(random)
    }
}