package com.example.clinic_info_branch.Views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.format.DateFormat
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import com.example.clinic_info_branch.data_base.ClinicInfo
import com.example.clinic_info_branch.models.Notes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

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
    var searchingList: MutableList<Notes> = mutableListOf()
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
            val top = startHeight + ((h - startHeight)/raw + 20f) * i
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
            raw = searchingList.size

            withContext(Dispatchers.Main){
                onSizeChanged(width,height-150,width,height)
                invalidate()


            }

        }
    }


}