package com.example.vpnapp.ui.selectLocation.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView

class CustomDividerItemDecoration(
    private val context: Context,
    private val dividerHeight: Int,
    private val dividerColor: Int
) : RecyclerView.ItemDecoration() {

    private val paint: Paint = Paint().apply {
        color = dividerColor
        strokeWidth = dividerHeight.toFloat()
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount

        for (i in 0 until childCount - 1) { // Skip the last item
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val left = child.left
            val right = child.right
            val top = child.bottom + params.bottomMargin
            val bottom = top + dividerHeight

            // Draw the divider
            c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
        }
    }
}