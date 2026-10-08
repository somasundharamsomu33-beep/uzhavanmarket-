package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.example.model.CartItem
import com.example.ui.components.CropVisualBadge
import com.example.ui.theme.*
import com.example.util.LanguageManager

/**
 * Screen 9: Cart Screen
 * Tomato 100 kg, ₹18/kg, quantity stepper
 * Subtotal: ₹1,800
 * Clear All option
 * "Proceed to Checkout" button
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    currentLang: String,
    cartItems: List<CartItem>,
    onBack: () -> Unit,
    onQuantityChange: (String, Double) -> Unit,
    onClearAll: () -> Unit,
    onProceedToCheckout: () -> Unit
) {
    val subtotal = cartItems.sumOf { it.produce.pricePerKg * it.quantityKg }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = LanguageManager.getString("cart", currentLang),
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
                actions = {
                    if (cartItems.isNotEmpty()) {
                        TextButton(onClick = onClearAll) {
                            Text("Clear All", color = Color(0xFFDC2626), fontWeight = FontWeight.SemiBold)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White,
        bottomBar = {
            if (cartItems.isNotEmpty()) {
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
                                text = "Total Amount",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = AgriTextSecondary
                            )
                            Text(
                                text = "₹${subtotal.toInt()}",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = AgriGreenDark
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = onProceedToCheckout,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(26.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AgriGreenDark)
                        ) {
                            Text(
                                text = LanguageManager.getString("proceed_to_checkout", currentLang),
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
        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = Color(0xFFCBD5E1),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Your Cart is Empty",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgriTextSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Explore fresh farm produce and add to cart",
                        fontSize = 12.sp,
                        color = AgriTextMuted
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(cartItems) { item ->
                    CartItemCard(
                        item = item,
                        currentLang = currentLang,
                        onQuantityChange = { delta ->
                            onQuantityChange(item.produce.id, delta)
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
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
                                Text("Subtotal", fontSize = 14.sp, color = AgriTextSecondary)
                                Text("₹${subtotal.toInt()}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AgriTextPrimary)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Estimated Delivery", fontSize = 13.sp, color = AgriTextMuted)
                                Text("₹150", fontSize = 13.sp, color = AgriTextSecondary)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemCard(
    item: CartItem,
    currentLang: String,
    onQuantityChange: (Double) -> Unit
) {
    Card(
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
            CropVisualBadge(cropName = item.produce.nameEn, size = 60.dp)

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (currentLang == "ta") item.produce.nameTa else item.produce.nameEn,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgriGreenDark
                )
                Text(
                    text = "₹${item.produce.pricePerKg.toInt()} / kg",
                    fontSize = 13.sp,
                    color = AgriTextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Stepper
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { onQuantityChange(-25.0) },
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF1F5F9))
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = null, tint = AgriGreenDark, modifier = Modifier.size(16.dp))
                    }

                    Text(
                        text = "${item.quantityKg.toInt()} kg",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgriTextPrimary
                    )

                    IconButton(
                        onClick = { onQuantityChange(25.0) },
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF1F5F9))
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = AgriGreenDark, modifier = Modifier.size(16.dp))
                    }
                }
            }

            Text(
                text = "₹${(item.produce.pricePerKg * item.quantityKg).toInt()}",
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AgriGreenDark
            )
        }
    }
}
