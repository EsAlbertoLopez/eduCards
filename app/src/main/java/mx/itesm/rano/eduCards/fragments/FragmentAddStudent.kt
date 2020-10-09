package mx.itesm.rano.eduCards.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import mx.itesm.rano.eduCards.R

class FragmentAddStudent : Fragment() {
    lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_add_student, container, false)
        setNewStudentDetailsButtons()
        return root
    }

    private fun setNewStudentDetailsButtons() {
        setSubmitNewStudentButton()
    }

    private fun setSubmitNewStudentButton() {
        var btnSubmitNewStudent = root.findViewById<View>(R.id.btnSubmitNewStudent) as Button
        btnSubmitNewStudent.setOnClickListener {
            // Sustraer datos de los textFields para crear una nueva instancia de data class de Group,
            // y enviar esos datos a Firebase
        }
    }
}