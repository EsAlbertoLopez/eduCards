package mx.itesm.rano.eduCards.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import mx.itesm.rano.eduCards.Interfaces.ListListener
import mx.itesm.rano.eduCards.R

class groupList : AppCompatActivity(), ListListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_list)
    }

    override fun itemClicked(index: Int) {
        val detail = Intent(this, ActivityListStudents::class.java)
        detail.putExtra("Index", index)
        startActivity(detail)
    }
}