package mx.itesm.rano.eduCards.fragments

import android.R
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.ListFragment
import mx.itesm.rano.eduCards.Interfaces.ListListener
import mx.itesm.rano.eduCards.models.Group

class FragmentGroupList : ListFragment(){
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
        val groups = Array<String>(Group.groups.size){
            Group.groups[it].name
        }
        val adaptador = ArrayAdapter<String>(inflater.context,
            R.layout.simple_list_item_1 , groups)

        listAdapter = adaptador
        return vista
    }
}