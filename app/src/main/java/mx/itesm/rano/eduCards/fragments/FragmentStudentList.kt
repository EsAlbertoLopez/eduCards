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
import mx.itesm.rano.eduCards.models.Student

class FragmentStudentList : ListFragment(){
    var listener: ListListener? = null
    lateinit var parent: FragmentStudent
    lateinit var arrStudents: MutableList<String>
    var course = "None"
    var group = "None"
    var user = "None"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ListListener){
            listener = context
            parent = parentFragment as FragmentStudent
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arrStudents = mutableListOf()
        getValueParent()
    }

    fun getValueParent(){
        val myParent = parentFragment as FragmentStudent
        course = myParent.course
        group = myParent.group
        user = myParent.user
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
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("Instructors/$user/Courses/$courseId/Groups/$groupId/Alumnos")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrStudents.clear()
                if (snapshot.exists()) {
                    for (registro in snapshot.children){
                        val student = registro.getValue(Student::class.java)
                        if (student != null) {
                            val dataCourse = "[${student.key}] ${student.name} "
                            arrStudents.add(dataCourse)
                        }
                        if (context != null) {
                            val adapter = ArrayAdapter<String>(
                                context!!,
                                android.R.layout.simple_list_item_1,
                                arrStudents
                            )
                            listAdapter = adapter
                        }
                    }
                } else {
                    parent.setWhenNoItemsInList()
                    arrStudents.add("There are no students")
                    if (context != null) {
                        val adapter = ArrayAdapter<String>(
                            context!!,
                            android.R.layout.simple_list_item_1,
                            arrStudents
                        )
                        println(arrStudents.toString())
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
        if (arrStudents[position] == "There are no students"){
            Toast.makeText(context, "Please add students to continue...", Toast.LENGTH_LONG).show()
        } else {
            super.onListItemClick(l, v, position, id)
            var element = arrStudents[position]
            listener?.itemClicked(position, element)
        }
    }
}