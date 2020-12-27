package com.example.clinic_info_branch

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.clinic_info_branch.fragments.home_fragment.HomeFragment
import com.example.clinic_info_branch.fragments.register_fragment.RegisterFragment
import com.example.clinic_info_branch.fragments.searching_fragment.SearchingFragment
import kotlinx.android.synthetic.main.activity_main.*


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
            R.id.reset -> {
                finish()
                startActivity(intent)
            }
            R.id.about ->
                Toast.makeText(this, R.string.about_toast, Toast.LENGTH_LONG).show();
            R.id.exit ->
                finish()
        }
        return (super.onOptionsItemSelected(item))
    }

}