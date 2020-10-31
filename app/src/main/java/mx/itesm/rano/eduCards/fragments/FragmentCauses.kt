package mx.itesm.rano.eduCards.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import mx.itesm.rano.eduCards.R

class FragmentCauses(user: String, selectedCourse: String, selectedGroup: String, selectedStudent: String) : Fragment() {

    var user = user
    var course = selectedCourse
    var group = selectedGroup
    var student = selectedStudent
    lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_causes, container, false)
        return root
    }
}