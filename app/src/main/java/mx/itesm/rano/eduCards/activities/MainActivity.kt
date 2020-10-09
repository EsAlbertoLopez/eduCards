package mx.itesm.rano.eduCards.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_main.*
import mx.itesm.rano.eduCards.Interfaces.ListListener
import mx.itesm.rano.eduCards.R
import mx.itesm.rano.eduCards.fragments.*

class MainActivity : AppCompatActivity(), ListListener {
    lateinit var actionBar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setBottomNavBar()
        setInitialUI()
        setFragment(FragmentHome())
    }

    private fun setBottomNavBar() {
        bottomNavBar.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    window.statusBarColor = resources.getColor(R.color.colorR)
                    bottomNavBar.itemIconTintList = resources.getColorStateList(R.color.colorPrimary)
                    bottomNavBar.itemTextColor = resources.getColorStateList(R.color.colorPrimary)
                    ivGradient.setColorFilter(ContextCompat.getColor(this,
                        R.color.colorR), android.graphics.PorterDuff.Mode.MULTIPLY)
                    bottomNavBar.menu.findItem(R.id.home).setEnabled(false)
                    bottomNavBar.menu.findItem(R.id.general).setEnabled(true)
                    bottomNavBar.menu.findItem(R.id.live).setEnabled(true)
                    bottomNavBar.menu.findItem(R.id.settings).setEnabled(true)
                    setFragment(FragmentHome())
                }
                R.id.general -> {
                    window.statusBarColor = resources.getColor(R.color.colorA)
                    bottomNavBar.itemIconTintList = resources.getColorStateList(R.color.colorPrimary)
                    bottomNavBar.itemTextColor = resources.getColorStateList(R.color.colorPrimary)
                    ivGradient.setColorFilter(ContextCompat.getColor(this,
                        R.color.colorA), android.graphics.PorterDuff.Mode.MULTIPLY)
                    bottomNavBar.menu.findItem(R.id.home).setEnabled(true)
                    bottomNavBar.menu.findItem(R.id.general).setEnabled(false)
                    bottomNavBar.menu.findItem(R.id.live).setEnabled(true)
                    bottomNavBar.menu.findItem(R.id.settings).setEnabled(true)
                    setFragment(FragmentCourse())
                }
                R.id.live -> {
                    window.statusBarColor = resources.getColor(R.color.colorN)
                    bottomNavBar.itemIconTintList = resources.getColorStateList(R.color.colorPrimary)
                    bottomNavBar.itemTextColor = resources.getColorStateList(R.color.colorPrimary)
                    ivGradient.setColorFilter(ContextCompat.getColor(this,
                        R.color.colorN), android.graphics.PorterDuff.Mode.MULTIPLY)
                    bottomNavBar.menu.findItem(R.id.home).setEnabled(true)
                    bottomNavBar.menu.findItem(R.id.general).setEnabled(true)
                    bottomNavBar.menu.findItem(R.id.live).setEnabled(false)
                    bottomNavBar.menu.findItem(R.id.settings).setEnabled(true)
                    setFragment(FragmentLive())
                }
                R.id.settings -> {
                    window.statusBarColor = resources.getColor(R.color.colorO)
                    bottomNavBar.itemIconTintList = resources.getColorStateList(R.color.colorPrimary)
                    bottomNavBar.itemTextColor = resources.getColorStateList(R.color.colorPrimary)
                    ivGradient.setColorFilter(ContextCompat.getColor(this,
                        R.color.colorO), android.graphics.PorterDuff.Mode.MULTIPLY)
                    bottomNavBar.menu.findItem(R.id.home).setEnabled(true)
                    bottomNavBar.menu.findItem(R.id.general).setEnabled(true)
                    bottomNavBar.menu.findItem(R.id.live).setEnabled(true)
                    bottomNavBar.menu.findItem(R.id.settings).setEnabled(false)
                    setFragment(FragmentSettings())
                }
            }
            true
        }
    }

    private fun setInitialUI() {
        actionBar = getSupportActionBar()!!
        if(actionBar != null) {
            actionBar?.hide()
        }
        window.statusBarColor = resources.getColor(R.color.colorR)
        bottomNavBar.itemIconTintList = resources.getColorStateList(R.color.colorPrimary)
        bottomNavBar.itemTextColor = resources.getColorStateList(R.color.colorPrimary)
        ivGradient.setColorFilter(ContextCompat.getColor(this,
            R.color.colorR), android.graphics.PorterDuff.Mode.MULTIPLY)
        bottomNavBar.menu.findItem(R.id.home).setEnabled(false)
    }

    private fun setFragment(fragment: Fragment) {
        println(fragment.javaClass.toString())
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    private fun setFragmentWithBackStack(fragment: Fragment) {
        println(fragment.javaClass.toString())
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(fragment.toString())
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun setActivity(appCompatActivity: AppCompatActivity) {
        val intent = Intent(this, appCompatActivity::class.java)
        startActivity(intent)
    }

    override fun itemClicked(index: Int) {
        //val detail = Intent(this, ActivityGroupList::class.java)
        //        //detail.putExtra("INDEX", index)
        //        //startActivity(detail)
        var currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (currentFragment is FragmentCourse) {
            setFragmentWithBackStack(FragmentGroup())
        } else if (currentFragment is FragmentGroup) {
            setFragmentWithBackStack(FragmentStudent())
        } else if (currentFragment is FragmentStudent) {
            setFragmentWithBackStack(FragmentCard())
        }
    }

}