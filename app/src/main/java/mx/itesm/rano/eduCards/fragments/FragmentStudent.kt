package mx.itesm.rano.eduCards.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import mx.itesm.rano.eduCards.R

class FragmentStudent(course: String, group: String) : Fragment() {
    lateinit var root: View

    var course = course
    var group = group
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_student, container, false)
        setButtons()
        return root
    }

    private fun setButtons() {
        setStudentActionsButtons()
    }

    private fun setStudentActionsButtons() {
        var btnAddStudent = root.findViewById<View>(R.id.btnAddStudent) as Button
        btnAddStudent.setOnClickListener {
            var fragment = FragmentAddStudent(course, group)
            fragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentContainer, fragment)
                ?.addToBackStack(fragment.toString())
                ?.replace(R.id.fragmentContainer, fragment)
                ?.commit()
        }
    }
}