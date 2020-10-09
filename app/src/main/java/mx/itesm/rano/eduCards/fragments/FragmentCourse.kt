package mx.itesm.rano.eduCards.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.ListFragment
import kotlinx.android.synthetic.main.activity_general.*
import mx.itesm.rano.eduCards.Interfaces.ListListener
import mx.itesm.rano.eduCards.R

class FragmentCourse : Fragment(), ListListener {
    lateinit var root: View

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
        //setFragmentCourseList()
    }

    override fun itemClicked(index: Int) {
        // doesnt work :(
        println("Here!")
        var fragment = FragmentGroup()
        fragmentManager?.beginTransaction()
            ?.replace(R.id.fragmentContainer, fragment)
            ?.addToBackStack(fragment.toString())
            ?.replace(R.id.fragmentContainer, fragment)
            ?.commit()
    }

    //private fun setFragmentCourseList() {
    //    val list = root.findViewById<View>(R.id.fragmentFragmentCourseList) as ListFragment
    //    list.listView.setOnItemClickListener { adapterView, view, i, l ->
    //        val fragment = FragmentGroup()
    //        fragmentManager?.beginTransaction()
    //            ?.replace(R.id.fragmentContainer, fragment)
    //            ?.addToBackStack(fragment.toString())
    //            ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
    //            ?.commit()
    //    }
    //}
}