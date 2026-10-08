package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.OrderRecord
import com.example.ui.theme.*
import com.example.util.LanguageManager

/**
 * Screen 13: Order Tracking
 * Order #UM12345 placed on 12 Apr 2025, 10:15 AM
 * Timeline steps:
 * 1. Order Confirmed (10:20 AM)
 * 2. Pickup Scheduled (Today, 2:00 PM)
 * 3. Out for Delivery (Today, 4:00 PM)
 * 4. Delivered (Expected by 6:00 PM)
 * Contact Support button
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderTrackingScreen(
    currentLang: String,
    order: OrderRecord,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = LanguageManager.getString("track_order", currentLang),
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
                Box(modifier = Modifier.padding(20.dp)) {
                    OutlinedButton(
                        onClick = { /* Contact Support */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AgriGreenDark)
                    ) {
                        Icon(Icons.Default.SupportAgent, contentDescription = null, tint = AgriGreenDark)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Contact Support",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriGreenDark
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
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            // Header Info
            Text(
                text = "Order #${order.orderId}",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = AgriGreenDark
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Placed on ${order.date}",
                fontSize = 12.sp,
                color = AgriTextMuted
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Timeline Steps
            order.trackingSteps.forEachIndexed { index, step ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    // Left Indicator + Connecting Line
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(36.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(if (step.isCompleted) AgriGreenPrimary else Color(0xFFE2E8F0)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (step.isCompleted) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF94A3B8))
                                )
                            }
                        }

                        if (index < order.trackingSteps.size - 1) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(48.dp)
                                    .background(if (step.isCompleted) AgriGreenPrimary else Color(0xFFE2E8F0))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    // Step Text Details
                    Column(modifier = Modifier.padding(top = 2.dp, bottom = 24.dp)) {
                        Text(
                            text = step.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (step.isCompleted) AgriTextPrimary else AgriTextMuted
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = step.timeDescription,
                            fontSize = 12.sp,
                            color = if (step.isCompleted) AgriGreenDark else AgriTextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Destination Address preview
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Delivery Location", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AgriGreenDark)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = order.deliveryAddress, fontSize = 12.sp, color = AgriTextSecondary)
                }
            }
        }
    }
}
