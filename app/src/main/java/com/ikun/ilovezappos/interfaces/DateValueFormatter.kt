package com.ikun.ilovezappos.interfaces

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

class DateValueFormatter : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        val sdf = SimpleDateFormat("MM/dd/yyyy")
        val netDate = Date(value.toLong() * 1000)
        return sdf.format(netDate)
    }

    override fun getAxisLabel(value: Float, axis: AxisBase): String {
        val sdf = SimpleDateFormat("MM/dd/yyyy")
        val netDate = Date(value.toLong() * 1000)
        return sdf.format(netDate)

    }
}