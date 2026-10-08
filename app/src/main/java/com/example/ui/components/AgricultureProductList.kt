package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ProduceItem
import com.example.ui.theme.*

/**
 * A reusable scrollable list of agriculture products.
 * Displays product name, price/kg, available quantity, quality grade, farmer details,
 * and an agricultural visual placeholder image for each entry.
 */
@Composable
fun AgricultureProductList(
    products: List<ProduceItem>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    onProductClick: (ProduceItem) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .testTag("agriculture_product_list"),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = products,
            key = { it.id }
        ) { product ->
            AgricultureProductRowItem(
                product = product,
                onClick = { onProductClick(product) }
            )
        }
    }
}

/**
 * Single card entry representing an agriculture product with name, price,
 * metadata badges, farmer info, and placeholder image badge.
 */
@Composable
fun AgricultureProductRowItem(
    product: ProduceItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("product_item_${product.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Agricultural Realistic Photo Preview
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .testTag("product_image_${product.id}")
            ) {
                RealisticProductImage(
                    cropName = product.nameEn,
                    height = 72.dp,
                    imageSourceType = product.imageType,
                    isAiRepresentative = product.isAiRepresentative,
                    showSourceBadge = false
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Product Details (Name, Farmer, Quantity & Grade)
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = product.nameEn,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgriGreenDark,
                        modifier = Modifier.testTag("product_name_${product.id}")
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    // Quality Grade Chip
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFDCFCE7)
                    ) {
                        Text(
                            text = product.grade.label,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriGreenDark,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                // Farmer & Location
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${product.farmerName} • ${product.village}",
                        fontSize = 12.sp,
                        color = AgriTextSecondary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verified Farmer",
                        tint = AgriGreenPrimary,
                        modifier = Modifier.size(13.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Quantity Available
                Text(
                    text = "${product.availableKg.toInt()} kg available",
                    fontSize = 11.sp,
                    color = AgriTextMuted
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Product Price & Arrow
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "₹${product.pricePerKg.toInt()}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AgriGreenDark,
                    modifier = Modifier.testTag("product_price_${product.id}")
                )
                Text(
                    text = "/ kg",
                    fontSize = 11.sp,
                    color = AgriTextMuted
                )

                Spacer(modifier = Modifier.height(6.dp))

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "View Details",
                    tint = AgriGreenPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
