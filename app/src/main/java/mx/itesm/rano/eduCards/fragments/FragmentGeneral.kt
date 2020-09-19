package mx.itesm.rano.eduCards.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import mx.itesm.rano.eduCards.Interfaces.ListListener
import mx.itesm.rano.eduCards.R
import mx.itesm.rano.eduCards.models.Student

class FragmentGeneral : ListFragment() {

    var listener: ListListener? = null

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        listener?.itemClicked(position)
    }

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
        val students = Array<String>(Student.students.size){
            Student.students[it].name
        }
        val adaptador = ArrayAdapter<String>(inflater.context,
            android.R.layout.simple_list_item_1 , students)

        listAdapter = adaptador
        return vista
    }
}