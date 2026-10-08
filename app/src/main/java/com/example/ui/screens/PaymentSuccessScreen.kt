package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.OrderRecord
import com.example.ui.theme.*
import com.example.util.LanguageManager

/**
 * Screen 12: Payment Success Screen
 * Large green checkmark icon with celebration confetti vibe
 * "Payment Successful!"
 * ₹2,040 has been paid for your order.
 * Order Breakdown: Order #UM12345, Tomato 100kg (₹1,800), Delivery (₹150), Platform Commission (₹90), Total: ₹2,040
 * Action buttons: "Track Order", "Back to Home"
 * Confirmation note: "You will receive a confirmation via SMS and WhatsApp."
 */
@Composable
fun PaymentSuccessScreen(
    currentLang: String,
    order: OrderRecord,
    onTrackOrder: () -> Unit,
    onBackToHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Large Animated-looking Green Success Circle
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFDCFCE7)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(AgriGreenPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Success",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = LanguageManager.getString("payment_successful", currentLang),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AgriGreenDark,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "₹${order.totalAmount.toInt()} has been paid\nfor your order.",
                fontSize = 15.sp,
                color = AgriTextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Order Breakdown Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Order #${order.orderId}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgriGreenDark
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    order.items.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${item.produce.nameEn} ${item.quantityKg.toInt()} kg",
                                fontSize = 13.sp,
                                color = AgriTextSecondary
                            )
                            Text(
                                text = "₹${(item.produce.pricePerKg * item.quantityKg).toInt()}",
                                fontSize = 13.sp,
                                color = AgriTextPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Delivery Charge", fontSize = 13.sp, color = AgriTextMuted)
                        Text(text = "₹${order.deliveryFee.toInt()}", fontSize = 13.sp, color = AgriTextSecondary)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Platform Commission", fontSize = 13.sp, color = AgriTextMuted)
                        Text(text = "₹${order.platformFee.toInt()}", fontSize = 13.sp, color = AgriTextSecondary)
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFE2E8F0))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Total", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = AgriTextPrimary)
                        Text(text = "₹${order.totalAmount.toInt()}", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = AgriGreenDark)
                    }
                }
            }
        }

        // Bottom Action Buttons + SMS/WhatsApp Note
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onTrackOrder,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AgriGreenDark)
            ) {
                Text(
                    text = LanguageManager.getString("track_order", currentLang),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = onBackToHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, AgriGreenDark)
            ) {
                Text(
                    text = LanguageManager.getString("back_to_home", currentLang),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgriGreenDark
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "You will receive a confirmation\nvia SMS and WhatsApp.",
                fontSize = 12.sp,
                color = AgriTextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}
