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
import mx.itesm.rano.eduCards.models.Card
import mx.itesm.rano.eduCards.models.Student

class FragmentCausesList : ListFragment(){
    var listener: ListListener? = null
    lateinit var parent: FragmentCauses
    lateinit var arrCauses: MutableList<String>
    lateinit var arrKeys: MutableList<String>
    var user = "None"
    var course = "None"
    var group = "None"
    var student = "None"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ListListener){
            listener = context
            parent = parentFragment as FragmentCauses
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arrCauses = mutableListOf()
        arrKeys = mutableListOf()
        getValueParent()
    }

    fun getValueParent(){
        val myParent = parentFragment as FragmentCauses
        user = myParent.user
        course = myParent.course
        group = myParent.group
        student = myParent.student
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
        readDataFromCloud()
    }

    private fun readDataFromCloud() {
        val courseId = course.split("[", "]")[1]
        val groupId = group.split("[", "]")[1]
        val studentId = student.split("[", "]")[1]
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("Instructors/$user/Courses/$courseId/Groups/$groupId/Alumnos/$studentId/Events/")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrCauses.clear()
                if (snapshot.exists()) {
                    for (registro in snapshot.children){
                        val cause = registro.getValue(Card::class.java)
                        if (cause != null){
                            val dataCourse = "[${cause.description}]"
                            arrCauses.add(dataCourse)
                            arrKeys.add(registro.key.toString())
                        }
                        if (context != null) {
                            val adapter = ArrayAdapter<String>(
                                context!!,
                                android.R.layout.simple_list_item_1,
                                arrCauses
                            )
                            listAdapter = adapter
                        }
                    }
                } else {
                    parent.setWhenNoItemsInList()
                    arrCauses.add("There are no cards")
                    if (context != null) {
                        val adapter = ArrayAdapter<String>(
                            context!!,
                            android.R.layout.simple_list_item_1,
                            arrCauses
                        )
                        println(arrCauses.toString())
                        listAdapter = adapter
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        if (arrCauses[position] == "There are no cards"){
            Toast.makeText(context, "Please add cards to continue...", Toast.LENGTH_LONG).show()
        }else {
            super.onListItemClick(l, v, position, id)
            var element = arrCauses[position]
            listener?.itemClicked(position, element)
        }
    }
}