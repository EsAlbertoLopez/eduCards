package mx.itesm.rano.eduCards.activities

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_main.*
import mx.itesm.rano.eduCards.R
import mx.itesm.rano.eduCards.fragments.*
import mx.itesm.rano.eduCards.interfaces.ListListener
import java.text.DateFormat
import java.util.*


class MainActivity : AppCompatActivity(), ListListener {
    lateinit var actionBar: ActionBar
    lateinit var calendar: Calendar
    lateinit var currentDate: String
    //lateinit var fragmentHome: FragmentHome
    //lateinit var fragmentCourse : FragmentCourse
    //lateinit var fragmentLive: FragmentLive
    //lateinit var fragmentSettings : FragmentSettings
    //lateinit var currentFragment : Fragment
    var username: String? = null
    var course = ""
    var group = ""
    var student = ""
    var keyEvent = ""
    var loginFlag:Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setInitialUI()
        setCalendar()
        printPug()
    }

    private fun setInitialUI() {
        //fragmentSettings = FragmentSettings()
        if (loginFlag == true) {
            activateApplication("Home")
            setFragment(FragmentHome())
            //currentFragment = fragmentHome
        } else {
            deactivateApplication("Home")
            setFragment(FragmentAuthenticationLock("Home"))
        }
        bottomNavBar.menu.findItem(R.id.home).setEnabled(false)
        hideBottomNavBarWhenKeyboardIsShown()
    }

    fun activateApplication(screen: String) {
        setActiveBottomNavBar()
        setActiveUI(screen)
        //setMainFragments()
    }

    fun deactivateApplication(screen: String) {
        setInactiveBottomNavBar()
        setInactiveUI(screen)
    }

   // private fun setMainFragments() {
   //     fragmentHome = FragmentHome()
   //     fragmentCourse = FragmentCourse()
   //     fragmentLive = FragmentLive()
   // }

    private fun setActiveBottomNavBar() {
        bottomNavBar.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    window.statusBarColor = resources.getColor(R.color.colorR)
                    //bottomNavBar.itemIconTintList =
                    //    resources.getColorStateList(R.color.colorPrimary)
                    //bottomNavBar.itemTextColor = resources.getColorStateList(R.color.colorPrimary)
                    ivGradient.setColorFilter(
                        ContextCompat.getColor(
                            this,
                            R.color.colorR
                        ), android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                    bottomNavBar.menu.findItem(R.id.home).setEnabled(false)
                    bottomNavBar.menu.findItem(R.id.general).setEnabled(true)
                    bottomNavBar.menu.findItem(R.id.live).setEnabled(true)
                    bottomNavBar.menu.findItem(R.id.settings).setEnabled(true)
                    setFragment(FragmentHome())
                    //currentFragment = fragmentHome
                }
                R.id.general -> {
                    window.statusBarColor = resources.getColor(R.color.colorA)
                    //bottomNavBar.itemIconTintList =
                    //    resources.getColorStateList(R.color.colorPrimary)
                    //bottomNavBar.itemTextColor = resources.getColorStateList(R.color.colorPrimary)
                    ivGradient.setColorFilter(
                        ContextCompat.getColor(
                            this,
                            R.color.colorA
                        ), android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                    bottomNavBar.menu.findItem(R.id.home).setEnabled(true)
                    bottomNavBar.menu.findItem(R.id.general).setEnabled(false)
                    bottomNavBar.menu.findItem(R.id.live).setEnabled(true)
                    bottomNavBar.menu.findItem(R.id.settings).setEnabled(true)
                    setFragment(FragmentCourse())
                }
                R.id.live -> {
                    window.statusBarColor = resources.getColor(R.color.colorN)
                    //bottomNavBar.itemIconTintList =
                    //    resources.getColorStateList(R.color.colorPrimary)
                    //bottomNavBar.itemTextColor = resources.getColorStateList(R.color.colorPrimary)
                    ivGradient.setColorFilter(
                        ContextCompat.getColor(
                            this,
                            R.color.colorN
                        ), android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                    bottomNavBar.menu.findItem(R.id.home).setEnabled(true)
                    bottomNavBar.menu.findItem(R.id.general).setEnabled(true)
                    bottomNavBar.menu.findItem(R.id.live).setEnabled(false)
                    bottomNavBar.menu.findItem(R.id.settings).setEnabled(true)
                    setFragment(FragmentLive())
                }
                R.id.settings -> {
                    window.statusBarColor = resources.getColor(R.color.colorO)
                    //bottomNavBar.itemIconTintList =
                    //    resources.getColorStateList(R.color.colorPrimary)
                    //bottomNavBar.itemTextColor = resources.getColorStateList(R.color.colorPrimary)
                    ivGradient.setColorFilter(
                        ContextCompat.getColor(
                            this,
                            R.color.colorO
                        ), android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                    bottomNavBar.menu.findItem(R.id.home).setEnabled(true)
                    bottomNavBar.menu.findItem(R.id.general).setEnabled(true)
                    bottomNavBar.menu.findItem(R.id.live).setEnabled(true)
                    bottomNavBar.menu.findItem(R.id.settings).setEnabled(false)
                    setFragment(FragmentSettings())
                    //deactivateApplication("Settings")
                }
            }
            true
        }
    }

    private fun setInactiveBottomNavBar() {
        bottomNavBar.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    window.statusBarColor = resources.getColor(R.color.colorDeactivated)
                    //bottomNavBar.itemIconTintList =
                    //    resources.getColorStateList(R.color.colorPrimary)
                    //bottomNavBar.itemTextColor = resources.getColorStateList(R.color.colorPrimary)
                    ivGradient.setColorFilter(
                        ContextCompat.getColor(
                            this,
                            R.color.colorDeactivated
                        ), android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                    bottomNavBar.menu.findItem(R.id.home).setEnabled(false)
                    bottomNavBar.menu.findItem(R.id.general).setEnabled(true)
                    bottomNavBar.menu.findItem(R.id.live).setEnabled(true)
                    bottomNavBar.menu.findItem(R.id.settings).setEnabled(true)
                    //var fragmentAuthenticationLock = FragmentAuthenticationLock("Home")
                    setFragment(FragmentAuthenticationLock("Home"))
                    //currentFragment = fragmentAuthenticationLock
                }
                R.id.general -> {
                    window.statusBarColor = resources.getColor(R.color.colorDeactivated)
                    //bottomNavBar.itemIconTintList =
                    //    resources.getColorStateList(R.color.colorPrimary)
                    //bottomNavBar.itemTextColor = resources.getColorStateList(R.color.colorPrimary)
                    ivGradient.setColorFilter(
                        ContextCompat.getColor(
                            this,
                            R.color.colorDeactivated
                        ), android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                    bottomNavBar.menu.findItem(R.id.home).setEnabled(true)
                    bottomNavBar.menu.findItem(R.id.general).setEnabled(false)
                    bottomNavBar.menu.findItem(R.id.live).setEnabled(true)
                    bottomNavBar.menu.findItem(R.id.settings).setEnabled(true)
                    //var fragmentAuthenticationLock = FragmentAuthenticationLock("Geeral")
                    setFragment(FragmentAuthenticationLock("General"))
                    //currentFragment = fragmentAuthenticationLock
                }
                R.id.live -> {
                    window.statusBarColor = resources.getColor(R.color.colorDeactivated)
                    //bottomNavBar.itemIconTintList =
                    //    resources.getColorStateList(R.color.colorPrimary)
                    //bottomNavBar.itemTextColor = resources.getColorStateList(R.color.colorPrimary)
                    ivGradient.setColorFilter(
                        ContextCompat.getColor(
                            this,
                            R.color.colorDeactivated
                        ), android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                    bottomNavBar.menu.findItem(R.id.home).setEnabled(true)
                    bottomNavBar.menu.findItem(R.id.general).setEnabled(true)
                    bottomNavBar.menu.findItem(R.id.live).setEnabled(false)
                    bottomNavBar.menu.findItem(R.id.settings).setEnabled(true)
                    //var fragmentAuthenticationLock = FragmentAuthenticationLock("Live")
                    setFragment(FragmentAuthenticationLock("Live"))
                    //currentFragment = fragmentAuthenticationLock
                }
                R.id.settings -> {
                    window.statusBarColor = resources.getColor(R.color.colorO)
                    //bottomNavBar.itemIconTintList =
                    //    resources.getColorStateList(R.color.colorPrimary)
                    //bottomNavBar.itemTextColor = resources.getColorStateList(R.color.colorPrimary)
                    ivGradient.setColorFilter(
                        ContextCompat.getColor(
                            this,
                            R.color.colorO
                        ), android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                    bottomNavBar.menu.findItem(R.id.home).setEnabled(true)
                    bottomNavBar.menu.findItem(R.id.general).setEnabled(true)
                    bottomNavBar.menu.findItem(R.id.live).setEnabled(true)
                    bottomNavBar.menu.findItem(R.id.settings).setEnabled(false)
                    setFragment(FragmentSettings())
                    //currentFragment = fragmentSettings
                    //activateApplication("Settings")
                }
            }
            true
        }
    }

    private fun hideBottomNavBarWhenKeyboardIsShown() {
        window.decorView.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            window.decorView.getWindowVisibleDisplayFrame(r)
            val screenHeight = window.decorView.rootView.height
            val keypadHeight: Int = screenHeight - r.bottom
            if (keypadHeight > screenHeight * 0.15) {
                bottomNavBar.visibility = View.GONE
                ivGradient.visibility = View.GONE
            } else {
                bottomNavBar.visibility = View.VISIBLE
                ivGradient.visibility = View.VISIBLE
            }
        }
    }

    private fun setActiveUI(screen: String) {
        var color = 0
        when (screen) {
            "Home" -> color = R.color.colorR
            "General" -> color = R.color.colorA
            "Live" -> color = R.color.colorN
            "Settings" -> color = R.color.colorO
        }
        actionBar = getSupportActionBar()!!
        if(actionBar != null) {
            actionBar?.hide()
        }
        window.statusBarColor = resources.getColor(color)
        //bottomNavBar.itemIconTintList = resources.getColorStateList(R.color.colorPrimary)
        //bottomNavBar.itemTextColor = resources.getColorStateList(R.color.colorPrimary)
        ivGradient.setColorFilter(
            ContextCompat.getColor(
                this,
                color
            ), android.graphics.PorterDuff.Mode.MULTIPLY
        )
        //bottomNavBar.menu.findItem(R.id.home).setEnabled(false)
    }

    private fun setInactiveUI(screen: String) {
        var color = 0
        when (screen) {
            "Home", "General", "Live" -> color = R.color.colorDeactivated
            "Settings" -> color = R.color.colorO
        }
        actionBar = getSupportActionBar()!!
        if(actionBar != null) {
            actionBar?.hide()
        }
        window.statusBarColor = resources.getColor(color)
        //bottomNavBar.itemIconTintList = resources.getColorStateList(R.color.colorPrimary)
        //bottomNavBar.itemTextColor = resources.getColorStateList(R.color.colorPrimary)
        ivGradient.setColorFilter(
            ContextCompat.getColor(
                this,
                color
            ), android.graphics.PorterDuff.Mode.MULTIPLY
        )
        //bottomNavBar.menu.findItem(R.id.home).setEnabled(false)
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
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    private fun setActivity(appCompatActivity: AppCompatActivity) {
        val intent = Intent(this, appCompatActivity::class.java)
        startActivity(intent)
    }
    
    fun setCalendar() {
        calendar = Calendar.getInstance()
        currentDate = DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.time)
    }

    override fun itemClicked(index: Int, element: String) {
        //val detail = Intent(this, ActivityGroupList::class.java)
        //        //detail.putExtra("INDEX", index)
        //        //startActivity(detail)
        var currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (currentFragment is FragmentCourse) {
            setFragmentWithBackStack(FragmentGroup(element))
            course = element
        } else if (currentFragment is FragmentGroup) {
            group = element
            setFragmentWithBackStack(FragmentStudent(course, group))
        } else if (currentFragment is FragmentStudent) {
            student = element
            setFragmentWithBackStack(FragmentCauses(course, group, student))
        } else if (currentFragment is FragmentCauses){
            keyEvent = element
            setFragmentWithBackStack(FragmentCardDetail(course, group, student, keyEvent))
        }
    }

    fun printPug() {
        println("User : ${username}")
    }
}