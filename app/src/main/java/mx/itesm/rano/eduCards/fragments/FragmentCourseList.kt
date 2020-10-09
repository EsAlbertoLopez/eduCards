package mx.itesm.rano.eduCards.fragments

import android.R
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.ListFragment
import mx.itesm.rano.eduCards.Interfaces.ListListener
import mx.itesm.rano.eduCards.models.Course

class FragmentCourseList : ListFragment() {
    var listener: ListListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ListListener){
            listener = context
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vista = super.onCreateView(inflater, container, savedInstanceState)
        val courses = Array<String>(4){
            "Hola"
        }
        val adaptador = ArrayAdapter<String>(inflater.context,
            R.layout.simple_list_item_1 , courses)

        listAdapter = adaptador
        return vista
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        listener?.itemClicked(position)
    }

}