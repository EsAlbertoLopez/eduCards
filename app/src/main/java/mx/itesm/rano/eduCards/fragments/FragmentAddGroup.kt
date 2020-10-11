package mx.itesm.rano.eduCards.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_add_course.*
import kotlinx.android.synthetic.main.fragment_add_group.*
import mx.itesm.rano.eduCards.R
import mx.itesm.rano.eduCards.models.Course
import mx.itesm.rano.eduCards.models.Group

class FragmentAddGroup(element: String) : Fragment() {

    val element = element
    lateinit var root: View
    private lateinit var baseDatos: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseDatos = FirebaseDatabase.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_add_group, container, false)
        setNewGroupDetailsButtons()
        return root
    }

    private fun setNewGroupDetailsButtons() {
        setSubmitNewGroupButton()
    }

    private fun setSubmitNewGroupButton() {
        var btnSubmitNewGroup = root.findViewById<View>(R.id.btnSubmitNewgroup) as Button
        btnSubmitNewGroup.setOnClickListener {
            val groupId = editTextTextGroupID.text.toString()
            val groupName = editTextTextGroupName.text.toString()

            escribirDatos(groupId, groupName)
        }
    }

    private fun escribirDatos(gropuId: String, groupName: String) {
        val courseId = element.split("[", "]")[1]
        val group = Group(gropuId, groupName)
        val referencia = baseDatos.getReference("/Courses/$courseId/Groups/$gropuId")
        referencia.setValue(group)
    }
}