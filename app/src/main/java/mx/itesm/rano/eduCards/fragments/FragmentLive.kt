package mx.itesm.rano.eduCards.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import mx.itesm.rano.eduCards.R

class FragmentLive : Fragment(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v =  inflater.inflate(R.layout.fragment_live, container, false)

        initializeSpinner(inflater, v, R.id.optSpinner, resources.getStringArray(R.array.Reasons))
        initializeSpinner(inflater, v, R.id.courseSpinner, resources.getStringArray(R.array.Courses))
        initializeSpinner(inflater, v, R.id.studentSpinner, resources.getStringArray(R.array.Students))



            return v
    }

    private fun initializeSpinner(
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
}