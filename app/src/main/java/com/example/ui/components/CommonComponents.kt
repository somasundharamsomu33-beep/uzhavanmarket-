package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

/**
 * Clean agricultural leaf logo matching Uzhavan Market brand identity
 */
@Composable
fun UzhavanBrandLogo(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    showText: Boolean = true,
    lightText: Boolean = false
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(AgriGreenPrimary),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(size * 0.65f)) {
                val w = this.size.width
                val h = this.size.height

                // Left Leaf
                val leftLeaf = Path().apply {
                    moveTo(w * 0.5f, h * 0.5f)
                    cubicTo(w * 0.2f, h * 0.3f, w * 0.15f, h * 0.1f, w * 0.45f, h * 0.05f)
                    cubicTo(w * 0.7f, h * 0.1f, w * 0.7f, h * 0.35f, w * 0.5f, h * 0.5f)
                    close()
                }
                drawPath(leftLeaf, color = Color(0xFF86EFAC))

                // Right Leaf
                val rightLeaf = Path().apply {
                    moveTo(w * 0.5f, h * 0.5f)
                    cubicTo(w * 0.6f, h * 0.25f, w * 0.85f, h * 0.15f, w * 0.95f, h * 0.35f)
                    cubicTo(w * 0.95f, h * 0.6f, w * 0.75f, h * 0.65f, w * 0.5f, h * 0.5f)
                    close()
                }
                drawPath(rightLeaf, color = Color.White)

                // Stem
                val stem = Path().apply {
                    moveTo(w * 0.48f, h * 0.48f)
                    cubicTo(w * 0.48f, h * 0.75f, w * 0.35f, h * 0.88f, w * 0.2f, h * 0.95f)
                    cubicTo(w * 0.38f, h * 0.9f, w * 0.55f, h * 0.75f, w * 0.55f, h * 0.48f)
                    close()
                }
                drawPath(stem, color = Color(0xFF4ADE80))
            }
        }

        if (showText) {
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "Uzhavan Market",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (lightText) Color.White else AgriGreenDark,
                    letterSpacing = (-0.3).sp
                )
                Text(
                    text = "From Farmer to Market, Fairly.",
                    fontSize = 11.sp,
                    color = if (lightText) Color(0xFFDCFCE7) else AgriTextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/**
 * Visual illustration for agricultural crops (Tomato, Onion, Banana, Brinjal, etc.)
 */
@Composable
fun CropVisualBadge(
    cropName: String,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp
) {
    val (bgColor, circleColor, accentColor) = when (cropName.lowercase()) {
        "tomato", "தக்காளி" -> Triple(Color(0xFFFFE4E6), Color(0xFFE11D48), Color(0xFF15803D))
        "onion", "வெங்காயம்" -> Triple(Color(0xFFF3E8FF), Color(0xFF9333EA), Color(0xFF22C55E))
        "banana", "வாழை" -> Triple(Color(0xFFFEF9C3), Color(0xFFEAB308), Color(0xFF854D0E))
        "brinjal", "கத்தரிக்காய்" -> Triple(Color(0xFFEDE9FE), Color(0xFF6D28D9), Color(0xFF15803D))
        "coconut", "தேங்காய்" -> Triple(Color(0xFFFEF3C7), Color(0xFFB45309), Color(0xFF15803D))
        else -> Triple(AgriGreenContainer, AgriGreenPrimary, AgriGreenLight)
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 0.7f)) {
            val w = this.size.width
            val h = this.size.height

            // Crop Body
            when (cropName.lowercase()) {
                "tomato", "தக்காளி" -> {
                    // Two juicy red tomatoes side-by-side like in reference
                    drawCircle(color = circleColor, radius = w * 0.35f, center = Offset(w * 0.38f, h * 0.58f))
                    drawCircle(color = Color(0xFFBE123C), radius = w * 0.32f, center = Offset(w * 0.68f, h * 0.52f))
                    // Green calyx leaves on top
                    drawCircle(color = accentColor, radius = w * 0.1f, center = Offset(w * 0.38f, h * 0.28f))
                    drawCircle(color = accentColor, radius = w * 0.08f, center = Offset(w * 0.68f, h * 0.24f))
                }
                "onion", "வெங்காயம்" -> {
                    // Purple-magenta bulb
                    drawCircle(color = circleColor, radius = w * 0.4f, center = Offset(w * 0.5f, h * 0.55f))
                    drawCircle(color = Color(0xFFA855F7), radius = w * 0.25f, center = Offset(w * 0.5f, h * 0.55f))
                    drawCircle(color = accentColor, radius = w * 0.08f, center = Offset(w * 0.5f, h * 0.18f))
                }
                "banana", "வாழை" -> {
                    // Bunch of yellow bananas
                    drawRoundRect(
                        color = circleColor,
                        topLeft = Offset(w * 0.2f, h * 0.35f),
                        size = Size(w * 0.6f, h * 0.35f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(16f, 16f)
                    )
                    drawCircle(color = accentColor, radius = w * 0.12f, center = Offset(w * 0.22f, h * 0.32f))
                }
                else -> {
                    drawCircle(color = circleColor, radius = w * 0.4f, center = Offset(w * 0.5f, h * 0.5f))
                    drawCircle(color = accentColor, radius = w * 0.15f, center = Offset(w * 0.5f, h * 0.2f))
                }
            }
        }
    }
}

/**
 * Agricultural Landscape Background illustration banner
 */
@Composable
fun AgriLandscapeHero(
    modifier: Modifier = Modifier,
    farmerName: String? = null,
    onListClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFF166534), Color(0xFF15803D), Color(0xFF22C55E))
                    )
                )
                .padding(18.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth(0.65f)) {
                Text(
                    text = "Better Price\nfor Your Produce",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onListClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "List now →",
                        color = AgriGreenDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            // Farmer in green field avatar visual
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.CenterEnd)
                    .clip(CircleShape)
                    .background(Color(0xFFDCFCE7)),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(70.dp)) {
                    val w = size.width
                    val h = size.height
                    // Sun
                    drawCircle(color = AgriGold, radius = w * 0.18f, center = Offset(w * 0.25f, h * 0.3f))
                    // Green hills
                    drawCircle(color = AgriGreenLight, radius = w * 0.55f, center = Offset(w * 0.5f, h * 1.1f))
                    // Farmer silhouette / hat
                    drawCircle(color = Color(0xFFD97706), radius = w * 0.2f, center = Offset(w * 0.7f, h * 0.55f))
                    drawRoundRect(
                        color = AgriGreenDark,
                        topLeft = Offset(w * 0.55f, h * 0.75f),
                        size = Size(w * 0.35f, h * 0.35f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f, 12f)
                    )
                }
            }
        }
    }
}
