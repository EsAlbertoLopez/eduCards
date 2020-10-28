package mx.itesm.rano.eduCards.fragments



import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.ListFragment
import mx.itesm.rano.eduCards.interfaces.ListListener


class FragmentCausesList : ListFragment(){
    var listener: ListListener? = null
    lateinit var arrGroups : MutableList<String>
    var selected = "NADA"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ListListener){
            listener = context
        }
    }

    fun getValueParent(){
        val myParent = parentFragment as FragmentGroup
        selected = myParent.element
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arrGroups = mutableListOf()
        getValueParent()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vista = super.onCreateView(inflater, container, savedInstanceState)
        return vista
    }

    override fun onStart() {
        super.onStart()
        println("Starting")
        println(selected)
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        var element = arrGroups[position]
        listener?.itemClicked(position, element)
    }
}