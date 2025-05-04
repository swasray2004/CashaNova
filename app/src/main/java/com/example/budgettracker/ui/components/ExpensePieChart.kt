package com.example.budgettracker.ui.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.budgettracker.data.models.ExpenseCategory
import com.example.budgettracker.ui.theme.BudgetTrackerTheme
import kotlin.math.min
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ExpensePieChartContent(
    expensesByCategory: Map<ExpenseCategory, Double>,
    modifier: Modifier = Modifier
) {
    val total = expensesByCategory.values.sum()
    val proportions = expensesByCategory.values.map { it / total }
    val sweepAngles = proportions.map { (it * 360f).toFloat() }


    val categoryColors = mapOf(
        ExpenseCategory.GROCERIES to Color(0xFF4CAF50),
        ExpenseCategory.BILLS to Color(0xFF2196F3),
        ExpenseCategory.ENTERTAINMENT to Color(0xFFFF9800),
        ExpenseCategory.TRANSPORT to Color(0xFF9C27B0),
        ExpenseCategory.HEALTH to Color(0xFFE91E63),
        ExpenseCategory.EDUCATION to Color(0xFF3F51B5),
        ExpenseCategory.OTHER to Color(0xFF607D8B)
    )

    val paint = remember {
        Paint().apply {
            style = Paint.Style.FILL
            isAntiAlias = true
        }
    }

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val radius = min(width, height) / 2
            val cx = width / 2
            val cy = height / 2

            var startAngle = -90f

            expensesByCategory.entries.forEachIndexed { index, (category, _) ->
                val sweepAngle = sweepAngles[index]
                val color = categoryColors[category] ?: Color.Gray

                paint.color = color.toArgb()

                drawContext.canvas.nativeCanvas.drawArc(
                    cx - radius,
                    cy - radius,
                    cx + radius,
                    cy + radius,
                    startAngle,
                    sweepAngle,
                    true,
                    paint
                )

                // Draw category label
                val midAngle = (startAngle + sweepAngle / 2) * PI / 180f



                val labelRadius = radius * 0.7f
                val x = (cx + cos(midAngle) * labelRadius).toFloat()
                val y = (cy + sin(midAngle) * labelRadius).toFloat()

                paint.color = Color.White.toArgb()
                paint.textSize = 12.dp.toPx()
                paint.textAlign = Paint.Align.CENTER

                if (sweepAngle > 15f) {
                    drawContext.canvas.nativeCanvas.drawText(
                        category.displayName,
                        x,
                        y,
                        paint
                    )
                }

                startAngle += sweepAngle
            }
        }
    }
}
@Composable
fun ExpensePieChart(expensesByCategory: Map<ExpenseCategory, Double>,modifier: Modifier = Modifier) {
    ExpensePieChartContent(expensesByCategory = expensesByCategory, modifier = Modifier.fillMaxSize())

}

@Preview(showBackground = true)
@Composable
private fun ExpensePieChartPreview () {
    val sampleData= mapOf(
        ExpenseCategory.GROCERIES to 100.0,
        ExpenseCategory.BILLS to 200.0,
        ExpenseCategory.ENTERTAINMENT to 150.0,
    )
    BudgetTrackerTheme {
        ExpensePieChartContent(expensesByCategory = sampleData, modifier = Modifier.fillMaxSize())
    }
}
