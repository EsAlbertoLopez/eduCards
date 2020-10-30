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
import mx.itesm.rano.eduCards.models.Course

class FragmentCourseList : ListFragment() {
    var listener: ListListener? = null
    lateinit var arrCourses : MutableList<String>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ListListener){
            listener = context
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arrCourses = mutableListOf()
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
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("/Courses")
        reference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                arrCourses.clear()
                for (record in snapshot.children){
                    val course = record.getValue(Course::class.java)
                    if (course != null){
                        val dataCourse = "[${course.key}] ${course.name} "
                        arrCourses.add(dataCourse)
                    }
                    if (context != null){
                        val adapter = ArrayAdapter<String>(context!!,
                            android.R.layout.simple_list_item_1,
                            arrCourses)
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
        var element = arrCourses[position]
        listener?.itemClicked(position, element)
    }
}