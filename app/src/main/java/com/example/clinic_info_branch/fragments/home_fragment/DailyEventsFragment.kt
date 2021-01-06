package com.example.clinic_info_branch.fragments.home_fragment

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.clinic_info_branch.R
import com.example.clinic_info_branch.fragments.base.BaseFragment
import com.example.clinic_info_branch.models.Notes

import kotlinx.android.synthetic.main.fragment_daily_events.addNote
import kotlinx.coroutines.*

class DailyEventsFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily_events, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        addNote.setOnClickListener {


            var name: String
            var phone: String
            var date: String
            var startTime : Int
            var endTime: Int
            var timeText : String
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.create_note, null)
            val txtDateDialog: TextView = mDialogView.findViewById(R.id.txtDateDialog)
            val txtTimeDialogS: TextView = mDialogView.findViewById(R.id.txtTimeDialogS)
            val txtTimeDialogE: TextView = mDialogView.findViewById(R.id.txtTimeDialogE)
            val etName: AutoCompleteTextView = mDialogView.findViewById(R.id.etFullName)
            val etPhone: EditText = mDialogView.findViewById(R.id.etPhone)
            val btnDatePicker : ImageView = mDialogView.findViewById(R.id.date)
            val btnStartTimePicker : ImageView = mDialogView.findViewById(R.id.timeS)
            val btnEndTimePicker : ImageView = mDialogView.findViewById(R.id.timeE)


            val mBuilder = AlertDialog.Builder(context)
                .setView(mDialogView)
                .setTitle("Create Note")
                .setPositiveButton("Create") { _, _ ->

                    name = etName.text.toString()
                    phone = etPhone.text.toString()
                    date = txtDateDialog.text.toString()
                    val startArray = txtTimeDialogS.text.toString().split(":")
                    val endArray = txtTimeDialogE.text.toString().split(":")
                    startTime = startArray[0].toInt() * 60 + startArray[1].toInt()
                    endTime = endArray[0].toInt() * 60 + endArray[1].toInt()
                    timeText = txtTimeDialogS.text.toString() + " - " + txtTimeDialogE.text.toString()
                    //create note
                    val note = Notes(
                        0, name, phone, date, startTime,endTime,timeText
                    )
                    Toast.makeText(context, "$note", Toast.LENGTH_SHORT).show()


                    //insert note to database
                    GlobalScope.launch(Dispatchers.Default) {
                        db.notesDao().insertNote(note)
                    }


                }
                .setNegativeButton("Cancel") { _, _ ->

                }
            //pick date
            btnDatePicker.setOnClickListener {
               val datePicker = handelDateButton(txtDateDialog)
                datePicker?.year
            }

            //pick start time
            btnStartTimePicker.setOnClickListener {
                handleTimeButton(txtTimeDialogS)
            }

            //pick end time
            btnEndTimePicker.setOnClickListener {
                handleTimeButton(txtTimeDialogE)
            }


            mBuilder.show()
        }
    }
}

