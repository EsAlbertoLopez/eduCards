package mx.itesm.rano.eduCards.fragments

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_live.*
import mx.itesm.rano.eduCards.R
import mx.itesm.rano.eduCards.models.Course
import mx.itesm.rano.eduCards.models.Student
import java.text.DateFormat
import java.util.*

class FragmentLive : Fragment(){
    lateinit var root: View
    lateinit var inflater: LayoutInflater
    lateinit var chronometer: Chronometer
    lateinit var calendar: Calendar
    lateinit var currentDate: String
    var pauseOffset: Long = 0
    var chronometerState: Boolean = false

    lateinit var arrCourses : MutableList<String>
    lateinit var arrStudents: MutableList<String>

    private lateinit var baseDatos: FirebaseDatabase

    override fun onStart() {
        super.onStart()
        descargarDatosNubeCourses()
        descargarDatosNubeStudents()
    }

    private fun descargarDatosNubeStudents() {
        val referencia = baseDatos.getReference("/Courses/TI80/Groups/21/Alumnos")
        referencia.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrStudents.clear()
                for (registro in snapshot.children){
                    val student = registro.getValue(Student::class.java)
                    if (student != null){
                        val dataStudent = "[${student.studentId}] ${student.name} "
                        arrStudents.add(dataStudent)
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun descargarDatosNubeCourses() {
        val referencia = baseDatos.getReference("/Courses")
        referencia.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrCourses.clear()
                for (registro in snapshot.children){
                    val course = registro.getValue(Course::class.java)
                    if (course != null){
                        val dataCourse = "[${course.courseKey}] ${course.name} "
                        arrCourses.add(dataCourse)
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
            }

        })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseDatos = FirebaseDatabase.getInstance()
        arrCourses = mutableListOf()
        arrStudents = mutableListOf()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.inflater = inflater
        root = inflater.inflate(R.layout.fragment_live, container, false)
        setSpinners()
        setButtons()
        setCalendar()
        return root
    }

    private fun setSpinners() {
        print(arrCourses)
        print(arrStudents)
        setSpinner(inflater, root, R.id.cardTypeSpinner, resources.getStringArray(R.array.Reasons))
        setSpinner(inflater, root, R.id.courseSpinner, arrCourses.toTypedArray())
        setSpinner(inflater, root, R.id.studentSpinner, arrStudents.toTypedArray())
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
                println(reasons[p2])
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

    private fun setCalendar() {
        calendar = Calendar.getInstance()
        currentDate = DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.time)
        val tvDay = root.findViewById<View>(R.id.tvDay) as TextView
        tvDay.text = currentDate
    }
}