package mx.itesm.rano.eduCards.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_authentication_lock.*
import mx.itesm.rano.eduCards.R

class FragmentAuthenticationLock(title: String) : Fragment() {
    lateinit var root: View
    var title = title

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_authentication_lock, container, false)
        setTitle()
        return root
    }

    private fun setTitle() {
        //root.findViewById<View>(R.id.tvTitle)
        var tvTitle = root.findViewById<View>(R.id.tvTitle) as TextView
        tvTitle.text = title
    }
}