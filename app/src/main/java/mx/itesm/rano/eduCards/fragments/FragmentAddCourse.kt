package mx.itesm.rano.eduCards.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import mx.itesm.rano.eduCards.R

class FragmentAddCourse : Fragment() {
    lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_add_course, container, false)
        setNewCourseDetailsButtons()
        return root
    }

    private fun setNewCourseDetailsButtons() {
        setSubmitNewCourseButton()
    }

    private fun setSubmitNewCourseButton() {
        var btnSubmitNewCourse = root.findViewById<View>(R.id.btnSubmitNewCourse) as Button
        btnSubmitNewCourse.setOnClickListener {
            // Sustraer datos de los textFields para crear una nueva instancia de data class de Course,
            // y enviar esos datos a Firebase
        }
    }
}