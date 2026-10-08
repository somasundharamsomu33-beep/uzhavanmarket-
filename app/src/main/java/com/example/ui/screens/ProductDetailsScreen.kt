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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ProduceItem
import com.example.ui.components.CropVisualBadge
import com.example.ui.components.RealisticProductImage
import com.example.ui.theme.*
import com.example.util.LanguageManager

/**
 * Screen 8: Product Details
 * Large crop visual, Tomato Fresh Grade A, 500 kg available, ₹18/kg
 * Farmer details: Ramasamy, Tiruvannamalai, rating 4.8 (12 reviews)
 * Description
 * Quantity selector with - 100 kg +
 * Add to Cart button
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    currentLang: String,
    produce: ProduceItem,
    onBack: () -> Unit,
    onAddToCart: (ProduceItem, Double) -> Unit
) {
    var selectedQty by remember { mutableDoubleStateOf(100.0) }
    var showAddedSnackbar by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Product Details",
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Total Price",
                            fontSize = 12.sp,
                            color = AgriTextMuted
                        )
                        Text(
                            text = "₹${(produce.pricePerKg * selectedQty).toInt()}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriGreenDark
                        )
                    }

                    Button(
                        onClick = {
                            onAddToCart(produce, selectedQty)
                            showAddedSnackbar = true
                        },
                        modifier = Modifier
                            .height(50.dp)
                            .widthIn(min = 180.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AgriGreenDark)
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = LanguageManager.getString("add_to_cart", currentLang),
                            fontSize = 15.sp,
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
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            // Big Hero Realistic Agricultural Photo Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFF8FAFC))
            ) {
                RealisticProductImage(
                    cropName = produce.nameEn,
                    height = 220.dp,
                    imageSourceType = produce.imageType,
                    isAiRepresentative = produce.isAiRepresentative,
                    showSourceBadge = true
                )

                // Gallery indicator dots
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(Color(0x88FFFFFF))
                    )
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(Color(0x88FFFFFF))
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Title & Tags
            Text(
                text = if (currentLang == "ta") produce.nameTa else produce.nameEn,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = AgriGreenDark
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFDCFCE7)
                ) {
                    Text(
                        text = "Fresh",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgriGreenDark,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFFEF3C7)
                ) {
                    Text(
                        text = produce.grade.label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF92400E),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "• ${produce.availableKg.toInt()} kg available",
                    fontSize = 12.sp,
                    color = AgriTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Price per kg
            Text(
                text = "₹ ${produce.pricePerKg.toInt()} / kg",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AgriGreenDark
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Farmer Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(AgriGreenContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = AgriGreenDark)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = produce.farmerName,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgriTextPrimary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.Verified, contentDescription = "Verified Farmer", tint = AgriGreenPrimary, modifier = Modifier.size(16.dp))
                        }
                        Text(
                            text = "${produce.village}, ${produce.district}",
                            fontSize = 12.sp,
                            color = AgriTextSecondary
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFEAB308), modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "4.8 (12 reviews)", fontSize = 11.sp, color = AgriTextMuted)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Description
            Text(
                text = "Description",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = AgriGreenDark
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = produce.description,
                fontSize = 13.sp,
                color = AgriTextSecondary,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Quantity Stepper Selector
            Text(
                text = "Select Quantity (kg)",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = AgriGreenDark
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(
                    onClick = {
                        if (selectedQty > 25.0) selectedQty -= 25.0
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF1F5F9))
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = AgriGreenDark)
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF8FAFC),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Text(
                        text = "${selectedQty.toInt()} kg",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgriGreenDark,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp)
                    )
                }

                IconButton(
                    onClick = {
                        if (selectedQty < produce.availableKg) selectedQty += 25.0
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF1F5F9))
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Increase", tint = AgriGreenDark)
                }
            }

            if (showAddedSnackbar) {
                Spacer(modifier = Modifier.height(14.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFDCFCE7),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = AgriGreenPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Added ${selectedQty.toInt()} kg to cart successfully!",
                            color = AgriGreenDark,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
