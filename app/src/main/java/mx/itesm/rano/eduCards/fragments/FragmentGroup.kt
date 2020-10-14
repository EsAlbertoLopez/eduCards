package mx.itesm.rano.eduCards.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import mx.itesm.rano.eduCards.R

class FragmentGroup(element: String) : Fragment() {
    var element = element
    lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_group, container, false)
        setButtons()
        return root
    }

    private fun setButtons() {
        setGroupActionsButtons()
    }

    private fun setGroupActionsButtons() {
        var btnAddGroup = root.findViewById<View>(R.id.btnAddGroup)
        btnAddGroup.setOnClickListener {
            var fragment = FragmentAddGroup(element)
            fragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentContainer, fragment)
                ?.addToBackStack(fragment.toString())
                ?.replace(R.id.fragmentContainer, fragment)
                ?.commit()
        }
    }
}