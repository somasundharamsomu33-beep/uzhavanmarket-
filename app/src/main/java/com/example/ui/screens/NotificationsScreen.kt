package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.model.NotificationItem
import com.example.ui.theme.*
import com.example.util.LanguageManager

/**
 * Screen 17: Notifications Screen
 * Tabs: All | Orders | Payments | Offers
 * Items:
 * 1. "Payment received" - ₹2,040 for Order #UM12345 (12 Apr, 10:45 AM)
 * 2. "Pickup scheduled" - Your produce will be picked up (12 Apr, 1:30 PM)
 * 3. "New buyer message" - Hotel Sunrise is interested in tomato (12 Apr, 11:30 AM)
 * 4. "Price update" - Tomato price increased to ₹22/kg in your area (11 Apr, 9:15 AM)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    currentLang: String,
    notifications: List<NotificationItem>,
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableStateOf("All") }
    val tabs = listOf("All", "Orders", "Payments", "Offers")

    val filtered = notifications.filter { n ->
        when (selectedTab) {
            "Orders" -> n.type == "orders"
            "Payments" -> n.type == "payments"
            "Offers" -> n.type == "offers" || n.type == "price"
            else -> true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = LanguageManager.getString("notifications", currentLang),
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
        containerColor = AgriBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            // Tabs
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                tabs.forEach { tab ->
                    val isSelected = selectedTab == tab
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedTab = tab },
                        label = { Text(tab, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AgriGreenContainer,
                            selectedLabelColor = AgriGreenDark
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filtered) { item ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when (item.type) {
                                            "payments" -> Color(0xFFDCFCE7)
                                            "price" -> Color(0xFFFEF3C7)
                                            else -> Color(0xFFE0F2FE)
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = when (item.type) {
                                        "payments" -> Icons.Default.MonetizationOn
                                        "price" -> Icons.Default.TrendingUp
                                        else -> Icons.Default.LocalShipping
                                    },
                                    contentDescription = null,
                                    tint = when (item.type) {
                                        "payments" -> AgriGreenDark
                                        "price" -> Color(0xFFB45309)
                                        else -> Color(0xFF0284C7)
                                    },
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AgriTextPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = item.message,
                                    fontSize = 12.sp,
                                    color = AgriTextSecondary,
                                    lineHeight = 16.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = item.timeAgo,
                                    fontSize = 11.sp,
                                    color = AgriTextMuted
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
