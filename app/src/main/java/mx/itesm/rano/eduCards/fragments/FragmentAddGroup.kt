package mx.itesm.rano.eduCards.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_add_group.*
import mx.itesm.rano.eduCards.R
import mx.itesm.rano.eduCards.models.Group

class FragmentAddGroup(user:String, element: String) : Fragment() {
    val element = element
    val user = user
    lateinit var root: View
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance()
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
            if (groupId != "" && groupName != "") {
                writeDataToCloud(groupId, groupName)
            } else{
                Toast.makeText(context, "Error: The fields are empty", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun writeDataToCloud(groupId: String, groupName: String) {
        val courseId = element.split("[", "]")[1]
        val group = Group(groupId, groupName)
        val reference = database.getReference("Instructors/$user/Courses/$courseId/Groups/$groupId")
        reference.setValue(group)
    }
}