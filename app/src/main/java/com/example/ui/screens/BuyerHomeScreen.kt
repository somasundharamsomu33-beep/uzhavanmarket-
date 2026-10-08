package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import com.example.ui.components.AgricultureProductList
import com.example.ui.components.CropVisualBadge
import com.example.ui.components.RealisticProductImage
import com.example.ui.theme.*
import com.example.util.LanguageManager

/**
 * Screen 7: Buyer Home Marketplace
 * Location header: "Tiruvannamalai"
 * Search: "Search for vegetables, fruits, etc."
 * Hero Banner: "Fresh & Local Direct from Farmers"
 * Categories: Vegetables, Fruits, Grains, Others
 * Popular Now: Tomato (₹18/kg, 500kg), Onion (₹16/kg, 300kg), Banana, etc.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyerHomeScreen(
    currentLang: String,
    produceList: List<ProduceItem>,
    cartCount: Int,
    onProductClick: (ProduceItem) -> Unit,
    onCartClick: () -> Unit,
    onNotificationsClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var isGridView by remember { mutableStateOf(false) }

    val categories = listOf("All", "Vegetables", "Fruits", "Grains", "Others")

    val filteredList = produceList.filter { item ->
        val matchesCategory = selectedCategory == "All" || item.category.equals(selectedCategory, ignoreCase = true)
        val matchesSearch = searchQuery.isBlank() ||
                item.nameEn.contains(searchQuery, ignoreCase = true) ||
                item.nameTa.contains(searchQuery, ignoreCase = true) ||
                item.village.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AgriBackground)
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        // Location Header + Cart Badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = AgriGreenPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Tiruvannamalai, Tamil Nadu",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgriGreenDark
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onNotificationsClick) {
                    Icon(Icons.Outlined.Notifications, contentDescription = "Notifications", tint = AgriGreenDark)
                }
                IconButton(onClick = onCartClick) {
                    BadgedBox(
                        badge = {
                            if (cartCount > 0) {
                                Badge(containerColor = AgriGreenPrimary) {
                                    Text("$cartCount", color = Color.White, fontSize = 10.sp)
                                }
                            }
                        }
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart", tint = AgriGreenDark)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text(LanguageManager.getString("search_placeholder", currentLang), fontSize = 13.sp) },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = AgriGreenPrimary)
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = null, tint = AgriTextMuted)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AgriGreenPrimary,
                unfocusedBorderColor = Color(0xFFCBD5E1),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Hero Banner: "Fresh & Local Direct from Farmers"
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = AgriGreenDark)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = LanguageManager.getString("fresh_and_local", currentLang),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "100% Verified Local Tamil Farmers",
                        fontSize = 11.sp,
                        color = Color(0xFFBBF7D0)
                    )
                }

                CropVisualBadge(cropName = "Tomato", size = 60.dp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Categories Chips Row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { cat ->
                val isSelected = selectedCategory == cat
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedCategory = cat },
                    label = {
                        Text(
                            text = when (cat) {
                                "Vegetables" -> LanguageManager.getString("vegetables", currentLang)
                                "Fruits" -> LanguageManager.getString("fruits", currentLang)
                                "Grains" -> LanguageManager.getString("grains", currentLang)
                                "Others" -> LanguageManager.getString("others", currentLang)
                                else -> "All"
                            },
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AgriGreenContainer,
                        selectedLabelColor = AgriGreenDark
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Popular Now Header with Grid / List View Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = LanguageManager.getString("popular_now", currentLang),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = AgriGreenDark
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { isGridView = true },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.GridView,
                        contentDescription = "Grid View",
                        tint = if (isGridView) AgriGreenPrimary else AgriTextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(
                    onClick = { isGridView = false },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ViewList,
                        contentDescription = "List View",
                        tint = if (!isGridView) AgriGreenPrimary else AgriTextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (isGridView) {
            // Produce Grid Cards
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredList) { item ->
                    BuyerProductCard(
                        item = item,
                        currentLang = currentLang,
                        onClick = { onProductClick(item) }
                    )
                }
            }
        } else {
            // Reusable Agriculture Product List Component
            AgricultureProductList(
                products = filteredList,
                contentPadding = PaddingValues(bottom = 16.dp),
                onProductClick = onProductClick,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun BuyerProductCard(
    item: ProduceItem,
    currentLang: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Realistic Agricultural Photography Header
            RealisticProductImage(
                cropName = item.nameEn,
                height = 120.dp,
                imageSourceType = item.imageType,
                isAiRepresentative = item.isAiRepresentative,
                showSourceBadge = true
            )

            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                // Name & Grade
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (currentLang == "ta") item.nameTa else item.nameEn,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgriGreenDark,
                        maxLines = 1
                    )

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFDCFCE7)
                    ) {
                        Text(
                            text = item.grade.label,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriGreenDark,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Price & Quantity
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "₹${item.pricePerKg.toInt()}/kg",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AgriTextPrimary
                    )

                    Text(
                        text = "${item.availableKg.toInt()} kg",
                        fontSize = 11.sp,
                        color = AgriTextMuted
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Location / Farmer
                Text(
                    text = "📍 ${item.village}",
                    fontSize = 11.sp,
                    color = AgriTextSecondary,
                    maxLines = 1
                )
            }
        }
    }
}
