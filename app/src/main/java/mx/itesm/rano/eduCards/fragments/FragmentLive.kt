package mx.itesm.rano.eduCards.fragments

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_live.*
import mx.itesm.rano.eduCards.R
import mx.itesm.rano.eduCards.models.Card
import mx.itesm.rano.eduCards.models.Course
import mx.itesm.rano.eduCards.models.Group
import mx.itesm.rano.eduCards.models.Student
import java.lang.Exception
import java.text.DateFormat
import java.util.*

class FragmentLive : Fragment(){
    var pauseOffset: Long = 0
    var chronometerState: Boolean = false
    lateinit var root: View
    lateinit var inflater: LayoutInflater
    lateinit var chronometer: Chronometer
    lateinit var calendar: Calendar
    lateinit var currentDate: String
    lateinit var arrCourses: MutableList<String>
    lateinit var arrStudents: MutableList<String>
    lateinit var arrGroup: MutableList<String>
    private lateinit var database: FirebaseDatabase
    lateinit var selectedCourse: String
    lateinit var selectedGroup: String
    lateinit var selectedStudent: String
    lateinit var selectedCause: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance()
        arrCourses = mutableListOf()
        arrStudents = mutableListOf()
        arrGroup = mutableListOf()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.inflater = inflater
        root = inflater.inflate(R.layout.fragment_live, container, false)
        setSpinners()
        setButtons()
        setCalendar()
        return root
    }

    override fun onStart() {
        super.onStart()
        readCourseDataFromCloud()

    }

    private fun readGroupDataFromCloud() {

        val courseId = selectedCourse.split("[", "]")[1]
        val reference = database.getReference("/Courses/$courseId/Groups/")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrGroup.clear()
                for (register in snapshot.children){
                    val group = register.getValue(Group::class.java)
                    if (group != null){
                        val dataGroup = "[${group.key}] ${group.name} "
                        arrGroup.add(dataGroup)
                    }
                }
                setSpinner(inflater, root, R.id.groupSpinner, arrGroup.toTypedArray())
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun readStudentDataFromCloud() {
        val courseId = selectedCourse.split("[", "]")[1]
        val groupId = selectedGroup.split("[", "]")[1]
        val reference = database.getReference("/Courses/$courseId/Groups/$groupId/Alumnos")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrStudents.clear()
                for (register in snapshot.children){
                    val student = register.getValue(Student::class.java)
                    if (student != null){
                        val dataStudent = "[${student.key}] ${student.name} "
                        arrStudents.add(dataStudent)
                    }
                }
                setSpinner(inflater, root, R.id.studentSpinner, arrStudents.toTypedArray())
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun readCourseDataFromCloud() {
        val reference = database.getReference("/Courses")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrCourses.clear()
                for (register in snapshot.children){
                    val course = register.getValue(Course::class.java)
                    if (course != null){
                        val dataCourse = "[${course.key}] ${course.name} "
                        arrCourses.add(dataCourse)
                    }
                }
                setSpinner(inflater, root, R.id.courseSpinner, arrCourses.toTypedArray())


            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun setSpinners() {
        setSpinner(inflater, root, R.id.cardTypeSpinner, resources.getStringArray(R.array.Reasons))


    }

    private fun setSpinner(
        inflater: LayoutInflater, v: View?, optSpinner: Int, stringArray: Array<String>) {
        val spinner = v?.findViewById<Spinner>(optSpinner)
        val reasons = stringArray
        val adapter = ArrayAdapter<String>(inflater.context,
            android.R.layout.simple_spinner_item, reasons)
        spinner?.adapter = adapter
        spinner?.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                println("check to change id ${optSpinner == 2131361900}")
                println("Spinner as int $optSpinner")

                when (optSpinner){
                    2131361918 -> {
                        selectedCourse = reasons[p2]
                        try {
                            readGroupDataFromCloud()
                        }catch (e:Exception){
                            Toast.makeText(context, "Error: $e", Toast.LENGTH_LONG).show()
                        }
                    }
                    2131361999 -> {
                        selectedGroup = reasons[p2]
                        try {
                            readStudentDataFromCloud()
                        }catch (e:Exception){
                            Toast.makeText(context, "Error: $e", Toast.LENGTH_LONG).show()
                        }
                    }
                    2131362153 -> {
                        selectedStudent = reasons[p2]
                    }
                    2131361900 ->{
                        selectedCause = reasons[p2]
                    }
                }

                //Course id  2131361918
                //Group id   2131361995
                //Student id 2131362153

                //currentCourse = reasons[p2]
                //println(currentCourse)
                //println(reasons[p2])
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

    }

    private fun setButtons() {
        setCardActionsButtons()
    }

    private fun setCardActionsButtons() {
        val btnSave = root.findViewById<View>(R.id.btnSave) as Button
        btnSave.setOnClickListener{
            val courseId = selectedCourse.split("[", "]")[1]
            val groupId = selectedGroup.split("[", "]")[1]
            val studentId = selectedStudent.split("[", "]")[1]
            val cause = selectedCause
            val explanation = editTextTextMultiLineDescription.text.toString()

            writeDataToCloud(courseId, groupId, studentId, cause, explanation)

        }
        chronometer = root.findViewById(R.id.chChronometer) as Chronometer
        val btnStart = root.findViewById<View>(R.id.btnStart) as Button
        btnStart.setOnClickListener {
            chronometer.base = SystemClock.elapsedRealtime() - pauseOffset
            chronometer.start()
            btnStart.isEnabled = false
            btnStopResume.isEnabled = true
            btnRestart.isEnabled = true
            chronometerState = true
        }
        btnStart.isEnabled = true
        val btnStopResume = root.findViewById<View>(R.id.btnStopResume) as Button
        btnStopResume.setOnClickListener {
            if (chronometerState == true) {
                chronometer.stop()
                pauseOffset = SystemClock.elapsedRealtime() - chronometer.base
                btnStopResume.setText("Resume")
                chronometerState = false
            } else {
                chronometer.base = SystemClock.elapsedRealtime() - pauseOffset
                chronometer.start()
                btnStopResume.setText("Stop")
                chronometerState = true
            }
        }
        btnStopResume.isEnabled = false
        val btnRestart = root.findViewById<View>(R.id.btnRestart) as Button
        btnRestart.setOnClickListener {
            chronometer.base = SystemClock.elapsedRealtime()
            pauseOffset = 0
            btnRestart.isEnabled = false
            btnStopResume.isEnabled = false
            btnStopResume.setText("Stop")
            btnStart.isEnabled = true
        }
        btnRestart.isEnabled = false
    }

    private fun writeDataToCloud(courseId: String, groupId: String, studentId: String, cause: String, explanation: String) {
        val card = Card(cause, explanation)
        val reference = database.getReference("/Courses/$courseId/Groups/$groupId/Alumnos/$studentId/Events/")
        val ref = reference.push()

        ref.setValue(card)

    }

    private fun setCalendar() {
        calendar = Calendar.getInstance()
        currentDate = DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.time)
        val tvDay = root.findViewById<View>(R.id.tvDay) as TextView
        tvDay.text = currentDate
    }
}