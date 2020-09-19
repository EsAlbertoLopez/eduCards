package mx.itesm.rano.eduCards.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import mx.itesm.rano.eduCards.R
import mx.itesm.rano.eduCards.fragments.FragmentEventDetail

class ActivityDetail : AppCompatActivity() {
    lateinit var fragment: FragmentEventDetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val index = intent.getIntExtra("INDEX", 0)
        fragment = fragFragmentEventDetail as FragmentEventDetail
        fragment.index = index
    }
}