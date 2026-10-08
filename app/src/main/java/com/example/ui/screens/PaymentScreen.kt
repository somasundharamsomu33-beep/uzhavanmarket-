package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.util.LanguageManager

/**
 * Screen 11: Payment Screen
 * Select Payment Method:
 * 1. UPI (PhonePe / GPay / etc.) - Primary Recommended
 * 2. Bank Transfer
 * 3. Wallet
 * 4. Card (Debit / Credit)
 * Secure & Safe Payments: "Your payment is protected with bank-level security."
 * Pay Button: "Pay ₹2,040"
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    currentLang: String,
    totalAmount: Double,
    onBack: () -> Unit,
    onProcessPayment: (String) -> Unit
) {
    var selectedMethod by remember { mutableStateOf("UPI") }
    var isProcessing by remember { mutableStateOf(false) }

    val methods = listOf(
        PaymentOption("UPI", "UPI (PhonePe / GPay / etc.)", Icons.Default.QrCodeScanner, true),
        PaymentOption("Bank Transfer", "Bank Transfer (NEFT/RTGS)", Icons.Default.AccountBalance, false),
        PaymentOption("Wallet", "Uzhavan Agri Wallet", Icons.Default.AccountBalanceWallet, false),
        PaymentOption("Card", "Card (Debit / Credit)", Icons.Default.CreditCard, false)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = LanguageManager.getString("payment", currentLang),
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
                    Button(
                        onClick = {
                            isProcessing = true
                            onProcessPayment(selectedMethod)
                        },
                        enabled = !isProcessing,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(26.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AgriGreenDark)
                    ) {
                        if (isProcessing) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text(
                                text = "Pay ₹${totalAmount.toInt()}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = LanguageManager.getString("select_payment_method", currentLang),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = AgriGreenDark
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Methods list
            methods.forEach { opt ->
                val isSelected = selectedMethod == opt.id
                Card(
                    onClick = { selectedMethod = opt.id },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFFF0FDF4) else Color.White
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        if (isSelected) 1.5.dp else 1.dp,
                        if (isSelected) AgriGreenPrimary else Color(0xFFE2E8F0)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) Color(0xFFDCFCE7) else Color(0xFFF1F5F9)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = opt.icon,
                                contentDescription = null,
                                tint = if (isSelected) AgriGreenPrimary else AgriTextSecondary,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Text(
                            text = opt.label,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = AgriTextPrimary,
                            modifier = Modifier.weight(1f)
                        )

                        RadioButton(
                            selected = isSelected,
                            onClick = { selectedMethod = opt.id },
                            colors = RadioButtonDefaults.colors(selectedColor = AgriGreenPrimary)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Trust Card: Secure & Safe Payments
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDCFCE7)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = AgriGreenPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = LanguageManager.getString("secure_payments", currentLang),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriGreenDark
                        )
                        Text(
                            text = "Your payment is protected with bank-level security and verified direct farmer settlements.",
                            fontSize = 11.sp,
                            color = AgriTextSecondary,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }
    }
}

data class PaymentOption(
    val id: String,
    val label: String,
    val icon: ImageVector,
    val isRecommended: Boolean
)
