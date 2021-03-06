package mx.itesm.rano.eduCards.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_live.*
import mx.itesm.rano.eduCards.R
import mx.itesm.rano.eduCards.activities.MainActivity
import mx.itesm.rano.eduCards.models.Card
import mx.itesm.rano.eduCards.models.Course
import mx.itesm.rano.eduCards.models.Group
import mx.itesm.rano.eduCards.models.Student
import java.lang.Exception
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class FragmentLive : Fragment(){
    var user = FirebaseAuth.getInstance().currentUser?.email?.replace(".", "__dot__").toString()
    var pauseOffset: Long = 0
    var chronometerState: Boolean = false
    lateinit var root: View
    lateinit var mainActivity: MainActivity
    lateinit var inflater: LayoutInflater
    lateinit var chronometer: Chronometer
    lateinit var calendar: Calendar
    lateinit var currentDate: String
    lateinit var arrCourses: MutableList<String>
    lateinit var arrStudents: MutableList<String>
    var arrMail: MutableList<String> = arrayListOf()
    lateinit var arrGroup: MutableList<String>
    lateinit var recordedTime: String
    lateinit var selectedCourse: String
    lateinit var selectedGroup: String
    lateinit var selectedStudent: String
    lateinit var mailTutor: String
    lateinit var selectedCause: String
    private lateinit var database: FirebaseDatabase

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
        mainActivity = context as MainActivity
        setSpinners()
        setButtons()
        setCalendar()
        setReportDetails()
        return root
    }

    override fun onStart() {
        super.onStart()
        readCourseDataFromCloud()
    }

    private fun readGroupDataFromCloud() {
        val courseId = selectedCourse.split("[", "]")[1]
        val reference = database.getReference("/Instructors/$user/Courses/$courseId/Groups/")
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
                setSpinner(
                    inflater,
                    root,
                    R.id.groupSpinner,
                    arrGroup.toTypedArray(),
                    arrMail.toTypedArray()
                )
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun readStudentDataFromCloud() {
        val courseId = selectedCourse.split("[", "]")[1]
        val groupId = selectedGroup.split("[", "]")[1]
        val reference = database.getReference("/Instructors/$user/Courses/$courseId/Groups/$groupId/Alumnos")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrStudents.clear()
                arrMail.clear()
                for (register in snapshot.children){
                    val student = register.getValue(Student::class.java)
                    if (student != null){
                        val dataStudent = "[${student.key}] ${student.name}"
                        arrStudents.add(dataStudent)
                        arrMail.add(student.mailTutor)
                    }
                }
                setSpinner(inflater, root, R.id.studentSpinner, arrStudents.toTypedArray(), arrMail.toTypedArray())
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun readCourseDataFromCloud() {
        val reference = database.getReference("/Instructors/$user/Courses")
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
                setSpinner(
                    inflater,
                    root,
                    R.id.courseSpinner,
                    arrCourses.toTypedArray(),
                    arrMail.toTypedArray()
                )
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setSpinners() {
        setSpinner(
            inflater,
            root,
            R.id.cardTypeSpinner,
            resources.getStringArray(R.array.Reasons),
            arrMail.toTypedArray()
        )
    }

    private fun setSpinner(
        inflater: LayoutInflater,
        v: View?,
        optSpinner: Int,
        stringArray: Array<String>,
        mailArray: Array<String>
    ) {
        val spinner = v?.findViewById<Spinner>(optSpinner)
        val reasons = stringArray
        val adapter = ArrayAdapter<String>(inflater.context,
            android.R.layout.simple_spinner_item, reasons)
        spinner?.adapter = adapter
        spinner?.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, p2: Int, p3: Long) {
                println("check to change id ${optSpinner == 2131361900}")
                println("Spinner as int $optSpinner")
                when (spinner){
                    v?.findViewById<Spinner>(R.id.courseSpinner) -> {
                        selectedCourse = reasons[p2]
                        try {
                            readGroupDataFromCloud()
                        }catch (e:Exception){
                            Toast.makeText(context, "Error: $e", Toast.LENGTH_LONG).show()
                        }
                    }
                    v?.findViewById<Spinner>(R.id.groupSpinner) -> {
                        selectedGroup = reasons[p2]
                        try {
                            readStudentDataFromCloud()
                        }catch (e:Exception){
                            Toast.makeText(context, "Error: $e", Toast.LENGTH_LONG).show()
                        }
                    }
                    v?.findViewById<Spinner>(R.id.studentSpinner) -> {
                        selectedStudent = reasons[p2]
                        mailTutor = mailArray[p2]
                    }
                    v?.findViewById<Spinner>(R.id.cardTypeSpinner) ->{
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
            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    private fun setButtons() {
        setCardActionsButtons()
        setSessionActionsButtons()
    }

    private fun setCardActionsButtons() {
        val btnSave = root.findViewById<View>(R.id.btnSave) as Button
        btnSave.setOnClickListener{
            if (arrStudents.isEmpty() || editTextTextMultiLineDescription.text.isEmpty()) {
                Toast.makeText(mainActivity,
                    "Nothing to save",
                    Toast.LENGTH_SHORT).show()
            } else {
                var alertDialogBuilder : AlertDialog.Builder = AlertDialog.Builder(mainActivity)
                alertDialogBuilder.setTitle("Save Card?")
                    .setMessage("")
                    .setPositiveButton("Yes", DialogInterface.OnClickListener{
                            dialog, id ->
                        Toast.makeText(mainActivity,
                            "Saving card",
                            Toast.LENGTH_SHORT).show()
                        val courseId = selectedCourse.split("[", "]")[1]
                        val groupId = selectedGroup.split("[", "]")[1]
                        val studentId = selectedStudent.split("[", "]")[1]
                        val cause = selectedCause
                        val description = editTextTextMultiLineDescription.text.toString()
                        writeDataToCloud(courseId, groupId, studentId, cause, description)
                        sendMail(cause, description, selectedCourse.split("[", "]")[2], selectedCourse.split("[", "]")[2], recordedTime)
                        editTextTextMultiLineDescription.text.clear()
                    })
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener{
                            dialog, id -> dialog.cancel()
                    })
                alertDialogBuilder.create().show()
            }
        }
        val btnDiscard = root.findViewById<View>(R.id.btnDsicard) as Button
        btnDiscard.setOnClickListener {
            if (arrStudents.isEmpty() || editTextTextMultiLineDescription.text.isEmpty()) {
                Toast.makeText(mainActivity,
                    "Nothing to discard",
                    Toast.LENGTH_SHORT).show()
            } else {
                var alertDialogBuilder : AlertDialog.Builder = AlertDialog.Builder(mainActivity)
                alertDialogBuilder.setTitle("Discard Card?")
                    .setMessage("Doing so clears this card's description")
                    .setPositiveButton("Yes", DialogInterface.OnClickListener{
                        dialog, id ->
                        Toast.makeText(mainActivity,
                            "Proceeding to discard",
                            Toast.LENGTH_SHORT).show()
                        var editTextDescription = root.findViewById<View>(R.id.editTextTextMultiLineDescription) as EditText
                        editTextDescription.text.clear()
                    })
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener{
                            dialog, id -> dialog.cancel()
                    })
                alertDialogBuilder.create().show()
            }
        }
        btnSave.isEnabled = false
        btnDiscard.isEnabled = false
    }

    private fun sendMail(cause: String, description: String, course: String, date: String, time: String) {
        val subject = cause
        val miIntent = Intent(Intent.ACTION_SEND)
        miIntent.data = Uri.parse("mailto:")
        miIntent.type = "text/plain"

        miIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(mailTutor))
        miIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        miIntent.putExtra(Intent.EXTRA_TEXT,
            "Hola, tutor de ${selectedStudent.split("[", "]")[2]}." +
                    "\nSe ha registrado una tarjeta de ${cause} por lo siguiente:\n\n" +
                    "${description}\n\n" +
                    "Esta tarjeta se registró para ${course} el ${tvDay.text} a las ${time}.")

        try {
            startActivity(Intent.createChooser(miIntent, "Choose Email Client..."))
        }catch (e: Exception){
            Toast.makeText(mainActivity, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun setSessionActionsButtons() {
        chronometer = root.findViewById(R.id.chChronometer) as Chronometer
        val btnStart = root.findViewById<View>(R.id.btnStart) as Button
        btnStart.setOnClickListener {
            chronometer.base = SystemClock.elapsedRealtime() - pauseOffset
            chronometer.start()
            btnStart.isEnabled = false
            btnStopResume.isEnabled = true
            btnRestart.isEnabled = false
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
                btnRestart.isEnabled = true

            } else {
                chronometer.base = SystemClock.elapsedRealtime() - pauseOffset
                chronometer.start()
                btnStopResume.setText("Stop")
                chronometerState = true
                btnRestart.isEnabled = false


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
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        val dateTime = currentDate.split(" ")
        recordedTime = dateTime[1]
        val card = Card(cause, explanation, user, dateTime[0], dateTime[1])
        val cardReference =
            database.getReference("/Instructors/$user/Courses/$courseId/Groups/$groupId/Alumnos/$studentId/Events/")
        cardReference.push().setValue(card)
        //var statReference = database.getReference("/Instructors/$user/Statistics/${card.type}")
        //statReference.setValue(0)
        //statReference.addValueEventListener(object : ValueEventListener {
        //    override fun onDataChange(snapshot: DataSnapshot) {
        //        var count = snapshot.getValue()
        //        print("Counter\n")
        //        print(count?.javaClass.toString() + "\n")
        //        print(count.toString() + "\n")
        //    }
        //
        //    override fun onCancelled(error: DatabaseError) {
        //        Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
        //    }
        //})
        //var countLong = count as Long
        //countLong = countLong + 1
        //statReference.setValue(countLong)
    }

    private fun setCalendar() {
        calendar = Calendar.getInstance()
        currentDate = DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.time)
        val tvDay = root.findViewById<View>(R.id.tvDay) as TextView
        tvDay.text = currentDate
    }

    private fun setReportDetails() {
        var editTextDescription = root.findViewById<View>(R.id.editTextTextMultiLineDescription) as EditText
        editTextDescription.addTextChangedListener (object : TextWatcher {
            var tvEventDescription = root.findViewById<View>(R.id.tvEventDescription) as TextView
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Fires right as the text is being changed (even supplies the range of text)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Fires right before text is changing
            }

            override fun afterTextChanged(s: Editable) {
                btnDsicard.isEnabled = !(arrStudents.isEmpty() || editTextTextMultiLineDescription.text.isEmpty())
                btnSave.isEnabled = !(arrStudents.isEmpty() || editTextTextMultiLineDescription.text.isEmpty())
                //tvEventDescription.text = "Event Description"
                var charactersLeft = 280 - editTextDescription.text.toString().length
                tvEventDescription.text = "${charactersLeft} Characters Left"
                //tvDisplay.setText(s.toString())
            }
        })
    }
}