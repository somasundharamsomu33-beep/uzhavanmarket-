package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.UzhavanBrandLogo
import com.example.ui.theme.*

/**
 * Screen 18: End Screen / Brand Manifesto
 * Clean white background with Uzhavan Market logo
 * "Empowering Farmers. Feeding Communities."
 * Beautiful agricultural landscape illustration
 * "Let's Grow Together" action button
 */
@Composable
fun BrandEndScreen(
    onContinue: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Center Branding & Mission
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                UzhavanBrandLogo(
                    size = 64.dp,
                    showText = false
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Uzhavan\nMarket",
                    fontSize = 32.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AgriGreenDark,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Empowering Farmers.\nFeeding Communities.",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = AgriGreenPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Beautiful Agricultural Sunset Landscape Canvas
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFFFEF9C3), Color(0xFFDCFCE7), Color(0xFF86EFAC))
                            )
                        )
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height

                        // Golden Horizon Sun
                        drawCircle(color = Color(0xFFFBBF24), radius = w * 0.16f, center = Offset(w * 0.18f, h * 0.45f))

                        // Green Rolling Hills
                        val hill = Path().apply {
                            moveTo(0f, h * 0.65f)
                            cubicTo(w * 0.35f, h * 0.52f, w * 0.65f, h * 0.68f, w, h * 0.58f)
                            lineTo(w, h)
                            lineTo(0f, h)
                            close()
                        }
                        drawPath(hill, color = Color(0xFF22C55E))

                        val foregroundField = Path().apply {
                            moveTo(0f, h * 0.75f)
                            cubicTo(w * 0.4f, h * 0.72f, w * 0.7f, h * 0.82f, w, h * 0.7f)
                            lineTo(w, h)
                            lineTo(0f, h)
                            close()
                        }
                        drawPath(foregroundField, color = Color(0xFF166534))

                        // Tree Silhouettes
                        drawCircle(color = Color(0xFF14532D), radius = w * 0.08f, center = Offset(w * 0.85f, h * 0.55f))
                        drawLine(
                            color = Color(0xFF78350F),
                            start = Offset(w * 0.85f, h * 0.55f),
                            end = Offset(w * 0.85f, h * 0.75f),
                            strokeWidth = 6f
                        )
                    }
                }
            }

            // Bottom Button
            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(27.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AgriGreenDark)
            ) {
                Text(
                    text = "Let's Grow Together",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
