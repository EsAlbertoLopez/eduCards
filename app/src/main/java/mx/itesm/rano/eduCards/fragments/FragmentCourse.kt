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
import mx.itesm.rano.eduCards.activities.MainActivity

class FragmentCourse (): Fragment() {
    lateinit var root: View
    var loginUser = FirebaseAuth.getInstance().currentUser?.email?.replace(".", "__dot__").toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_course, container, false)
        setButtons()
        return root
    }

    private fun setButtons() {
        setCourseActionsButtons()
    }

    private fun setCourseActionsButtons() {
        var btnAddCourse = root.findViewById<View>(R.id.btnAddCourse) as Button
        btnAddCourse.setOnClickListener {
            var fragment = FragmentAddCourse(loginUser)
            fragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentContainer, fragment)
                ?.commit()
        }
    }

    fun setWhenNoItemsInList() {
        var tvSubtitle = root.findViewById<View>(R.id.tvSubtitle) as TextView
        tvSubtitle.text = "Waiting for you to add Courses..."
    }
}