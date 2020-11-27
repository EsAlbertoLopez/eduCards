package mx.itesm.rano.eduCards.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_card_detail.*
import kotlinx.android.synthetic.main.fragment_home.*
import mx.itesm.rano.eduCards.R
import mx.itesm.rano.eduCards.models.Instructor
import java.text.DateFormat
import java.util.*

class FragmentHome : Fragment() {
    var user = FirebaseAuth.getInstance().currentUser?.email?.replace(".", "__dot__").toString()
    private val messages = mutableListOf<String>(
        "Buen día", "¡Suerte!", "¿Queso?",
        "Espero que estés bien", "Taquitos", "Godspeed, Watts Humphrey",
        "Bienvenido", "¿Dónde estabas?", "¿Con quién hablabas?",
        "¡Gracias, Tuxe!", "¡Gracias, Atena!", "¡Gracias, Frank!",
        "¡Gracias, Lola!", "Listo para trabajar", "Sasquatch",
        "Ameno", "Descarga D-DEF", "Gracias, RMRomán",
        "Hecho por Raíl Ortíz Mateos", "Hecho por Nataly P. López", "Hecho por Oswaldo Morales",
        "Hecho por Alberto López Reyes", "Hecho por RANO", "Quality Assurance, baby!",
        "Usa Cubrebocas", "¡Hola, Ray!", "¡Hola, Val!"
    )
    lateinit var root: View
    lateinit var tvSubtitle: TextView
    lateinit var tvName: TextView
    lateinit var tvInstitute: TextView
    lateinit var tvDay: TextView
    lateinit var tvEmail: TextView

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
        readDataFromCloud()
        return root
    }

    private fun setLayoutVariables() {
        setLayHeadingVariables()
        setUserHeadingVariables()
    }

    private fun setLayHeadingVariables() {
        setWelcomeMessage()
    }

    private fun setUserHeadingVariables() {
        tvName = root.findViewById(R.id.tvName)
        tvInstitute = root.findViewById(R.id.tvInstitute)
        tvEmail = root.findViewById(R.id.tvEmail)

        tvDay = root.findViewById(R.id.tvDay)
        var calendar = Calendar.getInstance()
        var currentDate = DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.time)
        tvDay.text = currentDate
    }

    private fun readDataFromCloud() {
        val database = FirebaseDatabase.getInstance()
        val userReference = database.getReference("Instructors/$user")
        userReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val person = snapshot.getValue(Instructor::class.java)
                if (person != null) {
                    tvName.text = person.name
                    tvInstitute.text = person.institute
                    tvEmail.text = person.email
                }
                if (view != null){
                    print("Null view")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun setWelcomeMessage(){
        val random = (0 until (messages.size - 1)).random()
        val tvSubtitle = root.findViewById(R.id.tvSubtitle) as TextView
        tvSubtitle.text = messages.get(random)
    }
}