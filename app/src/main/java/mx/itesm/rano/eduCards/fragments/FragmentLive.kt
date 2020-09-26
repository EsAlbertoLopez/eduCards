package mx.itesm.rano.eduCards.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import mx.itesm.rano.eduCards.R
import mx.itesm.rano.eduCards.models.Student

class FragmentLive : Fragment(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v =  inflater.inflate(R.layout.fragment_live, container, false)
        val spinner = v?.findViewById<Spinner>(R.id.optSpinner)
        val reasons = resources.getStringArray(R.array.Reasons)
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


            return v
    }
}