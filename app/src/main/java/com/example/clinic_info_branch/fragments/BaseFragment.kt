package com.example.clinic_info_branch.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.text.format.DateFormat
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.clinic_info_branch.data_base.ClinicInfo
import java.util.*


open class BaseFragment : Fragment() {

    protected lateinit var db: ClinicInfo

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = ClinicInfo.getDatabase(context)


    }

    //pick date
    @RequiresApi(Build.VERSION_CODES.N)
    protected fun handelDateButton(view: TextView): DatePicker? {
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

        return datePickerDialog?.datePicker
    }

    //pick time
    protected fun handleTimeButton(view: TextView) {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        //val is24HourFormat: Boolean = is24HourFormat(context)
        var time : Int = 0

        val timePickerDialog =
            TimePickerDialog(context, { _, i, i2 ->
                val calendar1 = Calendar.getInstance()
                calendar1.set(Calendar.HOUR, i)
                calendar1.set(Calendar.MINUTE, i2)
//                val dateText = DateFormat.format("h:mm a", calendar1).toString()
                var dateText = "$i:$i2"
                if (i2 < 10){
                   dateText = "$i:0$i2"
                }

                view.text = dateText


            }, hourOfDay, minute, true)



        timePickerDialog.show()


    }


}