package mx.itesm.rano.eduCards.fragments

import android.R
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
import mx.itesm.rano.eduCards.Interfaces.ListListener
import mx.itesm.rano.eduCards.models.Group
import mx.itesm.rano.eduCards.models.Student

class FragmentStudentList : ListFragment(){
    var listener: ListListener? = null
    lateinit var arrStudents : MutableList<String>

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        var element = arrStudents[position]
        super.onListItemClick(l, v, position, id)
        listener?.itemClicked(position, element)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ListListener){
            listener = context
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arrStudents = mutableListOf()
    }

    override fun onStart() {
        super.onStart()
        descargarDatosNube()
    }

    private fun descargarDatosNube() {
        val baseDatos = FirebaseDatabase.getInstance()
        val referencia = baseDatos.getReference("/Courses/TI80/Groups/21/Alumnos")

        referencia.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrStudents.clear()
                for (registro in snapshot.children){
                    val student = registro.getValue(Student::class.java)
                    if (student != null){
                        val dataCourse = "${student.name} "
                        arrStudents.add(dataCourse)
                    }

                    val adapter = ArrayAdapter<String>(context!!,
                        android.R.layout.simple_list_item_1,
                        arrStudents)

                    listAdapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vista = super.onCreateView(inflater, container, savedInstanceState)
        return vista
    }
}