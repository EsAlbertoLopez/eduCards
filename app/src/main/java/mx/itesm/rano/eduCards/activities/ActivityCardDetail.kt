package mx.itesm.rano.eduCards.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_card_detail.*
import mx.itesm.rano.eduCards.R
import mx.itesm.rano.eduCards.fragments.FragmentCardDetail

class ActivityCardDetail : AppCompatActivity() {
    lateinit var fragment: FragmentCardDetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_detail)
        val index = intent.getIntExtra("INDEX", 0)
        fragment = fragFragmentEventDetail as FragmentCardDetail
    }
}