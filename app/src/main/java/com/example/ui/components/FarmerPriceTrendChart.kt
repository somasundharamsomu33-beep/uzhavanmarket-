package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PriceTrendAnalysis
import com.example.model.PriceTrendPoint
import com.example.ui.theme.*

/**
 * Clean farmer-friendly price trend line chart
 * Displays modal price progression over the last 7 days with clear data dots and trend summary
 */
@Composable
fun FarmerPriceTrendChart(
    points: List<PriceTrendPoint>,
    analysis: PriceTrendAnalysis,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "7-Day Modal Price Trend",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgriGreenDark
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFDCFCE7)
                ) {
                    Text(
                        text = "${analysis.direction.arrow} ${analysis.direction.label}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgriGreenDark,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Canvas Chart
            if (points.isNotEmpty()) {
                val minVal = points.minOf { it.pricePerKg } * 0.95
                val maxVal = points.maxOf { it.pricePerKg } * 1.05
                val range = (maxVal - minVal).coerceAtLeast(1.0)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .padding(horizontal = 10.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height

                        // Horizontal guideline grid lines
                        for (i in 0..3) {
                            val y = h * (i / 3f)
                            drawLine(
                                color = Color(0xFFF1F5F9),
                                start = Offset(0f, y),
                                end = Offset(w, y),
                                strokeWidth = 1.5f
                            )
                        }

                        val stepX = w / (points.size - 1)
                        val chartPath = Path()

                        val coords = points.mapIndexed { index, pt ->
                            val x = index * stepX
                            val normalizedY = 1f - ((pt.pricePerKg - minVal) / range).toFloat()
                            val y = (normalizedY * (h * 0.75f)) + (h * 0.12f)
                            Offset(x, y)
                        }

                        // Draw path line
                        chartPath.moveTo(coords[0].x, coords[0].y)
                        for (i in 1 until coords.size) {
                            chartPath.lineTo(coords[i].x, coords[i].y)
                        }

                        drawPath(
                            path = chartPath,
                            color = AgriGreenPrimary,
                            style = Stroke(width = 3.5f.dp.toPx(), cap = StrokeCap.Round)
                        )

                        // Draw Point Dots
                        coords.forEach { pt ->
                            drawCircle(color = Color.White, radius = 5.dp.toPx(), center = pt)
                            drawCircle(color = AgriGreenDark, radius = 3.5f.dp.toPx(), center = pt)
                        }
                    }
                }

                // Date labels
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    points.forEach { pt ->
                        Text(
                            text = pt.formattedDate,
                            fontSize = 10.sp,
                            color = AgriTextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Trend explanation text
            Text(
                text = analysis.summaryText,
                fontSize = 12.sp,
                color = AgriTextSecondary,
                lineHeight = 16.sp
            )
        }
    }
}
