package mx.itesm.rano.eduCards.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_card_detail.*
import mx.itesm.rano.eduCards.R
import mx.itesm.rano.eduCards.models.Card
import mx.itesm.rano.eduCards.models.Student

class FragmentCardDetail(selectedCourse: String, selectedGroup: String, selectedStudent: String, keyEvent: String) : Fragment() {

    var course = selectedCourse
    var group = selectedGroup
    var student = selectedStudent
    var key = keyEvent
    var cause = ""
    var description = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        readFromcloud()

        super.onStart()

    }

    private fun readFromcloud() {
        val courseId = course.split("[", "]")[1]
        val groupId = group.split("[", "]")[1]
        val studentId = student.split("[", "]")[1]
        val database = FirebaseDatabase.getInstance()

        val reference = database.getReference("/Courses/$courseId/Groups/$groupId/Alumnos/$studentId/Events/$key")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                val card = snapshot.getValue(Card::class.java)
                if (card != null){
                    cause = card.type
                    description = card.description
                }

                if(view != null) {
                    tvTitle.text = cause
                    tvDetail.text = description
                    when(cause){
                        "Violencia Física" ->  view!!.setBackgroundColor(Color.RED)
                        "Violencia Verbal" -> view!!.setBackgroundColor(Color.rgb(255, 69, 0))
                        "Comportamiento Inadecuado" -> view!!.setBackgroundColor(Color.rgb(255, 20, 147))
                        "Logro" -> view!!.setBackgroundColor(Color.YELLOW)
                        "Empatía" -> view!!.setBackgroundColor(Color.GREEN)
                        else-> view!!.setBackgroundColor(Color.CYAN)

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
            }

        })
    }



    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card_detail, container, false)
    }
}