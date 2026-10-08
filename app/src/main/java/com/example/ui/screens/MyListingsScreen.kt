package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ProduceItem
import com.example.ui.components.RealisticProductImage
import com.example.ui.theme.*

/**
 * Screen: Farmer's Active Listings Management (My Listings)
 * Shows all active listings by the farmer with clear "✏️ Edit" option on each card.
 * Pre-populates Edit Listing screen upon tapping.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyListingsScreen(
    currentLang: String,
    listings: List<ProduceItem>,
    onBack: () -> Unit,
    onEditListing: (ProduceItem) -> Unit,
    onAddNewListing: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Listings",
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
                    IconButton(onClick = onAddNewListing) {
                        Icon(Icons.Default.Add, contentDescription = "Add Listing", tint = AgriGreenDark)
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
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${listings.size} Active Produce Listings",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgriTextSecondary
                    )
                    TextButton(onClick = onAddNewListing) {
                        Text("+ Add New", color = AgriGreenPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }

            items(listings) { item ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // High Quality Realistic Image
                        RealisticProductImage(
                            cropName = item.nameEn,
                            height = 140.dp,
                            imageSourceType = item.imageType,
                            isAiRepresentative = item.isAiRepresentative,
                            showSourceBadge = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = item.nameEn,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AgriGreenDark
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFFDCFCE7)
                                    ) {
                                        Text(
                                            text = item.grade.label,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = AgriGreenDark,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "Available: ${item.availableKg.toInt()} kg • ₹${item.pricePerKg.toInt()}/kg",
                                    fontSize = 13.sp,
                                    color = AgriTextSecondary
                                )
                            }

                            // ✏️ Edit Action Button
                            OutlinedButton(
                                onClick = { onEditListing(item) },
                                shape = RoundedCornerShape(20.dp),
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, AgriGreenPrimary),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Listing",
                                    tint = AgriGreenPrimary,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Edit", color = AgriGreenPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = item.description,
                            fontSize = 12.sp,
                            color = AgriTextMuted,
                            maxLines = 2
                        )
                    }
                }
            }
        }
    }
}
