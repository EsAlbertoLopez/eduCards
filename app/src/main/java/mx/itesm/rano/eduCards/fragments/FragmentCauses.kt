package mx.itesm.rano.eduCards.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import mx.itesm.rano.eduCards.R

class FragmentCauses(selectedCourse: String, selectedGroup: String, selectedStudent: String) : Fragment() {
    var user = FirebaseAuth.getInstance().currentUser?.email?.replace(".", "__dot__").toString()
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
        setLayoutVariables()
        return root
    }

    private fun setLayoutVariables() {
        var studentName = student.split("[", "]")[2]
        var tvSubtitle = root.findViewById<View>(R.id.tvSubtitle) as TextView
        tvSubtitle.text = "Choose a Card from${studentName}"
    }

    fun setWhenNoItemsInList() {
        var tvSubtitle = root.findViewById<View>(R.id.tvSubtitle) as TextView
        tvSubtitle.text = "Waiting for you to add Causes..."
    }
}