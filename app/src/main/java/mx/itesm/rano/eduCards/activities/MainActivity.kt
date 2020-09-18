package mx.itesm.rano.eduCards.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_main.*
import mx.itesm.rano.eduCards.Interfaces.ListListener
import mx.itesm.rano.eduCards.R
import mx.itesm.rano.eduCards.fragments.FragmentGeneral
import mx.itesm.rano.eduCards.fragments.FragmentHome
import mx.itesm.rano.eduCards.fragments.FragmentLive
import mx.itesm.rano.eduCards.fragments.FragmentSettings

class MainActivity : AppCompatActivity(), ListListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home)
        setBottomNavBar()
    }

    private fun setBottomNavBar() {
        bottomNavBar.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    setFragment(FragmentHome())
                }
                R.id.general -> {
                    setFragment(FragmentGeneral())
                }
                R.id.live -> {
                    setFragment(FragmentLive())
                }
                R.id.settings -> {
                    setFragment(FragmentSettings())
                }
            }
            true
        }
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(fragment.toString())
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    override fun itemClicked(index: Int) {
        val detail = Intent(this, ActivityDetail::class.java)
        detail.putExtra("INDEX", index)
        startActivity(detail)
    }
}