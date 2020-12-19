package com.insta.mycalendar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.clinic_info_branch.R
import java.util.*

class SimpleCalendar : LinearLayout {
    private var selectedDayButton: Button? = null
    private lateinit var days: Array<Button?>
    var weekOneLayout: LinearLayout? = null
    var weekTwoLayout: LinearLayout? = null
    var weekThreeLayout: LinearLayout? = null
    var weekFourLayout: LinearLayout? = null
    var weekFiveLayout: LinearLayout? = null
    var weekSixLayout: LinearLayout? = null
    private lateinit var weeks: Array<LinearLayout?>
    private var currentDateDay = 0
    private var chosenDateDay = 0
    private var currentDateMonth = 0
    private var chosenDateMonth = 0
    private var currentDateYear = 0
    private var chosenDateYear = 0
    private var pickedDateDay = 0
    private var pickedDateMonth = 0
    private var pickedDateYear = 0
    var userMonth = 0
    var userYear = 0
    private var mListener: DayClickListener? = null
    private var calendar: Calendar? = null
    var defaultButtonParams: LayoutParams? = null
    private var userButtonParams: LayoutParams? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }
/*
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }*/

    @SuppressLint("SetTextI18n")
    private fun init(context: Context) {
        val metrics = resources.displayMetrics
        val view: View = LayoutInflater.from(context).inflate(R.layout.simple_calendar, this, true)
        calendar = Calendar.getInstance()
        weekOneLayout = view.findViewById(R.id.calendar_week_1)
        weekTwoLayout = view.findViewById(R.id.calendar_week_2)
        weekThreeLayout = view.findViewById(R.id.calendar_week_3)
        weekFourLayout = view.findViewById(R.id.calendar_week_4)
        weekFiveLayout = view.findViewById(R.id.calendar_week_5)
        weekSixLayout = view.findViewById(R.id.calendar_week_6)
        val currentDate: TextView = view.findViewById(R.id.current_date)
        val currentMonth: TextView = view.findViewById(R.id.current_month)
        chosenDateDay = calendar!!.get(Calendar.DAY_OF_MONTH)
        currentDateDay = chosenDateDay
        if (userMonth != 0 && userYear != 0) {
            chosenDateMonth = userMonth
            currentDateMonth = chosenDateMonth
            chosenDateYear = userYear
            currentDateYear = chosenDateYear
        } else {
            chosenDateMonth = calendar!!.get(Calendar.MONTH)
            currentDateMonth = chosenDateMonth
            chosenDateYear = calendar!!.get(Calendar.YEAR)
            currentDateYear = chosenDateYear
        }
        currentDate.text = "" + currentDateDay
        currentMonth.text = ENG_MONTH_NAMES[currentDateMonth]
        initializeDaysWeeks()
        defaultButtonParams = if (userButtonParams != null) {
            userButtonParams
        } else {
            getDaysLayoutParams()
        }
        addDaysinCalendar(defaultButtonParams, context, metrics)
        initCalendarWithDate(chosenDateYear, chosenDateMonth, chosenDateDay)
    }

    private fun initializeDaysWeeks() {
        weeks = arrayOfNulls(6)
        days = arrayOfNulls<Button>(6 * 7)
        weeks[0] = weekOneLayout
        weeks[1] = weekTwoLayout
        weeks[2] = weekThreeLayout
        weeks[3] = weekFourLayout
        weeks[4] = weekFiveLayout
        weeks[5] = weekSixLayout
    }

    private fun initCalendarWithDate(year: Int, month: Int, day: Int) {
        if (calendar == null) calendar = Calendar.getInstance()
        calendar?.set(year, month, day)
        val daysInCurrentMonth: Int = calendar!!.getActualMaximum(Calendar.DAY_OF_MONTH)
        chosenDateYear = year
        chosenDateMonth = month
        chosenDateDay = day
        calendar!!.set(year, month, 1)
        val firstDayOfCurrentMonth: Int = calendar!!.get(Calendar.DAY_OF_WEEK)
        calendar!!.set(year, month, calendar!!.getActualMaximum(Calendar.DAY_OF_MONTH))
        var dayNumber = 1
        val daysLeftInFirstWeek: Int
        val indexOfDayAfterLastDayOfMonth: Int
        if (firstDayOfCurrentMonth != 1) {
            daysLeftInFirstWeek = firstDayOfCurrentMonth
            indexOfDayAfterLastDayOfMonth = daysLeftInFirstWeek + daysInCurrentMonth
            for (i in firstDayOfCurrentMonth until firstDayOfCurrentMonth + daysInCurrentMonth) {
                if (currentDateMonth == chosenDateMonth && currentDateYear == chosenDateYear && dayNumber == currentDateDay) {
                    days[i]?.setBackgroundColor(resources.getColor(R.color.pink))
                    days[i]?.setTextColor(Color.WHITE)
                } else {
                    days[i]?.setTextColor(Color.BLACK)
                    days[i]?.setBackgroundColor(Color.TRANSPARENT)
                }
                val dateArr = IntArray(3)
                dateArr[0] = dayNumber
                dateArr[1] = chosenDateMonth
                dateArr[2] = chosenDateYear
                days[i]?.tag = dateArr
                days[i]?.text = dayNumber.toString()
                days[i]?.setOnClickListener { v -> onDayClick(v) }
                ++dayNumber
            }
        } else {
            daysLeftInFirstWeek = 8
            indexOfDayAfterLastDayOfMonth = daysLeftInFirstWeek + daysInCurrentMonth
            for (i in 8 until 8 + daysInCurrentMonth) {
                if (currentDateMonth == chosenDateMonth && currentDateYear == chosenDateYear && dayNumber == currentDateDay) {
                    days[i]?.setBackgroundColor(resources.getColor(R.color.pink))
                    days[i]?.setTextColor(Color.WHITE)
                } else {
                    days[i]?.setTextColor(Color.BLACK)
                    days[i]?.setBackgroundColor(Color.TRANSPARENT)
                }
                val dateArr = IntArray(3)
                dateArr[0] = dayNumber
                dateArr[1] = chosenDateMonth
                dateArr[2] = chosenDateYear
                days[i]?.tag = dateArr
                days[i]?.text = dayNumber.toString()
                days[i]?.setOnClickListener(OnClickListener { v -> onDayClick(v) })
                ++dayNumber
            }
        }
        if (month > 0) calendar!!.set(year, month - 1, 1) else calendar!!.set(year - 1, 11, 1)
        var daysInPreviousMonth: Int = calendar!!.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in daysLeftInFirstWeek - 1 downTo 0) {
            val dateArr = IntArray(3)
            if (chosenDateMonth > 0) {
                if (currentDateMonth != chosenDateMonth - 1 || currentDateYear != chosenDateYear || daysInPreviousMonth != currentDateDay) {
                    days[i]?.setBackgroundColor(Color.TRANSPARENT)
                }
                dateArr[0] = daysInPreviousMonth
                dateArr[1] = chosenDateMonth - 1
                dateArr[2] = chosenDateYear
            } else {
                if (currentDateMonth != 11 || currentDateYear != chosenDateYear - 1 || daysInPreviousMonth != currentDateDay) {
                    days[i]?.setBackgroundColor(Color.TRANSPARENT)
                }
                dateArr[0] = daysInPreviousMonth
                dateArr[1] = 11
                dateArr[2] = chosenDateYear - 1
            }
            days[i]?.tag = dateArr
            days[i]?.text = daysInPreviousMonth--.toString()
            days[i]?.setOnClickListener(OnClickListener { v -> onDayClick(v) })
        }
        var nextMonthDaysCounter = 1
        for (i in indexOfDayAfterLastDayOfMonth until days.size) {
            val dateArr = IntArray(3)
            if (chosenDateMonth < 11) {
                if (currentDateMonth == chosenDateMonth + 1 && currentDateYear == chosenDateYear && nextMonthDaysCounter == currentDateDay) {
                    days[i]?.setBackgroundColor(resources.getColor(R.color.pink))
                } else {
                    days[i]?.setBackgroundColor(Color.TRANSPARENT)
                }
                dateArr[0] = nextMonthDaysCounter
                dateArr[1] = chosenDateMonth + 1
                dateArr[2] = chosenDateYear
            } else {
                if (currentDateMonth == 0 && currentDateYear == chosenDateYear + 1 && nextMonthDaysCounter == currentDateDay) {
                    days[i]?.setBackgroundColor(resources.getColor(R.color.pink))
                } else {
                    days[i]?.setBackgroundColor(Color.TRANSPARENT)
                }
                dateArr[0] = nextMonthDaysCounter
                dateArr[1] = 0
                dateArr[2] = chosenDateYear + 1
            }
            days[i]?.tag = dateArr
            days[i]?.setTextColor(Color.parseColor(CUSTOM_GREY))
            days[i]?.text = nextMonthDaysCounter++.toString()
            days[i]?.setOnClickListener(OnClickListener { v -> onDayClick(v) })
        }
        calendar!!.set(chosenDateYear, chosenDateMonth, chosenDateDay)
    }

    private fun onDayClick(view: View?) {
        mListener?.onDayClick(view)
        if (selectedDayButton != null) {
            if (chosenDateYear == currentDateYear && chosenDateMonth == currentDateMonth && pickedDateDay == currentDateDay) {
                selectedDayButton!!.setBackgroundColor(resources.getColor(R.color.pink))
                selectedDayButton!!.setTextColor(Color.WHITE)
            } else {
                selectedDayButton!!.setBackgroundColor(Color.TRANSPARENT)
                if (selectedDayButton?.currentTextColor !== Color.RED) {
                    selectedDayButton!!.setTextColor(
                        resources
                            .getColor(R.color.calendar_number)
                    )
                }
            }
        }
        selectedDayButton = view as Button?
        if (selectedDayButton?.tag != null) {
            val dateArray = selectedDayButton?.getTag() as IntArray
            pickedDateDay = dateArray[0]
            pickedDateMonth = dateArray[1]
            pickedDateYear = dateArray[2]
        }
        if (pickedDateYear == currentDateYear && pickedDateMonth == currentDateMonth && pickedDateDay == currentDateDay) {
            selectedDayButton?.setBackgroundColor(resources.getColor(R.color.pink))
            selectedDayButton?.setTextColor(Color.WHITE)
        } else {
            selectedDayButton?.setBackgroundColor(resources.getColor(R.color.grey))
            if (selectedDayButton?.currentTextColor !== Color.RED) {
                selectedDayButton?.setTextColor(Color.WHITE)
            }
        }
    }

    private fun addDaysinCalendar(
        buttonParams: LayoutParams?, context: Context,
        metrics: DisplayMetrics
    ) {
        var engDaysArrayCounter = 0
        for (weekNumber in 0..5) {
            for (dayInWeek in 0..6) {
                val day = Button(context)
                day.setTextColor(Color.parseColor(CUSTOM_GREY))
                day.setBackgroundColor(Color.TRANSPARENT)
                day.layoutParams = buttonParams
                day.textSize = (metrics.density.toInt() * 8).toFloat()
                day.setSingleLine()
                days[engDaysArrayCounter] = day
                weeks[weekNumber]!!.addView(day)
                ++engDaysArrayCounter
            }
        }
    }

    private fun getDaysLayoutParams(): LayoutParams {
        val buttonParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        buttonParams.weight = 1f
        return buttonParams
    }

    fun setUserDaysLayoutParams(userButtonParams: LayoutParams?) {
        this.userButtonParams = userButtonParams
    }

    fun setUserCurrentMonthYear(userMonth: Int, userYear: Int) {
        this.userMonth = userMonth
        this.userYear = userYear
    }

    fun setDayBackground(userDrawable: Drawable) {
        val userDrawable1 = userDrawable
    }

    companion object {
        private const val CUSTOM_GREY = "#a0a0a0"
        private val ENG_MONTH_NAMES = arrayOf(
            "January", "February", "March", "April",
            "May", "June", "July", "August",
            "September", "October", "November", "December"
        )
    }
}