package com.example.clinic_info_branch.fragments.home_fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.text.format.DateFormat.is24HourFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clinic_info_branch.R
import com.example.clinic_info_branch.data_base.ClinicInfo
import com.example.clinic_info_branch.data_base.Notes
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

const val REQUEST_CALL = 1

class HomeFragment : Fragment(), RecNoteAdapter.RecViewClickListener {


    private lateinit var noteList: MutableList<Notes>
    private var searchingList: MutableList<Notes> = mutableListOf()
    private var db: ClinicInfo? = null
    private lateinit var viewAdapter: RecNoteAdapter
    private lateinit var phoneNumber : String


    override fun onAttach(context: Context) {
        super.onAttach(context)

        db = ClinicInfo.getDatabase(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        //get notes list from database
        GlobalScope.launch(Dispatchers.Default) {

            if (db != null) {
                noteList = db!!.notesDao().getAllNotes() as MutableList<Notes>
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


        viewAdapter = RecNoteAdapter(this)
        viewAdapter.setList(noteList)



        //show list of notes
        recViewNote.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter

        }

        //searching by patient name,recording time or phone
        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                //var searchingList: List<Notes>? = null

                searchingList.clear()
                if (p0!!.isNotEmpty()) {
                    searchingList = noteList.filter {
                        it.name.toLowerCase(Locale.getDefault())
                            .contains(p0.toLowerCase(Locale.getDefault())) or
                                it.phone.contains(p0) or
                                it.time.toLowerCase(Locale.getDefault()).contains(p0)


                    } as MutableList<Notes>
                    viewAdapter.setList(searchingList)
                } else {
                    viewAdapter.setList(noteList)
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
                when (item.itemId) {
                    R.id.allTimes -> {

                        searchingList.clear()
                        viewAdapter.setList(noteList)


                    }
                    R.id.lastMonth -> {

                        searchingList.clear()
                        val month = DateFormat.format("MMM", calendar).toString()
                        val year = DateFormat.format("yyyy", calendar).toString()

                        searchingList = noteList.filter {
                            it.date.contains(month) and
                                    it.date.contains(year)
                        }.toMutableList()

                        viewAdapter.setList(searchingList)

                    }
                    R.id.lastWeek -> {


                        searchingList.clear()
                        var i = 0
                        while (i < 7) {
                            val dateText =
                                DateFormat.format("EEEE, MMM d, yyyy", calendar).toString()
                            searchingList.plusAssign(noteList.filter {
                                it.date == dateText
                            }.toMutableList())
                            calendar.add(Calendar.DAY_OF_YEAR, -1)
                            i++
                        }
                        viewAdapter.setList(searchingList)
//                        val calendarLastWeek = calendar.add(Calendar.DAY_OF_YEAR,-7)
//                        val searchingList  = noteList.filter{
//                            it.getCalendar().after(calendarLastWeek)
//                        }
//
//                        viewAdapter.setList(searchingList)


                    }
                    R.id.today -> {

                        searchingList.clear()
                        val dateText = DateFormat.format("EEEE, MMM d, yyyy", calendar).toString()
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
                            context?.let {
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
                                        }as MutableList<Notes>
                                        viewAdapter.setList(searchingList)

                                    },
                                    year,
                                    month,
                                    date
                                )
                            }

                        datePickerDialog?.show()


                    }
                    else -> false
                }



                true
            }
        }

        //Add new note
        addNote.setOnClickListener {
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

                    //create note
                    val note = Notes(
                        0, etName.text.toString(), etPhone.text.toString(),
                        txtDateDialog.text.toString(), txtTimeDialog.text.toString()
                    )

                    //insert note to database
                    val job = GlobalScope.launch(Dispatchers.Default) {

                        db?.notesDao()?.insertNote(note)
                    }

                    //update list
                    noteList.add(note)
                    viewAdapter.setList(noteList)

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
        val YEAR = calendar.get(Calendar.YEAR)
        val MONTH = calendar.get(Calendar.MONTH)
        val DATE = calendar.get(Calendar.DATE)
        val datePickerDialog =
            context?.let {
                DatePickerDialog(it, DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
                    val calendar1 = Calendar.getInstance()
                    calendar1.set(Calendar.YEAR, i)
                    calendar1.set(Calendar.MONTH, i2)
                    calendar1.set(Calendar.DATE, i3)
                    val dateText = DateFormat.format("EEEE, MMM d, yyyy", calendar1).toString()
                    view.text = dateText

                }, YEAR, MONTH, DATE)
            }

        datePickerDialog?.show()


    }

    //pick time
    private fun handleTimeButton(view: TextView) {
        val calendar = Calendar.getInstance()
        val HOUR = calendar.get(Calendar.HOUR)
        val MINUTE = calendar.get(Calendar.MINUTE)
        val is24HourFormat: Boolean = is24HourFormat(context)

        val timePickerDialog =
            TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { timePicker, i, i2 ->
                val calendar1 = Calendar.getInstance()
                calendar1.set(Calendar.HOUR, i)
                calendar1.set(Calendar.MINUTE, i2)
                val dateText = DateFormat.format("h:mm a", calendar1).toString()
                view.text = dateText

            }, HOUR, MINUTE, is24HourFormat)

        timePickerDialog.show()


    }

    //dialing
    override fun dialing(position : Int) {
        if (searchingList.isNotEmpty()){
            this.phoneNumber = searchingList[position].phone
            makePhoneCall(phoneNumber)
        }else{
            this.phoneNumber = noteList[position].phone
            makePhoneCall(phoneNumber)
        }

    }

    //delete entry
    override fun delete(position: Int) {
        var  note: Notes? = null
        if (searchingList.isNotEmpty()){
            note  = searchingList[position]
        }else{
            note = noteList[position]
        }

        //delete note from database
         GlobalScope.launch(Dispatchers.Default) {

            db?.notesDao()?.deleteNote(note)
        }

        //update list
        noteList.remove(note)
        viewAdapter.setList(noteList)
    }

    //make call
    //Request PHONE_CALL permission
    private fun makePhoneCall(phoneNumber: String) {

        if (phoneNumber.isNotEmpty()){

            if (context?.let {
                    ContextCompat.checkSelfPermission(
                        it,
                        android.Manifest.permission.CALL_PHONE)
                } != PackageManager.PERMISSION_GRANTED){
                 ActivityCompat.requestPermissions(context as Activity,arrayOf<String>(android.Manifest.permission.CALL_PHONE) ,
                     REQUEST_CALL
                 )
            }else{
                val dial = "tel:$phoneNumber"
                startActivity(Intent(Intent.ACTION_CALL,Uri.parse(dial)))
            }
        }else{
            Toast.makeText(context,"Not Found Phone Number",Toast.LENGTH_LONG).show()
        }
    }

    //Request PHONE_CALL permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CALL){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makePhoneCall(phoneNumber)
            }else{
                Toast.makeText(context,"Permission Denied",Toast.LENGTH_SHORT).show()
            }
        }
    }
}