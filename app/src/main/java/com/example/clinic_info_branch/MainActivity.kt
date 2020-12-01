package com.example.clinic_info_branch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.clinic_info_branch.R
import com.example.clinic_info_branch.data_base.ClinicInfo
import com.example.clinic_info_branch.fragments.home_fragment.HomeFragment
import com.example.clinic_info_branch.fragments.register_fragment.RegisterFragment
import com.example.clinic_info_branch.fragments.searching_fragment.SearchingFragment
import kotlinx.android.synthetic.main.activity_main.*

 var db: ClinicInfo? = null
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val homeFragment = HomeFragment()
        val registerFragment = RegisterFragment()
        val searchFragment = SearchingFragment()

        db = ClinicInfo.getDatabase(this)


        makeCurrentFragment(homeFragment)

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
}