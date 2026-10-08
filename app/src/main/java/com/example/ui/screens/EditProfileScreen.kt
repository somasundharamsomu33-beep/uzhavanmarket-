package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserProfile
import com.example.model.UserRole
import com.example.ui.theme.*

/**
 * Screen: Farmer Profile Edit
 * Dedicated full edit option:
 * Name, Phone, District, Village, Farm Size (Acres), Main Crops, Preferred Language.
 * Includes "Save Changes", "Cancel", and unsaved changes check.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    currentLang: String,
    initialProfile: UserProfile,
    onBack: () -> Unit,
    onSaveProfile: (name: String, phone: String, district: String, village: String, farmSize: Double, mainCrops: String) -> Unit
) {
    var name by remember { mutableStateOf(initialProfile.name) }
    var phone by remember { mutableStateOf(initialProfile.phone) }
    var district by remember { mutableStateOf(initialProfile.district) }
    var village by remember { mutableStateOf(initialProfile.village) }
    var farmSizeStr by remember { mutableStateOf(initialProfile.farmSizeAcres.toString()) }
    var mainCrops by remember { mutableStateOf(initialProfile.mainCrops) }

    var showUnsavedDialog by remember { mutableStateOf(false) }
    val hasChanges = name != initialProfile.name ||
            phone != initialProfile.phone ||
            district != initialProfile.district ||
            village != initialProfile.village ||
            farmSizeStr != initialProfile.farmSizeAcres.toString() ||
            mainCrops != initialProfile.mainCrops

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
                        text = "Edit Profile ✏️",
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
                            val size = farmSizeStr.toDoubleOrNull() ?: initialProfile.farmSizeAcres
                            onSaveProfile(name, phone, district, village, size, mainCrops)
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
                            val size = farmSizeStr.toDoubleOrNull() ?: initialProfile.farmSizeAcres
                            onSaveProfile(name, phone, district, village, size, mainCrops)
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
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Personal Details", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AgriGreenDark)

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone Number") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Farm & Location Details", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AgriGreenDark)

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = district,
                        onValueChange = { district = it },
                        label = { Text("District") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = village,
                        onValueChange = { village = it },
                        label = { Text("Village / Gram") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = farmSizeStr,
                        onValueChange = { farmSizeStr = it },
                        label = { Text("Farm Size (Acres)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = mainCrops,
                        onValueChange = { mainCrops = it },
                        label = { Text("Main Crops Cultivated") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    if (showUnsavedDialog) {
        AlertDialog(
            onDismissRequest = { showUnsavedDialog = false },
            title = { Text("Discard Changes?", fontWeight = FontWeight.Bold) },
            text = { Text("You have unsaved edits in your profile. Are you sure you want to discard them?") },
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
