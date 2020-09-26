package mx.itesm.rano.eduCards.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_event_detail.*
import mx.itesm.rano.eduCards.Interfaces.OnDataPass
import mx.itesm.rano.eduCards.R
import mx.itesm.rano.eduCards.models.Student

class FragmentEventDetail : Fragment() {


    var index: Int = 0
    set(value) {
        if(value >= 0) {
            field = value
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onStart() {
        super.onStart()

        if(view != null) {
            tvTitle.text = Student.students[index].name
            tvDetail.text = Student.students[index].description
            when(Student.students[index].type){
                "Violencia Física" ->  view!!.setBackgroundColor(Color.RED)
                "Violencia Verbal" -> view!!.setBackgroundColor(Color.rgb(255, 69, 0))
                "Comportamiento Inadecuado" -> view!!.setBackgroundColor(Color.rgb(255, 20, 147))
                "Logro" -> view!!.setBackgroundColor(Color.YELLOW)
                "Empatía" -> view!!.setBackgroundColor(Color.GREEN)
                else-> view!!.setBackgroundColor(Color.CYAN)

            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_detail, container, false)
    }
}