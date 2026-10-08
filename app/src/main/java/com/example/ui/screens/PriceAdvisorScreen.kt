package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MarketPriceInfo
import com.example.ui.components.CropVisualBadge
import com.example.ui.theme.*
import com.example.util.LanguageManager

/**
 * Screen 6: Price Advisor
 * Displays crop image (e.g. Tomato, Tiruvannamalai)
 * Market Benchmark Price: ₹20 / kg (Today's average)
 * Farmer's Expected Price: ₹22 / kg ("Good price!")
 * Benchmark info: "Based on current market data (Agmarknet, e-NAM)"
 * "Next" button confirms and publishes the produce listing
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceAdvisorScreen(
    currentLang: String,
    crop: String,
    expectedPrice: Double,
    quantityKg: Double,
    marketInfo: MarketPriceInfo,
    onBack: () -> Unit,
    onConfirmAndPublish: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = LanguageManager.getString("price_advisor", currentLang),
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
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // Crop Header with Illustration Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CropVisualBadge(cropName = crop, size = 72.dp)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = crop,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriGreenDark
                        )
                        Text(
                            text = marketInfo.location,
                            fontSize = 13.sp,
                            color = AgriTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Market Price Card (Agmarknet / e-NAM benchmark)
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = LanguageManager.getString("market_price", currentLang),
                                fontSize = 14.sp,
                                color = AgriTextSecondary,
                                fontWeight = FontWeight.Medium
                            )
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = AgriGreenPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "₹ ${marketInfo.currentBenchmark.toInt()} ",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgriTextPrimary
                            )
                            Text(
                                text = "/ kg",
                                fontSize = 16.sp,
                                color = AgriTextSecondary,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = LanguageManager.getString("todays_avg", currentLang),
                            fontSize = 12.sp,
                            color = AgriTextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Farmer's Expected Price Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFBBF7D0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Your Expected Price",
                                fontSize = 14.sp,
                                color = AgriGreenDark,
                                fontWeight = FontWeight.SemiBold
                            )
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFDCFCE7)
                            ) {
                                Text(
                                    text = LanguageManager.getString("good_price", currentLang),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AgriGreenDark,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "₹ ${expectedPrice.toInt()} ",
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgriGreenDark
                            )
                            Text(
                                text = "/ kg",
                                fontSize = 16.sp,
                                color = AgriGreenPrimary,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Explanation Note
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = AgriGreenPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = LanguageManager.getString("based_on_market", currentLang),
                        fontSize = 12.sp,
                        color = AgriTextMuted
                    )
                }
            }

            // Bottom Confirm Button
            Button(
                onClick = onConfirmAndPublish,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AgriGreenDark)
            ) {
                Text(
                    text = "Confirm & Publish Listing",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
