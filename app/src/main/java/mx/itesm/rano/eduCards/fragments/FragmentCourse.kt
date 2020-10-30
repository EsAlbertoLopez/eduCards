package mx.itesm.rano.eduCards.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import mx.itesm.rano.eduCards.R
import mx.itesm.rano.eduCards.activities.MainActivity

class FragmentCourse (user: String): Fragment() {
    lateinit var root: View
    var loginUser = user

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
                ?.addToBackStack(fragment.toString())
                ?.replace(R.id.fragmentContainer, fragment)
                ?.commit()
        }
    }
}