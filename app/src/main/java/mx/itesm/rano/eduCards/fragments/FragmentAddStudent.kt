package mx.itesm.rano.eduCards.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_add_student.*
import mx.itesm.rano.eduCards.R
import mx.itesm.rano.eduCards.models.Course
import mx.itesm.rano.eduCards.models.Student

class FragmentAddStudent(course: String, groupId: String) : Fragment() {
    lateinit var root: View
    private lateinit var baseDatos: FirebaseDatabase
    var course = course
    var group = groupId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseDatos = FirebaseDatabase.getInstance()
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
            val studentId = editTextTextStudentID.text.toString()
            val studentName = editTextTextStudentName.text.toString()

            escribirDatos(studentId, studentName)
        }
    }

    private fun escribirDatos(studentId: String, studentName: String) {
        val student = Student(studentId, studentName)
        val courseId = course.split("[", "]")[1]
        val groupId = group.split("[", "]")[1]
        val referencia = baseDatos.getReference("/Courses/$courseId/Groups/$groupId/Alumnos/$studentId")
        referencia.setValue(student)
    }
}