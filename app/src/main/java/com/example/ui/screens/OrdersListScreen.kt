package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.OrderRecord
import com.example.model.OrderStatus
import com.example.ui.components.CropVisualBadge
import com.example.ui.theme.*
import com.example.util.LanguageManager

/**
 * Screen 14: Orders List (My Orders)
 * Tabs: All | Active | Completed
 * Order items: Tomato 100kg (₹2,040, Out for Delivery), Onion 50kg (₹850, Delivered), Banana 200kg (₹2,800, Delivered)
 * Clicking opens Tracking
 */
@Composable
fun OrdersListScreen(
    currentLang: String,
    orders: List<OrderRecord>,
    onOrderClick: (OrderRecord) -> Unit
) {
    var selectedFilter by remember { mutableStateOf("All") }
    val tabs = listOf("All", "Active", "Completed")

    val filtered = orders.filter { o ->
        when (selectedFilter) {
            "Active" -> o.status == OrderStatus.CONFIRMED || o.status == OrderStatus.PICKUP_SCHEDULED || o.status == OrderStatus.OUT_FOR_DELIVERY
            "Completed" -> o.status == OrderStatus.DELIVERED
            else -> true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AgriBackground)
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Text(
            text = "My Orders",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = AgriGreenDark
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Filter Pills
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            tabs.forEach { tab ->
                val isSelected = selectedFilter == tab
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedFilter = tab },
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
            items(filtered) { order ->
                OrderCardItem(
                    order = order,
                    onClick = { onOrderClick(order) }
                )
            }
        }
    }
}

@Composable
fun OrderCardItem(
    order: OrderRecord,
    onClick: () -> Unit
) {
    val firstItem = order.items.firstOrNull()

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CropVisualBadge(
                cropName = firstItem?.produce?.nameEn ?: "Produce",
                size = 54.dp
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = firstItem?.produce?.nameEn ?: "Produce",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgriTextPrimary
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (order.status == OrderStatus.DELIVERED) Color(0xFFDCFCE7) else Color(0xFFFEF3C7)
                    ) {
                        Text(
                            text = if (order.status == OrderStatus.DELIVERED) "Delivered" else "Out for Delivery",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (order.status == OrderStatus.DELIVERED) AgriGreenDark else Color(0xFFB45309),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${firstItem?.quantityKg?.toInt() ?: 100} kg • ₹${order.totalAmount.toInt()}",
                    fontSize = 13.sp,
                    color = AgriTextSecondary
                )

                Text(
                    text = order.date,
                    fontSize = 11.sp,
                    color = AgriTextMuted
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = AgriTextMuted,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
