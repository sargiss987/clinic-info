package com.example.clinic_info_branch

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.clinic_info_branch.Views.RAW
import com.example.clinic_info_branch.fragments.home_fragment.HomeFragment
import com.example.clinic_info_branch.fragments.register_fragment.RegisterFragment
import com.example.clinic_info_branch.fragments.searching_fragment.SearchingFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.time_picker.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val homeFragment = HomeFragment()
        val registerFragment = RegisterFragment()
        val searchFragment = SearchingFragment()

        makeCurrentFragment(homeFragment)
        setSupportActionBar(findViewById(R.id.toolbar))
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.app_name)
        supportActionBar?.subtitle = "Dental clinic"
        supportActionBar?.setDisplayUseLogoEnabled(true)
        //toolbar.overflowIcon = ContextCompat.getDrawable(this, R.drawable.ic_settings)

        //button navigates fragments
        bottom_navigation.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.home -> makeCurrentFragment(homeFragment)
                R.id.register -> makeCurrentFragment(registerFragment)
                R.id.search -> makeCurrentFragment(searchFragment)
            }
            true
        }
    }

    //Navigate fragments
    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, fragment)
            commit()
        }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> {
                val mBuilder = AlertDialog.Builder(this)
                    .setTitle("Setting")
                    .setPositiveButton("Ok") { _, _ ->

                    }
                    .setNegativeButton("Cancel") { _, _ ->

                    }
                mBuilder.show()

            }
            R.id.reset -> {
                finish()
                startActivity(intent)
            }
            R.id.about -> {
                val mBuilder = AlertDialog.Builder(this)
                    .setTitle("Dental clinic")
                    .setIcon(R.drawable.ic_baseline_medical_services_24)
                    .setMessage(R.string.about_toast)
                    .setPositiveButton("Ok") { _, _ ->

                    }
                mBuilder.show()
            }
            R.id.exit ->
                finish()

        }
        return (super.onOptionsItemSelected(item))
    }

}