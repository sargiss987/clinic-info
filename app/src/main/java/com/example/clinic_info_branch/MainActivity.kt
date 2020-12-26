package com.example.clinic_info_branch

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setLogo(R.drawable.ic_baseline_medical_services_24)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                // overridePendingTransition(R.animator.anim_left, R.animator.anim_right);
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                return true

            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}