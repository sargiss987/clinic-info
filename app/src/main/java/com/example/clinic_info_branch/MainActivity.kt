package com.example.clinic_info_branch

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.clinic_info_branch.Views.RAW
import com.example.clinic_info_branch.fragments.home_fragment.HomeFragment
import com.example.clinic_info_branch.fragments.register_fragment.RegisterFragment
import com.example.clinic_info_branch.fragments.searching_fragment.SearchingFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


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
            R.id.workingTimes -> {
                val mDialogView = LayoutInflater.from(this).inflate(R.layout.time_picker, null)
                val btnStartTime = mDialogView.findViewById<Button>(R.id.btnStartTime)
                val btnEndTime = mDialogView.findViewById<Button>(R.id.btnEndTime)
                val txtStartTime = mDialogView.findViewById<TextView>(R.id.txtTimeStart)
                val txtEndTime = mDialogView.findViewById<TextView>(R.id.txtTimeEnd)
                val etNotesCount = mDialogView.findViewById<EditText>(R.id.etNotesCount)

                val mBuilder = AlertDialog.Builder(this)
                    .setView(mDialogView)
                    .setTitle("Create Working Schedule")
                    .setPositiveButton("Create") { _, _ ->
                        RAW = etNotesCount.text.toString().toInt()
                    }
                    .setNegativeButton("Cancel") { _, _ ->

                    }

                btnStartTime.setOnClickListener {
                    handleTimeButton(txtStartTime)

                }

                btnEndTime.setOnClickListener {
                    handleTimeButton(txtEndTime)

                }

                mBuilder.show()

            }

            R.id.reset -> {
                finish()
                startActivity(intent)
            }
            R.id.about -> {
                Toast.makeText(this, R.string.about_toast, Toast.LENGTH_LONG).show()
            }

            R.id.exit -> {
                finish()
            }
        }


        return (super.onOptionsItemSelected(item))

    }


    //pick date
    @RequiresApi(Build.VERSION_CODES.N)
    private fun handelDateButton(view: TextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DATE)
        val datePickerDialog =
            DatePickerDialog(this, { _, i, i2, i3 ->
                val calendar1 = Calendar.getInstance()
                calendar1.set(Calendar.YEAR, i)
                calendar1.set(Calendar.MONTH, i2)
                calendar1.set(Calendar.DATE, i3)
                val dateText = DateFormat.format("EEEE, MMM d, yyyy", calendar1).toString()
                view.text = dateText

            }, year, month, dayOfMonth)




        datePickerDialog.show()


    }

    //pick time
    private fun handleTimeButton(view: TextView) {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        //val is24HourFormat: Boolean = is24HourFormat(context)

        val timePickerDialog =
            TimePickerDialog(this, { _, i, i2 ->
                val calendar1 = Calendar.getInstance()
                calendar1.set(Calendar.HOUR, i)
                calendar1.set(Calendar.MINUTE, i2)
                val dateText = DateFormat.format("h:mm a", calendar1).toString()
                view.text = dateText


            }, hourOfDay, minute, true)



        timePickerDialog.show()

    }

}