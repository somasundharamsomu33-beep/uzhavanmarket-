package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CartItem
import com.example.ui.components.CropVisualBadge
import com.example.ui.theme.*
import com.example.util.LanguageManager

/**
 * Screen 10: Order Summary
 * Progress Step indicator: Address -> Payment -> Confirm
 * Delivery Address: Hotel Sunrise, 123 Main Road, Tiruvannamalai - 606601 (Change)
 * Order Items: Tomato 100 kg * 18 = ₹1,800
 * Delivery: ₹150, Platform Commission (5%): ₹90
 * Total Amount: ₹2,040
 * "Proceed to Payment" button
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderSummaryScreen(
    currentLang: String,
    cartItems: List<CartItem>,
    onBack: () -> Unit,
    onProceedToPayment: () -> Unit
) {
    val subtotal = cartItems.sumOf { it.produce.pricePerKg * it.quantityKg }
    val deliveryFee = 150.0
    val platformCommission = subtotal * 0.05
    val totalAmount = subtotal + deliveryFee + platformCommission

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = LanguageManager.getString("order_summary", currentLang),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgriGreenDark
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = AgriGreenDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White,
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = LanguageManager.getString("total", currentLang),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = AgriTextSecondary
                        )
                        Text(
                            text = "₹${totalAmount.toInt()}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AgriGreenDark
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = onProceedToPayment,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(26.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AgriGreenDark)
                    ) {
                        Text(
                            text = LanguageManager.getString("proceed_to_payment", currentLang),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            // Stepper indicator (Address -> Payment -> Confirm)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                StepIndicatorItem(label = "Address", isCompleted = true, isActive = true)
                HorizontalDivider(modifier = Modifier.width(36.dp), color = AgriGreenPrimary, thickness = 2.dp)
                StepIndicatorItem(label = "Payment", isCompleted = false, isActive = true)
                HorizontalDivider(modifier = Modifier.width(36.dp), color = Color(0xFFCBD5E1), thickness = 2.dp)
                StepIndicatorItem(label = "Confirm", isCompleted = false, isActive = false)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Delivery Address Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = LanguageManager.getString("delivery_address", currentLang),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriGreenDark
                        )
                        Text(
                            text = "Change",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AgriGreenPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Hotel Sunrise",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgriTextPrimary
                    )
                    Text(
                        text = "123, Main Road, Tiruvannamalai - 606601",
                        fontSize = 13.sp,
                        color = AgriTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Order Items
            Text(
                text = "Order Items",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = AgriGreenDark
            )

            Spacer(modifier = Modifier.height(10.dp))

            cartItems.forEach { item ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CropVisualBadge(cropName = item.produce.nameEn, size = 48.dp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.produce.nameEn,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgriTextPrimary
                            )
                            Text(
                                text = "${item.quantityKg.toInt()} kg × ₹${item.produce.pricePerKg.toInt()}",
                                fontSize = 12.sp,
                                color = AgriTextSecondary
                            )
                        }
                        Text(
                            text = "₹${(item.produce.pricePerKg * item.quantityKg).toInt()}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriGreenDark
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Fee Breakdown Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal", fontSize = 13.sp, color = AgriTextSecondary)
                        Text("₹${subtotal.toInt()}", fontSize = 13.sp, color = AgriTextPrimary, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(LanguageManager.getString("delivery_charge", currentLang), fontSize = 13.sp, color = AgriTextSecondary)
                        Text("₹${deliveryFee.toInt()}", fontSize = 13.sp, color = AgriTextPrimary, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(LanguageManager.getString("platform_commission", currentLang), fontSize = 13.sp, color = AgriTextSecondary)
                        Text("₹${platformCommission.toInt()}", fontSize = 13.sp, color = AgriTextPrimary, fontWeight = FontWeight.SemiBold)
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFE2E8F0))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total Amount", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = AgriGreenDark)
                        Text("₹${totalAmount.toInt()}", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = AgriGreenDark)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun StepIndicatorItem(
    label: String,
    isCompleted: Boolean,
    isActive: Boolean
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(
                    if (isCompleted || isActive) AgriGreenPrimary else Color(0xFFE2E8F0)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
            } else {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (isActive) Color.White else Color(0xFF94A3B8))
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
            color = if (isActive) AgriGreenDark else AgriTextMuted
        )
    }
}
