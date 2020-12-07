package com.example.clinic_info_branch.fragments.home_fragment

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clinic_info_branch.R
import com.example.clinic_info_branch.models.Notes
import com.example.clinic_info_branch.adapters.RecNoteAdapter
import com.example.clinic_info_branch.data_base.ClinicInfo
import com.example.clinic_info_branch.fragments.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.*
import java.util.*

const val REQUEST_CALL = 1

class HomeFragment : BaseFragment(), RecNoteAdapter.RecViewClickListener {

    private lateinit var noteList: MutableList<Notes>
    private var searchingList: MutableList<Notes> = mutableListOf()
    private lateinit var viewAdapter: RecNoteAdapter
    private lateinit var phoneNumber: String
    private lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewAdapter = RecNoteAdapter(this)

        //get notes list from database
        //update display
        job = GlobalScope.launch(Dispatchers.Default) {

            delay(2000)
            noteList = db.notesDao().getAllNotes()

            withContext(Dispatchers.Main) {
                progressBarHome.visibility = View.GONE
                val calendar = Calendar.getInstance()
                viewAdapter.setList(noteList)


//                val dateText =
//                    DateFormat.format("EEEE, MMM d, yyyy", calendar).toString()
//                val todayList = noteList.filter {
//                    it.date.contains(dateText)
//                }.toMutableList()
//
//                viewAdapter.setList(todayList)

                //show list of notes
                recViewNote.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = viewAdapter
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //searching by patient name,recording time or phone
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                if (::noteList.isInitialized) {
                    searchingList.clear()
                    if (p0!!.isNotEmpty()) {
                        searchingList = noteList.filter {
                            it.name.toLowerCase(Locale.getDefault())
                                .contains(p0.toLowerCase(Locale.getDefault())) or
                                    it.phone.contains(p0) or
                                    it.time.toLowerCase(Locale.getDefault()).contains(p0)
                        }.toMutableList()
                        viewAdapter.setList(searchingList)
                    } else {
                        viewAdapter.setList(noteList)
                    }
                }
                return true
            }
        })

        //filter data by note's date
        filter.setOnClickListener {
            val filterMenu = PopupMenu(context, filter)
            filterMenu.inflate(R.menu.filter_menu)
            filterMenu.show()

            filterMenu.setOnMenuItemClickListener { item ->

                val calendar = Calendar.getInstance()

                if (::noteList.isInitialized) when (item.itemId) {
                    R.id.allTimes -> {

                        searchingList.clear()
                        viewAdapter.setList(noteList)
                    }

                    R.id.currentMonth -> {

                        searchingList.clear()
                        val month = DateFormat.format("MMM", calendar).toString()
                        val year = DateFormat.format("yyyy", calendar).toString()

                        searchingList = noteList.filter {
                            it.date.contains(month) and
                                    it.date.contains(year)
                        }.toMutableList()

                        viewAdapter.setList(searchingList)
                    }

                    R.id.currentWeek -> {

                        searchingList.clear()
                        var i = 0
                        while (i < 7) {
                            val dateText =
                                DateFormat.format("EEEE, MMM d, yyyy", calendar).toString()
                            searchingList.plusAssign(noteList.filter {
                                it.date == dateText
                            }.toMutableList())
                            calendar.add(Calendar.DAY_OF_YEAR, +1)
                            i++
                        }
                        viewAdapter.setList(searchingList)
//                        val calendarLastWeek = calendar.add(Calendar.DAY_OF_YEAR,-7)
//                        val searchingList  = noteList.filter{
//                            it.getCalendar().after(calendarLastWeek)
//                        }
//r
//                        viewAdapter.setList(searchingList)
                    }

                    R.id.today -> {

                        searchingList.clear()
                        val dateText =
                            DateFormat.format("EEEE, MMM d, yyyy", calendar).toString()
                        searchingList = noteList.filter {
                            it.date.contains(dateText)
                        }.toMutableList()

                        viewAdapter.setList(searchingList)
                    }

                    R.id.custom -> {

                        searchingList.clear()
                        val year = calendar.get(Calendar.YEAR)
                        val month = calendar.get(Calendar.MONTH)
                        val date = calendar.get(Calendar.DATE)
                        val datePickerDialog =
                            context?.let { it ->
                                DatePickerDialog(
                                    it,
                                    { _, i, i2, i3 ->
                                        val calendar1 = Calendar.getInstance()
                                        calendar1.set(Calendar.YEAR, i)
                                        calendar1.set(Calendar.MONTH, i2)
                                        calendar1.set(Calendar.DATE, i3)
                                        val dateText =
                                            DateFormat.format("EEEE, MMM d, yyyy", calendar1)
                                                .toString()

                                        searchingList = noteList.filter {
                                            it.date == dateText
                                        }.toMutableList()
                                        viewAdapter.setList(searchingList)
                                    },
                                    year,
                                    month,
                                    date
                                )
                            }

                        datePickerDialog?.show()
                    }
                    else -> {
                        searchingList.clear()
                        viewAdapter.setList(noteList)
                    }

                }
                true
            }
        }

        //Add new note
        addNote.setOnClickListener {
            var name: String
            var phone: String
            var date: String
            var time: String
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.create_note, null)
            val btnPickDate: Button = mDialogView.findViewById(R.id.pickDate)
            val btnPickTime: Button = mDialogView.findViewById(R.id.pickTime)
            val txtDateDialog: TextView = mDialogView.findViewById(R.id.txtDateDialog)
            val txtTimeDialog: TextView = mDialogView.findViewById(R.id.txtTimeDialog)
            val etName: EditText = mDialogView.findViewById(R.id.etFullName)
            val etPhone: EditText = mDialogView.findViewById(R.id.etPhone)

            val mBuilder = AlertDialog.Builder(context)
                .setView(mDialogView)
                .setTitle("Create Note")
                .setPositiveButton("Create") { _, _ ->

                    name = etName.text.toString()
                    phone = etPhone.text.toString()
                    date = txtDateDialog.text.toString()
                    time = txtTimeDialog.text.toString()
                    //create note
                    val note = Notes(
                        0, name, phone, date, time
                    )
                    progressBarHome.visibility = View.VISIBLE

                    //insert note to database
                    GlobalScope.launch(Dispatchers.Default) {
                        val id = db.notesDao().insertNote(note)
                        val newNote = Notes(id, name, phone, date, time)
                        delay(1000)
                        withContext(Dispatchers.Main) {
                            progressBarHome.visibility = View.GONE
                            //update list
                            noteList.add(newNote)
                            viewAdapter.setList(noteList)
                        }
                    }
                }
                .setNegativeButton("Cancel") { _, _ ->

                }
            //pick date
            btnPickDate.setOnClickListener {
                handelDateButton(txtDateDialog)
            }

            //pick time
            btnPickTime.setOnClickListener {
                handleTimeButton(txtTimeDialog)
            }

            mBuilder.show()
        }
    }

    //pick date
    @RequiresApi(Build.VERSION_CODES.N)
    private fun handelDateButton(view: TextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DATE)
        val datePickerDialog =
            context?.let {
                DatePickerDialog(it, { _, i, i2, i3 ->
                    val calendar1 = Calendar.getInstance()
                    calendar1.set(Calendar.YEAR, i)
                    calendar1.set(Calendar.MONTH, i2)
                    calendar1.set(Calendar.DATE, i3)
                    val dateText = DateFormat.format("EEEE, MMM d, yyyy", calendar1).toString()
                    view.text = dateText

                }, year, month, dayOfMonth)
            }

        datePickerDialog?.show()

    }

    //pick time
    private fun handleTimeButton(view: TextView) {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        //val is24HourFormat: Boolean = is24HourFormat(context)

        val timePickerDialog =
            TimePickerDialog(context, { _, i, i2 ->
                val calendar1 = Calendar.getInstance()
                calendar1.set(Calendar.HOUR, i)
                calendar1.set(Calendar.MINUTE, i2)
                val dateText = DateFormat.format("h:mm a", calendar1).toString()
                view.text = dateText

            }, hourOfDay, minute, true)

        timePickerDialog.show()

    }

    //dialing
    override fun dialing(position: Int) {
        if (searchingList.isNotEmpty()) {
            this.phoneNumber = searchingList[position].phone
            makePhoneCall(phoneNumber)
        } else {
            this.phoneNumber = noteList[position].phone
            makePhoneCall(phoneNumber)
        }
    }

    //delete entry
    override fun delete(position: Int) {
        val note: Notes

        if (searchingList.isNotEmpty()) {
            note = searchingList[position]
            //delete note from database
            GlobalScope.launch(Dispatchers.Default) {

                db.notesDao().deleteNote(note)
                delay(1000)
                withContext(Dispatchers.Main) {

                    //update list
                    searchingList.remove(note)
                    viewAdapter.setList(searchingList)
                }
            }

        } else {
            note = noteList[position]
            //delete note from database
            GlobalScope.launch(Dispatchers.Default) {

                db.notesDao().deleteNote(note)
                delay(1000)
                withContext(Dispatchers.Main) {
                    progressBarHome.visibility = View.GONE
                    //update list
                    noteList.remove(note)
                    viewAdapter.setList(noteList)
                }
            }
        }
    }

    //make call
    //Request PHONE_CALL permission
    private fun makePhoneCall(phoneNumber: String) {

        if (phoneNumber.isNotEmpty()) {

            if (context?.let {
                    ContextCompat.checkSelfPermission(
                        it,
                        android.Manifest.permission.CALL_PHONE
                    )
                } != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    context as Activity, arrayOf<String>(android.Manifest.permission.CALL_PHONE),
                    REQUEST_CALL
                )
            } else {
                val dial = "tel:$phoneNumber"
                startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
            }
        } else {
            Toast.makeText(context, "Not Found Phone Number", Toast.LENGTH_LONG).show()
        }
    }

    //Request PHONE_CALL permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(phoneNumber)
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //job cancel
    override fun onStop() {
        super.onStop()
        job.cancel()
    }
}