package com.example.clinic_info_branch.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.format.DateFormat
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.clinic_info_branch.data_base.ClinicInfo
import com.example.clinic_info_branch.models.Notes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

const val TAG = "DailyEvent"

class DailyEvents @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    val db = ClinicInfo.getDatabase(context)
    val cells: MutableList<RectF> = mutableListOf()
    private val startHeight = 150f
    private var min = 50f
    private var max = min
    var raw = 1
    private var searchingList: MutableList<Notes> = mutableListOf()
    var dailyList: MutableList<Int> = mutableListOf()
    var startTime = 600
    var endTime = 1200
    private val calendar: Calendar = Calendar.getInstance().apply {
        Calendar.YEAR
        Calendar.MONTH
        Calendar.DATE

    }
    private val dateText = DateFormat.format("EEEE, MMM d, yyyy", calendar).toString()




    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    private val dateTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
        textSize = 85f

    }

    private val timeTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
        textSize = 85f

    }




    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            initCells()
            drawCell(it)
            drawDate(it)


        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        cells.clear()
        var i = 0

        while (i < raw){
            val top = startHeight + ((h - startHeight)/raw) * i
            val bottom = top + (h - startHeight)/raw
            val left = 20f
            val right = w - 20f



            val cell = RectF(left,top,right,bottom)
            cells.add(cell)

            i++
        }


    }

    //draw cells
    private fun drawCell(canvas: Canvas){
        var i = 0
        while (i < cells.size){
            canvas.drawRect(cells[i],linePaint)
            i++
        }
    }

    //draw date
    private fun drawDate(canvas: Canvas){

        canvas.drawText(dateText, 20f, 100f, dateTextPaint)
    }

    private fun initCells(){
        GlobalScope.launch(Dispatchers.IO) {
            searchingList = db.notesDao().getNotes(dateText)
            Log.i(TAG, "initCells: $searchingList")
            createSchedule()
            raw = searchingList.size

            withContext(Dispatchers.Main){
                onSizeChanged(width,height-20,width,height)
                invalidate()


            }

        }
    }

    private fun createSchedule(){

        searchingList.sortBy {
            it.endTimeMinute
        }

        dailyList.add(startTime)

        searchingList.forEach {
            dailyList.add(it.startTimeMinute)
            dailyList.add(it.endTimeMinute)
        }

        dailyList.add(endTime)

        var i = dailyList.size-3
        while (i > 0){
            if (dailyList[i] != dailyList[i-1]){
                dailyList.add(i-1,dailyList[i-1])
            }
            i -= 2
        }

        if (dailyList.size > 2 && dailyList[0] != dailyList[1]){

            dailyList.add(0,dailyList[0])
        }else{
            dailyList.remove(startTime)
        }

        if (dailyList.size > 2 && dailyList[dailyList.size-1] != dailyList[dailyList.size-2]){

            dailyList.add(0,dailyList[dailyList.size-2])
        }else{
            dailyList.remove(endTime)
        }

        Log.i(TAG, "createSchedule: $dailyList")

    }


}