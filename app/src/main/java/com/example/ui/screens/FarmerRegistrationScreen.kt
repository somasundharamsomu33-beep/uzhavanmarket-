package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserRole
import com.example.ui.theme.*
import com.example.util.LanguageManager

/**
 * Screen 3: Farmer Registration
 * Step 1 of 2: Full Name, Phone Number (+91), District, Village, Password
 * Matching reference screen 3
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmerRegistrationScreen(
    currentLang: String,
    onBack: () -> Unit,
    onRegisterSuccess: (name: String, phone: String, district: String, village: String) -> Unit,
    onLoginClick: () -> Unit
) {
    var fullName by remember { mutableStateOf("Ramasamy") }
    var phoneNumber by remember { mutableStateOf("+91 98765 43210") }
    var district by remember { mutableStateOf("Tiruvannamalai") }
    var village by remember { mutableStateOf("Vellore") }
    var password by remember { mutableStateOf("••••••••") }
    var passwordVisible by remember { mutableStateOf(false) }

    var expandedDistrict by remember { mutableStateOf(false) }
    val districtOptions = listOf("Tiruvannamalai", "Vellore", "Salem", "Coimbatore", "Madurai", "Thanjavur")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = LanguageManager.getString("farmer_registration", currentLang),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriGreenDark
                        )
                        Text(
                            text = LanguageManager.getString("step_indicator", currentLang),
                            fontSize = 11.sp,
                            color = AgriGreenPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
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
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.padding(top = 12.dp)) {
                // Full Name
                Text(
                    text = LanguageManager.getString("full_name", currentLang),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = AgriTextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AgriGreenPrimary,
                        unfocusedBorderColor = Color(0xFFCBD5E1)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Phone Number
                Text(
                    text = LanguageManager.getString("phone_number", currentLang),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = AgriTextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    leadingIcon = {
                        Icon(Icons.Default.Phone, contentDescription = null, tint = AgriGreenPrimary)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AgriGreenPrimary,
                        unfocusedBorderColor = Color(0xFFCBD5E1)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // District Dropdown
                Text(
                    text = LanguageManager.getString("district", currentLang),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = AgriTextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box {
                    OutlinedTextField(
                        value = district,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { expandedDistrict = true }) {
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedDistrict = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AgriGreenPrimary,
                            unfocusedBorderColor = Color(0xFFCBD5E1)
                        )
                    )
                    DropdownMenu(
                        expanded = expandedDistrict,
                        onDismissRequest = { expandedDistrict = false }
                    ) {
                        districtOptions.forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt) },
                                onClick = {
                                    district = opt
                                    expandedDistrict = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Village
                Text(
                    text = LanguageManager.getString("village", currentLang),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = AgriTextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = village,
                    onValueChange = { village = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AgriGreenPrimary,
                        unfocusedBorderColor = Color(0xFFCBD5E1)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password
                Text(
                    text = LanguageManager.getString("password", currentLang),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = AgriTextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AgriGreenPrimary,
                        unfocusedBorderColor = Color(0xFFCBD5E1)
                    )
                )
            }

            // Bottom Buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        onRegisterSuccess(fullName, phoneNumber, district, village)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AgriGreenDark)
                ) {
                    Text(
                        text = LanguageManager.getString("next", currentLang),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.clickable { onLoginClick() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Already have an account? ",
                        fontSize = 13.sp,
                        color = AgriTextSecondary
                    )
                    Text(
                        text = "Login",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgriGreenDark
                    )
                }
            }
        }
    }
}
