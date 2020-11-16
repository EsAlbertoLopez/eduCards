package mx.itesm.rano.eduCards.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_add_course.*
import mx.itesm.rano.eduCards.R
import mx.itesm.rano.eduCards.models.Course

class FragmentAddCourse(user: String) : Fragment() {
    lateinit var root: View
    var userLogged = user
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance()
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
            val courseId = editTextTextCourseID.text.toString()
            val courseName = editTextTextCourseName.text.toString()
            if (courseId != "" && courseName != "") {
                writeDataToCloud(courseId, courseName)
                var fragment = FragmentCourse(userLogged)
                fragmentManager?.beginTransaction()
                    ?.replace(R.id.fragmentContainer, fragment)
                    ?.addToBackStack(fragment.toString())
                    ?.replace(R.id.fragmentContainer, fragment)
                    ?.commit()
            } else{
                Toast.makeText(context, "Error: The fields are empty", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun writeDataToCloud(courseId: String, courseName: String) {
        val course = Course(courseId, courseName)
        val reference = database.getReference("/Instructors/$userLogged/Courses/$courseId")
        reference.setValue(course)
    }
}