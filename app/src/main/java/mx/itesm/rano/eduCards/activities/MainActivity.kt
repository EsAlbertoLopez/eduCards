package mx.itesm.rano.eduCards.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_main.*
import mx.itesm.rano.eduCards.Interfaces.ClickListener
import mx.itesm.rano.eduCards.Interfaces.ListListener
import mx.itesm.rano.eduCards.R
import mx.itesm.rano.eduCards.adapters.AdapterHome
import mx.itesm.rano.eduCards.fragments.*
import mx.itesm.rano.eduCards.models.CardStatistics

class MainActivity : AppCompatActivity(), ListListener {
    lateinit var actionBar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        actionBar = getSupportActionBar()!!
        if(actionBar != null) {
            actionBar?.hide()
        }
        setBottomNavBar()
        setFragment(FragmentHome())
    }

    private fun setBottomNavBar() {
        bottomNavBar.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    //actionBar?.hide()
                    setFragment(FragmentHome())
                }
                R.id.general -> {
                    //actionBar?.show()
                    setFragment(FragmentGeneral())
                }
                R.id.live -> {
                    //actionBar?.show()
                    setFragment(FragmentLive())
                }
                R.id.settings -> {
                    //actionBar?.show()
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
        val detail = Intent(this, groupList::class.java)
        detail.putExtra("INDEX", index)
        startActivity(detail)

    }
}