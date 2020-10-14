package mx.itesm.rano.eduCards.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_add_course.*
import mx.itesm.rano.eduCards.R
import mx.itesm.rano.eduCards.models.Course

class FragmentAddCourse : Fragment() {
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
            writeDataToCloud(courseId, courseName)
        }
    }

    private fun writeDataToCloud(courseId: String, courseName: String) {
        val course = Course(courseId, courseName)
        val reference = database.getReference("/Courses/$courseId")
        reference.setValue(course)
    }
}