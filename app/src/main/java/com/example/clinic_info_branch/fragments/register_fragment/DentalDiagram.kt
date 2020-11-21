package com.example.clinic_info_branch.fragments.register_fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import com.example.clinic_info_branch.R
import com.example.clinic_info_branch.data_base.StateOfTooth

val stateOfTeethList: MutableList<StateOfTooth> = mutableListOf()

class DentalDiagram @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var cellsUp = mutableListOf<RectF>()
    private var cellsDown = mutableListOf<RectF>()
    private val cellsUpData = MutableList(16) { _ -> 0 }
    private val cellsDownData = MutableList(16) { _ -> 0 }
    private val cellsUpNum =
        mutableListOf(18, 17, 16, 15, 14, 13, 12, 11, 21, 22, 23, 24, 25, 26, 27, 28)
    private val cellsDownNum =
        mutableListOf(48, 47, 46, 45, 44, 43, 42, 41, 31, 32, 33, 34, 35, 36, 37, 38)
    private val jawUp = 1
    private val jawDown = -1


    private val boardPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        strokeWidth = 4f
    }
    private val txtPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 35f

    }

    private val xPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        strokeWidth = 10f
    }

    private val txtPaintOption = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 40f

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBoard(canvas)
        drawTeethNum(canvas)
        //drawOptions(canvas)
        var i = 0
        while (i < 16) {
            drawCell(canvas, cellsUp[i], cellsUpData[i])
            drawCell(canvas, cellsDown[i], cellsDownData[i])
            i++
        }
    }

    private fun drawBoard(canvas: Canvas) {
        var startX = 0.0f
        var endX = 0.0f
        var startY = height / 2f
        var endY = startY

        canvas.drawLine(0f, startY - startY / 8, width.toFloat(), startY - startY / 8, boardPaint)
        canvas.drawLine(0f, startY, width.toFloat(), endY, boardPaint)
        canvas.drawLine(0f, startY + startY / 8, width.toFloat(), startY + startY / 8, boardPaint)

        startX = width / 16.toFloat()
        endX = startX
        startY -= startY / 8
        endY += endY / 8
        var i = 0
        while (i < 16) {
            canvas.drawLine(startX * i, startY, endX * i, endY, boardPaint)
            i++
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        stateOfTeethList.clear()
        cellsUp.clear()
        cellsDown.clear()
        if (w > 0 && h > 0) {
            cellsDown.clear()
            cellsUp.clear()
            var i = 0
            while (i < 16) {
                var left = w / 16 * i.toFloat()
                var right = left + w / 16
                var top = h / 2.toFloat()
                var bottom = (h / 2 + h / 16).toFloat()
                var cell = RectF(left, top, right, bottom)
                cellsDown.add(cell)

                left = w / 16 * i.toFloat()
                right = left + w / 16
                top = (h / 2 - h / 16).toFloat()
                bottom = h / 2.toFloat()
                cell = RectF(left, top, right, bottom)
                cellsUp.add(cell)
                i++
            }
        }
    }

    //draw teeth diagram numbers
    private fun drawTeethNum(canvas: Canvas) {
        var i = 0
        var num1 = 18
        var num2 = 21
        var num3 = 48
        var num4 = 31
        while (i < 16) {

            if (i < 8) {
                canvas.drawText(
                    "$num1",
                    (width / 16 * i) + 10.toFloat(),
                    (height / 2 - height / 16 - 20).toFloat(),
                    txtPaint
                )
                canvas.drawText(
                    "$num3",
                    (width / 16 * i) + 10.toFloat(),
                    (height / 2 + height / 16 + 45).toFloat(),
                    txtPaint
                )
                num1--
                num3--
            } else {
                canvas.drawText(
                    "$num2",
                    (width / 16 * i) + 10.toFloat(),
                    (height / 2 - height / 16 - 20).toFloat(),
                    txtPaint
                )
                canvas.drawText(
                    "$num4",
                    (width / 16 * i) + 10.toFloat(),
                    (height / 2 + height / 16 + 45).toFloat(),
                    txtPaint
                )
                num2++
                num4++
            }
            i++
        }

    }

    //draw pathological abbreviation
    private fun drawOptions(canvas: Canvas) {
        val x = 10f
        val y = 50f
        val str = resources.getStringArray(R.array.dentalDiagram)

        var i = 0
        var j = 1
        while (i < 11) {
            canvas.drawText(str[i], x, y * j, txtPaintOption)
            j++
            i++
        }
    }

    //Point damaged tooth
    private fun drawX(canvas: Canvas, rectF: RectF) {

        canvas.drawLine(
            rectF.left + 10f,
            rectF.top + 10f,
            rectF.right - 10f,
            rectF.bottom - 10f,
            xPaint
        )
        canvas.drawLine(
            rectF.left + 10f,
            rectF.bottom - 10f,
            rectF.right - 10f,
            rectF.top + 10f,
            xPaint
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {

            var i = 0
            while (i < 16) {
                if (cellsUp[i].contains(event.x, event.y)) {
                    if (cellsUpData[i] == 0) {
                        cellsUpData[i] = 1
                        createBuilder(i, jawUp)
                        invalidate()
                    } else {
                        cellsUpData[i] = 0
                        invalidate()
                    }
                }

                if (cellsDown[i].contains(event.x, event.y)) {
                    if (cellsDownData[i] == 0) {
                        cellsDownData[i] = 1
                        createBuilder(i, jawDown)
                        invalidate()
                    } else {
                        cellsDownData[i] = 0
                        invalidate()
                    }
                }
                i++

            }

        }
        return true
    }

    private fun drawCell(canvas: Canvas, rectF: RectF, cellData: Int) {
        if (cellData == 1) {
            drawX(canvas, rectF)
        }
    }

    //Receive teeth's state
    private fun createBuilder(num: Int, jaw: Int) {
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.teeth_state, null)
        val checkBoxO = mDialogView.findViewById<CheckBox>(R.id.checkBoxO)
        val checkBoxC = mDialogView.findViewById<CheckBox>(R.id.checkBoxC)
        val checkBoxP = mDialogView.findViewById<CheckBox>(R.id.checkBoxP)
        val checkBoxPt = mDialogView.findViewById<CheckBox>(R.id.checkBoxPt)
        val checkBoxR = mDialogView.findViewById<CheckBox>(R.id.checkBoxR)
        val checkBoxI = mDialogView.findViewById<CheckBox>(R.id.checkBoxI)
        val checkBoxF = mDialogView.findViewById<CheckBox>(R.id.checkBoxF)
        val checkBoxDP = mDialogView.findViewById<CheckBox>(R.id.checkBoxDP)
        val checkBoxDC = mDialogView.findViewById<CheckBox>(R.id.checkBoxDC)
        val checkBoxCr = mDialogView.findViewById<CheckBox>(R.id.checkBoxCr)
        val checkBoxMob1 = mDialogView.findViewById<CheckBox>(R.id.checkBoxMob1)
        val checkBoxMob2 = mDialogView.findViewById<CheckBox>(R.id.checkBoxMob2)
        val checkBoxMob3 = mDialogView.findViewById<CheckBox>(R.id.checkBoxMob3)
        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)
            .setTitle("ԲԵՐԱՆԻ ԽՈՌՈՉԻ ՎԻՃԱԿԸ")
            .setNegativeButton("Cancel") { _, _ ->

            }
            .setPositiveButton("Receive") { _, _ ->

                val o =
                    if (checkBoxO.isChecked) resources.getString(R.string.O)
                    else ""
                val c =
                    if (checkBoxC.isChecked) resources.getString(R.string.C)
                    else ""
                val p =
                    if (checkBoxP.isChecked) resources.getString(R.string.P)
                    else ""
                val pt =
                    if (checkBoxPt.isChecked) resources.getString(R.string.Pt)
                    else ""
                val r =
                    if (checkBoxR.isChecked) resources.getString(R.string.R)
                    else ""
                val i =
                    if (checkBoxI.isChecked) resources.getString(R.string.I)
                    else ""
                val f =
                    if (checkBoxF.isChecked) resources.getString(R.string.F)
                    else ""
                val dp =
                    if (checkBoxDP.isChecked) resources.getString(R.string.DP)
                    else ""
                val dc =
                    if (checkBoxDC.isChecked) resources.getString(R.string.DC)
                    else ""
                val cr =
                    if (checkBoxCr.isChecked) resources.getString(R.string.Cr)
                    else ""
                val mob1 =
                    if (checkBoxMob1.isChecked) resources.getString(R.string.mob1)
                    else ""
                val mob2 =
                    if (checkBoxMob2.isChecked) resources.getString(R.string.mob2)
                    else ""
                val mob3 =
                    if (checkBoxMob3.isChecked) resources.getString(R.string.mob3)
                    else ""

                when (jaw) {
                    jawUp -> {
                        val stateOfTeeth = StateOfTooth(
                            cellsUpNum[num].toString(),
                            o,
                            c,
                            p,
                            pt,
                            r,
                            i,
                            f,
                            dp,
                            dc,
                            cr,
                            mob1
                        )

                        stateOfTeethList.add(stateOfTeeth)
                        Toast.makeText(context, "${cellsUpNum[num]} $o", Toast.LENGTH_LONG).show()
                    }
                    jawDown -> {
                        val stateOfTeeth = StateOfTooth(
                            cellsDownNum[num].toString(),
                            o,
                            c,
                            p,
                            pt,
                            r,
                            i,
                            f,
                            dp,
                            dc,
                            cr,
                            mob1
                        )
                        stateOfTeethList.add(stateOfTeeth)

                    }
                }

            }
        mBuilder.show()
    }
}
