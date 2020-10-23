package mx.itesm.rano.eduCards.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.ListFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import mx.itesm.rano.eduCards.interfaces.ListListener
import mx.itesm.rano.eduCards.models.Group

class FragmentGroupList : ListFragment(){
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
        readDataFromCloud()
    }

    private fun readDataFromCloud() {
        val database = FirebaseDatabase.getInstance()
        val courseId = selected.split("[", "]")[1]
        val reference = database.getReference("/Courses/$courseId/Groups")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrGroups.clear()
                for (registro in snapshot.children) {
                    val group = registro.getValue(Group::class.java)
                    if (group != null) {
                        val dataCourse = "[${group.key}] ${group.name} "
                        arrGroups.add(dataCourse)
                    }
                    if (context != null) {
                        val adapter = ArrayAdapter<String>(
                            context!!,
                            android.R.layout.simple_list_item_1,
                            arrGroups
                        )

                        listAdapter = adapter
                    }else{
                        println("Holi")
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        var element = arrGroups[position]
        listener?.itemClicked(position, element)
    }
}