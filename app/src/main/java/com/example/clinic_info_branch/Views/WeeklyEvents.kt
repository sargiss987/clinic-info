package com.example.clinic_info_branch.Views

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.format.DateFormat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.*
import com.example.clinic_info_branch.R
import com.example.clinic_info_branch.data_base.ClinicInfo
import com.example.clinic_info_branch.models.Notes
import kotlinx.android.synthetic.main.fragment_weekly_events.view.*
import kotlinx.coroutines.*
import java.util.*

var RAW = 10
var increase: Int = 0
var startTime = 10
var endTime = 20

class WeeklyEvents @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val startHeight = 200f
    private val cellsCount = (0..RAW * 7)
    private val cells = mutableListOf<RectF>()
    private var cellsData = MutableList(RAW * 7) { _ -> 0 }
    private val cellsUniqueData = mutableListOf<String>()
    private val cellsStringData = mutableListOf<String>()
    private lateinit var cellNext: RectF
    private lateinit var cellPrevious: RectF
    val db = ClinicInfo.getDatabase(context)
    private lateinit var noteList: MutableList<Notes>
    private var millis: Long = 0

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
        strokeWidth = 10f
    }

    private val dateTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
        textSize = 85f
    }

    private val dayOfWeekPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
        textSize = 32f
    }

    private val rectPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
        style = Paint.Style.FILL
    }

    private val rectBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
            drawBoard(canvas, RAW)
            drawDate(canvas)
            drawTime(canvas)
            fillCells(canvas)
            initCells()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        cells.clear()
        var i = 0f
        var j = 0f
        if (w > 0 && h > 0) {
            cellNext = RectF(width - width / 7f, 0f, width.toFloat(), startHeight / 2)
            cellPrevious = RectF(0f, 0f, width - width / 7f, startHeight / 2)
            cellsCount.forEach { _ ->
                val left = w / 8 + w / 8 * i
                val right = left + w / 8
                val top = startHeight + ((h - startHeight) / RAW) * j
                val bottom = top + (h - startHeight) / RAW
                val cell = RectF(left, top, right, bottom)
                cells.add(cell)
                i++
                if (i == 7f) {
                    i = 0f
                    j++
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {


            if (cellNext.contains(event.x, event.y)) {
                cellsData = MutableList(RAW * 7) { 0 }
                increase += 7
                invalidate()
            }
            if (cellPrevious.contains(event.x, event.y)) {
                cellsData = MutableList(RAW * 7) { 0 }
                increase -= 7
                invalidate()
            }
            var i = 0
            cellsCount.forEach { _ ->
                if (cells[i].contains(event.x, event.y)) {
                    //cellsData[i] = 1
                    addNote(i)
                    invalidate()
                }
                i++
            }
        }
        return true
    }

    //fill cells
    private fun fillCells(canvas: Canvas) {
        var i = 0
        while (i < cellsData.size) {

            if (cellsData[i] == 1) {
                canvas.drawRect(cells[i], rectBorderPaint)
                canvas.drawRect(cells[i], rectPaint)
            }
            i++
        }
    }

    private fun checkCells() {
        var i = 0
        while (i < cellsUniqueData.size) {
            noteList.forEach {
                if (it.uniqueData == cellsUniqueData[i]) {
                    cellsData[i] = 1
                }
            }
            i++
        }
    }

    //init cells
    private fun initCells() {

        GlobalScope.launch(Dispatchers.IO) {
            //get notes
            noteList = db.notesDao().getAllNotes()
            checkCells()

            withContext(Dispatchers.Main) {

                invalidate()
            }
        }
    }

    //draw time
    private fun drawTime(canvas: Canvas) {

        val count: Int = ((endTime - startTime) / RAW)
        var currentTime = startTime

        canvas.drawText("$startTime", 50f, startHeight + 40f, dayOfWeekPaint)

        var i = 1
        while (i < RAW + 1) {
            currentTime += count
            canvas.drawText(
                "$currentTime", 50f,
                startHeight + ((height - startHeight) / RAW) * i - 30f, dayOfWeekPaint
            )

            canvas.drawText(
                "$currentTime", 50f,
                startHeight + ((height - startHeight) / RAW) * i + 40f, dayOfWeekPaint
            )
            i++
        }
    }

    //draw board
    private fun drawBoard(canvas: Canvas, raw: Int) {

        var i = 1
        while (i < 9) {
            canvas.drawLine(
                i * width / 8.toFloat(), startHeight, i * width / 8.toFloat(), height.toFloat(),
                linePaint
            )
            i++
        }

        i = 0
        val currentHeight = height - startHeight
        while (i < raw + 1) {
            canvas.drawLine(
                width / 8f, startHeight + (i * currentHeight / raw.toFloat()), width.toFloat(),
                startHeight + (i * currentHeight / raw.toFloat()),
                linePaint
            )
            i++
        }
    }

    //draw date
    private fun drawDate(canvas: Canvas) {

        cellsStringData.clear()
        cellsUniqueData.clear()

        val calendar = Calendar.getInstance().apply {
            Calendar.YEAR
            Calendar.MONTH
            Calendar.DATE
            add(Calendar.DAY_OF_YEAR, increase)
        }

        val dateText = DateFormat.format("yyyy, MMM", calendar).toString()
        canvas.drawText(dateText, width / 3f, 70f, dateTextPaint)

        var i = 0
        var j = 0
        var k = 0
        while (i < 7) {

            calendar.add(Calendar.DAY_OF_YEAR, +1 * j)
            val dayOfWeek = DateFormat.format("EE d", calendar).toString()
            val fullDate = DateFormat.format("EEEE, MMM d, yyyy", calendar).toString()

            canvas.drawText(
                dayOfWeek,
                width / 8 + width / 8f * i + 10,
                (startHeight - 20),
                dayOfWeekPaint
            )
            cellsStringData.add(fullDate)
            cellsUniqueData.add(fullDate)
            i++
            j = 1
        }

        j = 0
        while (j < RAW * 7 - 7) {

            cellsStringData.add(cellsStringData[j])
            cellsUniqueData.add("${cellsUniqueData[j]}, $k")
            j++

            if (j == j + 6) {
                k++
            }
        }
    }

    //add note
    private fun addNote(position: Int) {
        val mDialogView =
            LayoutInflater.from(context).inflate(R.layout.create_note_from_weekly_events, null)
        val btnStartTime = mDialogView.findViewById<Button>(R.id.startTime)
        val btnEndTime = mDialogView.findViewById<Button>(R.id.endTime)
        val txtDate = mDialogView.findViewById<TextView>(R.id.txtDate)
        val txtStartTime = mDialogView.findViewById<TextView>(R.id.txtStartTime)
        val txtEndTime = mDialogView.findViewById<TextView>(R.id.txtEndTime)
        val txtName = mDialogView.findViewById<AutoCompleteTextView>(R.id.etFullName)
        val txtPhone = mDialogView.findViewById<EditText>(R.id.etPhone)
        var name: String
        var phone: String
        var date: String
        var time: String

        txtDate.text = cellsStringData[position]

        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)
            .setTitle("Create Note")
            .setPositiveButton("Create") { _, _ ->
                name = txtName.text.toString()
                phone = txtPhone.text.toString()
                date = cellsStringData[position]
                time = "${txtStartTime.text} - ${txtEndTime.text}"

                val note = Notes(
                    0, name, phone, date, time
                )

                note.uniqueData = cellsUniqueData[position]
                invalidate()

                Toast.makeText(context, cellsUniqueData[position], Toast.LENGTH_SHORT).show()

                //insert note to database
                GlobalScope.launch(Dispatchers.Default) {
                    db.notesDao().insertNote(note)
                }
            }
            .setNegativeButton("Cancel") { _, _ ->
            }

        btnStartTime.setOnClickListener {
            millis = handleTimeButton(txtStartTime)
        }

        btnEndTime.setOnClickListener {
            handleTimeButton(txtEndTime)
        }

        mBuilder.show()
    }

    //pick time
    private fun handleTimeButton(view: TextView): Long {
        //var millis: Long
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

                //millis = calendar1.timeInMillis

            }, hourOfDay, minute, true)

        timePickerDialog.show()

        return calendar.timeInMillis
    }
}