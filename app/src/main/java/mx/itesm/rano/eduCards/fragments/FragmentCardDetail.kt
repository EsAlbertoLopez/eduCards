package mx.itesm.rano.eduCards.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_card_detail.*
import mx.itesm.rano.eduCards.R
import mx.itesm.rano.eduCards.models.Card

class FragmentCardDetail(selectedCourse: String, selectedGroup: String, selectedStudent: String, keyEvent: String) : Fragment() {
    var course = selectedCourse
    var group = selectedGroup
    var student = selectedStudent
    var key = keyEvent
    var cause = ""
    var description = ""
    var author = ""
    var date = ""
    var time = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        readDataFromcloud()
        super.onStart()
    }

    private fun readDataFromcloud() {
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
                    author = card.author
                    date = card.date
                    time = card.time
                }
                if(view != null) {
                    tvStudentName.text = student
                    tvCardType.text = cause
                    tvCardDescription.text = description
                    tvCardAuthor.text = author
                    tvCardDate.text = date
                    tvCardTime.text = time
                    when(cause){
                        "Violencia Física" ->  view!!.setBackgroundColor(resources.getColor(R.color.card1))
                        "Violencia Verbal" -> view!!.setBackgroundColor(resources.getColor(R.color.card2))
                        "Comportamiento Inadecuado" -> view!!.setBackgroundColor(resources.getColor(R.color.card3))
                        "Logro" -> view!!.setBackgroundColor(resources.getColor(R.color.card4))
                        "Empatía" -> view!!.setBackgroundColor(resources.getColor(R.color.card5))
                        else-> view!!.setBackgroundColor(resources.getColor(R.color.card6))
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