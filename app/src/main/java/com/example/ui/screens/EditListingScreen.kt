package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ImageSourceType
import com.example.model.ProduceItem
import com.example.model.QualityGrade
import com.example.ui.components.ImageChangeDialog
import com.example.ui.components.RealisticProductImage
import com.example.ui.theme.*

/**
 * Screen: Edit Produce Listing (✏️)
 * Allows farmer to modify:
 * - Crop Name
 * - Total / Available Quantity
 * - Price per kg
 * - Quality Grade (Grade A, B, C)
 * - Available Date
 * - Description
 * - Change Image (Camera, Gallery, AI Representative)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditListingScreen(
    currentLang: String,
    listing: ProduceItem,
    onBack: () -> Unit,
    onSaveListing: (updatedItem: ProduceItem) -> Unit
) {
    var cropName by remember { mutableStateOf(listing.nameEn) }
    var quantityStr by remember { mutableStateOf(listing.availableKg.toInt().toString()) }
    var priceStr by remember { mutableStateOf(listing.pricePerKg.toInt().toString()) }
    var grade by remember { mutableStateOf(listing.grade) }
    var availableFrom by remember { mutableStateOf(listing.availableFrom) }
    var description by remember { mutableStateOf(listing.description) }
    var imageSourceType by remember { mutableStateOf(listing.imageType) }
    var isAiRepresentative by remember { mutableStateOf(listing.isAiRepresentative) }

    var showImageDialog by remember { mutableStateOf(false) }
    var showUnsavedDialog by remember { mutableStateOf(false) }

    val hasChanges = cropName != listing.nameEn ||
            quantityStr != listing.availableKg.toInt().toString() ||
            priceStr != listing.pricePerKg.toInt().toString() ||
            grade != listing.grade ||
            availableFrom != listing.availableFrom ||
            description != listing.description ||
            imageSourceType != listing.imageType ||
            isAiRepresentative != listing.isAiRepresentative

    fun handleBack() {
        if (hasChanges) {
            showUnsavedDialog = true
        } else {
            onBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Edit Listing ✏️",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgriGreenDark
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { handleBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = AgriGreenDark)
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            val q = quantityStr.toDoubleOrNull() ?: listing.availableKg
                            val p = priceStr.toDoubleOrNull() ?: listing.pricePerKg
                            val updated = listing.copy(
                                nameEn = cropName,
                                availableKg = q,
                                pricePerKg = p,
                                grade = grade,
                                availableFrom = availableFrom,
                                description = description,
                                imageType = imageSourceType,
                                isAiRepresentative = isAiRepresentative
                            )
                            onSaveListing(updated)
                        }
                    ) {
                        Text("Save", fontWeight = FontWeight.Bold, color = AgriGreenPrimary, fontSize = 15.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = AgriBackground,
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { handleBack() },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(24.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1))
                    ) {
                        Text("Cancel", color = AgriTextSecondary)
                    }

                    Button(
                        onClick = {
                            val q = quantityStr.toDoubleOrNull() ?: listing.availableKg
                            val p = priceStr.toDoubleOrNull() ?: listing.pricePerKg
                            val updated = listing.copy(
                                nameEn = cropName,
                                availableKg = q,
                                pricePerKg = p,
                                grade = grade,
                                availableFrom = availableFrom,
                                description = description,
                                imageType = imageSourceType,
                                isAiRepresentative = isAiRepresentative
                            )
                            onSaveListing(updated)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AgriGreenDark)
                    ) {
                        Text("Save Changes", fontWeight = FontWeight.Bold, color = Color.White)
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
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            // Product Image Preview & Change Button
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Product Photograph", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AgriGreenDark)

                    Spacer(modifier = Modifier.height(10.dp))

                    RealisticProductImage(
                        cropName = cropName,
                        height = 180.dp,
                        imageSourceType = imageSourceType,
                        isAiRepresentative = isAiRepresentative,
                        showSourceBadge = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = { showImageDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9))
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, tint = AgriGreenDark, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Change Image (Photo / Gallery / AI)", color = AgriGreenDark, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Pricing & Quantity
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Produce Details", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AgriGreenDark)

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = cropName,
                        onValueChange = { cropName = it },
                        label = { Text("Crop Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = quantityStr,
                        onValueChange = { quantityStr = it },
                        label = { Text("Available Quantity (kg)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = priceStr,
                        onValueChange = { priceStr = it },
                        label = { Text("Selling Price (₹ per kg)") },
                        prefix = { Text("₹ ") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Quality Grade", fontSize = 12.sp, color = AgriTextMuted)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        QualityGrade.values().forEach { g ->
                            FilterChip(
                                selected = grade == g,
                                onClick = { grade = g },
                                label = { Text(g.label) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = AgriGreenContainer,
                                    selectedLabelColor = AgriGreenDark
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = availableFrom,
                        onValueChange = { availableFrom = it },
                        label = { Text("Available From") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Harvest Description") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    if (showImageDialog) {
        ImageChangeDialog(
            cropName = cropName,
            onDismiss = { showImageDialog = false },
            onImageSelected = { sourceType, isAi ->
                imageSourceType = sourceType
                isAiRepresentative = isAi
            }
        )
    }

    if (showUnsavedDialog) {
        AlertDialog(
            onDismissRequest = { showUnsavedDialog = false },
            title = { Text("Discard Changes?", fontWeight = FontWeight.Bold) },
            text = { Text("You have unsaved edits in your listing. Are you sure you want to discard them?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showUnsavedDialog = false
                        onBack()
                    }
                ) {
                    Text("Discard", color = Color(0xFFDC2626))
                }
            },
            dismissButton = {
                TextButton(onClick = { showUnsavedDialog = false }) {
                    Text("Keep Editing", color = AgriGreenDark)
                }
            }
        )
    }
}
