package com.maabook.maabook.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.maabook.maabook.fragment.AboutAppFragment
import com.maabook.maabook.fragment.FavouritesFragment
import com.maabook.maabook.R
import com.maabook.maabook.fragment.DashboardFragment
import com.maabook.maabook.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var coordinatorLayout: CoordinatorLayout
    private lateinit var toolbar: Toolbar
    private lateinit var frameLayout: FrameLayout
    private lateinit var navigationView: NavigationView
    private var previousMenuItem: MenuItem? = null
    private lateinit var btn_delete_account: Button
    private lateinit var btn_log_out: Button
    private lateinit var sharedPreferences: SharedPreferences

    var name: String? = "name"
    var email: String? = "email"
    var mobileNum: String? = "mobileNum"
    var password: String? = "password"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frame)
        navigationView = findViewById(R.id.navigationView)
        btn_delete_account = findViewById(R.id.btn_delete_account)
        btn_log_out = findViewById(R.id.btn_log_out)

        name = sharedPreferences.getString("name", name.toString())
        email = sharedPreferences.getString("email", email.toString())
        mobileNum = sharedPreferences.getString("mobileNumber", mobileNum.toString())
        password = sharedPreferences.getString("password", password.toString())

        setUpToolbar()

        openDashboard()

        val actionBarDrawerToggle = ActionBarDrawerToggle(this@MainActivity, drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        onBackPressedDispatcher.addCallback(this, onBackInvokedCallback)

        navigationView.setNavigationItemSelectedListener {

            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = false
            }

            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it

            when(it.itemId){
                R.id.dashboard -> {
                    openDashboard()
                    drawerLayout.closeDrawers()
                }
                R.id.favourites -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame, FavouritesFragment()).commit()

                    supportActionBar?.title = "Favourites"
                    drawerLayout.closeDrawers()
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame, ProfileFragment()).commit()

                    supportActionBar?.title = "Profile"
                    drawerLayout.closeDrawers()
                }
                R.id.aboutApp -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame, AboutAppFragment()).commit()

                    supportActionBar?.title = "About App"
                    drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }

        btn_log_out.setOnClickListener{
            clearSharedPreference()

            Toast.makeText(this@MainActivity, "Logging Out", Toast.LENGTH_SHORT).show()

            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            intent.putExtra("name", name.toString())
            intent.putExtra("email", email.toString())
            intent.putExtra("mobileNum", mobileNum.toString())
            intent.putExtra("password", password.toString())
            startActivity(intent)
            finish()
        }

        btn_delete_account.setOnClickListener{
            clearSharedPreference()

            Toast.makeText(this@MainActivity, "Account Deleted", Toast.LENGTH_SHORT).show()

            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun clearSharedPreference() {
        sharedPreferences.edit().clear().apply()
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        return super.onOptionsItemSelected(item)
    }
    private fun openDashboard() {
        val fragment = DashboardFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, fragment)
        transaction.commit()
        supportActionBar?.title = "Dashboard"
        navigationView.setCheckedItem(R.id.dashboard)
    }

    private val onBackInvokedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            when(supportFragmentManager.findFragmentById(R.id.frame)) {
                !is DashboardFragment -> openDashboard()

                else -> finish()
            }
        }
    }
}