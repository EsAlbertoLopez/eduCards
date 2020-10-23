package mx.itesm.rano.eduCards.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import mx.itesm.rano.eduCards.R
import mx.itesm.rano.eduCards.activities.MainActivity

class FragmentCourse : Fragment() {
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
        setCourseActionsButtons()
        //setFragmentCourseList()
    }

    private fun setCourseActionsButtons() {
        var btnAddCourse = root.findViewById<View>(R.id.btnAddCourse) as Button
        btnAddCourse.setOnClickListener {
            var fragment = FragmentAddCourse()
            fragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentContainer, fragment)
                ?.addToBackStack(fragment.toString())
                ?.replace(R.id.fragmentContainer, fragment)
                ?.commit()
        }
    }

   /* override fun itemClicked(index: Int, element: String) {
        // doesn't work :(
        println("Here!")
        var fragment = FragmentGroup()
        fragmentManager?.beginTransaction()
            ?.replace(R.id.fragmentContainer, fragment)
            ?.addToBackStack(fragment.toString())
            ?.replace(R.id.fragmentContainer, fragment)
            ?.commit()
    }*/

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