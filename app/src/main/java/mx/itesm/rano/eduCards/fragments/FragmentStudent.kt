package mx.itesm.rano.eduCards.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import mx.itesm.rano.eduCards.R

class FragmentStudent(course: String, group: String) : Fragment() {
    var user = FirebaseAuth.getInstance().currentUser?.email?.replace(".", "__dot__").toString()
    var course = course
    var group = group
    lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_student, container, false)
        setLayoutVariables()
        setButtons()
        return root
    }

    private fun setLayoutVariables() {
        var groupName = group.split("[", "]")[2]
        var tvSubtitle = root.findViewById<View>(R.id.tvSubtitle) as TextView
        tvSubtitle.text = "Choose a Student from${groupName}"
    }

    private fun setButtons() {
        setStudentActionsButtons()
    }

    private fun setStudentActionsButtons() {
        var btnAddStudent = root.findViewById<View>(R.id.btnAddStudent) as Button
        btnAddStudent.setOnClickListener {
            var fragment = FragmentAddStudent(user, course, group)
            fragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentContainer, fragment)
                ?.addToBackStack(fragment.toString())
                ?.replace(R.id.fragmentContainer, fragment)
                ?.commit()
        }
    }

    fun setWhenNoItemsInList() {
        var tvSubtitle = root.findViewById<View>(R.id.tvSubtitle) as TextView
        tvSubtitle.text = "Waiting for you to add Students..."
    }
}