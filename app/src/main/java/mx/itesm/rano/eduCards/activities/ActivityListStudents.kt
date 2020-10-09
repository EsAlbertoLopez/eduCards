package mx.itesm.rano.eduCards.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import mx.itesm.rano.eduCards.Interfaces.ListListener
import mx.itesm.rano.eduCards.R

class ActivityListStudents : AppCompatActivity(), ListListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_students)
    }

    override fun itemClicked(index: Int) {
        val detail = Intent(this, ActivityDetail::class.java)
        detail.putExtra("Index", index)
        startActivity(detail)
    }
}