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

class FragmentGroup(element: String) : Fragment() {
    var element = element
    val user = FirebaseAuth.getInstance().currentUser?.email?.replace(".", "__dot__").toString()
    lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_group, container, false)
        setLayoutVariables()
        setButtons()
        return root
    }

    private fun setLayoutVariables() {
        var courseName = element.split("[", "]")[2]
        var tvSubtitle = root.findViewById<View>(R.id.tvSubtitle) as TextView
        tvSubtitle.text = "Choose a Group for${courseName}"
    }

    private fun setButtons() {
        setGroupActionsButtons()
    }

    private fun setGroupActionsButtons() {
        var btnAddGroup = root.findViewById<View>(R.id.btnAddGroup)
        btnAddGroup.setOnClickListener {
            var fragment = FragmentAddGroup(user, element)
            fragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentContainer, fragment)
                ?.addToBackStack(fragment.toString())
                ?.replace(R.id.fragmentContainer, fragment)
                ?.commit()
        }
    }

    fun setWhenNoItemsInList() {
        var tvSubtitle = root.findViewById<View>(R.id.tvSubtitle) as TextView
        tvSubtitle.text = "Waiting for you to add Groups..."
    }
}