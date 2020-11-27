package mx.itesm.rano.eduCards.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_add_student.*
import mx.itesm.rano.eduCards.R
import mx.itesm.rano.eduCards.models.Student

class FragmentAddStudent(user:String, course: String, group: String) : Fragment() {
    var user = user
    var course = course
    var group = group
    lateinit var root: View
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance()
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
            val studentMail = editTextTutorMailAddress.text.toString()
            if (studentId != "" && studentName != "") {
                writeDataToCloud(studentId, studentName, studentMail)
                Toast.makeText(context, "Student submitted", Toast.LENGTH_LONG).show()
            } else{
                Toast.makeText(context, "Error: The fields are empty", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun writeDataToCloud(studentId: String, studentName: String, studentMail: String) {
        val student = Student(studentId, studentName, studentMail)
        val courseId = course.split("[", "]")[1]
        val groupId = group.split("[", "]")[1]
        val referencia = database.getReference("Instructors/$user/Courses/$courseId/Groups/$groupId/Alumnos/$studentId")
        referencia.setValue(student)
    }
}