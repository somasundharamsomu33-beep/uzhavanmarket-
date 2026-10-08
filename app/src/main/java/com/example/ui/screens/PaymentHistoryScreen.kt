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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FarmerSaleComparison
import com.example.ui.theme.*
import com.example.util.LanguageManager

/**
 * Screen 15: Payment History / Farmer Earnings Tracking
 * Crucial Startup Value Proposition:
 * Shows total earned, net amount, and "You earned ₹2,000 more than usual local mandi route!"
 * List of transaction settlements:
 * 12 Apr 2025: ₹2,040 (UPI)
 * 8 Apr 2025: ₹850 (UPI)
 * 3 Apr 2025: ₹2,800 (Bank Transfer)
 * 28 Mar 2025: ₹1,250 (UPI)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentHistoryScreen(
    currentLang: String,
    incomeComparisons: List<FarmerSaleComparison>,
    onBack: () -> Unit
) {
    val totalEarnings = 6940.0
    val totalExtraVsMandi = incomeComparisons.sumOf { it.extraIncome }

    val transactions = listOf(
        HistoryTx("12 Apr 2025", 2040.0, "UPI", "Order #UM12345", true),
        HistoryTx("8 Apr 2025", 850.0, "UPI", "Order #UM11980", true),
        HistoryTx("3 Apr 2025", 2800.0, "Bank Transfer", "Order #UM11024", true),
        HistoryTx("28 Mar 2025", 1250.0, "UPI", "Order #UM10872", true)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Payment History",
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Farmer Earnings Impact Banner (Key requirement)
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = AgriGreenDark),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "Total Earned via Direct Sales",
                            fontSize = 13.sp,
                            color = Color(0xFFDCFCE7)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "₹${totalEarnings.toInt()}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF14532D)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Color(0xFF4ADE80), modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "You earned +₹${totalExtraVsMandi.toInt()} more than local mandi!",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF86EFAC)
                                )
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Settlement Transactions",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgriGreenDark,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(transactions) { tx ->
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
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFDCFCE7)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountBalanceWallet,
                                    contentDescription = null,
                                    tint = AgriGreenDark,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Text(
                                    text = tx.date,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AgriTextPrimary
                                )
                                Text(
                                    text = "${tx.orderRef} • ${tx.method}",
                                    fontSize = 12.sp,
                                    color = AgriTextMuted
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "₹${tx.amount.toInt()}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = AgriGreenDark
                            )
                            Text(
                                text = tx.method,
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

data class HistoryTx(
    val date: String,
    val amount: Double,
    val method: String,
    val orderRef: String,
    val isSettled: Boolean
)
