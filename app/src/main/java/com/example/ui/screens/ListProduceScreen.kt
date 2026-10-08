package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.QualityGrade
import com.example.ui.theme.*
import com.example.util.LanguageManager
import com.example.util.VoiceParser

/**
 * Screen 5: List Your Produce (Voice or Manual)
 * Tabs: Voice | Manual (Voice default)
 * Large microphone button: "Tap to speak", example hint: "500 kg tomato, ready tomorrow"
 * Populates fields automatically: Crop, Quantity, Expected Price, Available From.
 * "Next" leads to Screen 6: Price Advisor
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListProduceScreen(
    currentLang: String,
    initialCrop: String? = null,
    initialPricePerKg: Double? = null,
    onBack: () -> Unit,
    onProceedToPriceAdvisor: (crop: String, quantity: Double, expectedPrice: Double, availableFrom: String, grade: QualityGrade) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Voice, 1: Manual
    var isListening by remember { mutableStateOf(false) }
    var spokenFeedback by remember { mutableStateOf<String?>(null) }

    // Listing Fields (Pre-populated from Today's Price if passed)
    var cropName by remember { mutableStateOf(initialCrop ?: "Tomato") }
    var quantityKg by remember { mutableStateOf("500") }
    var expectedPrice by remember { mutableStateOf(initialPricePerKg?.toInt()?.toString() ?: "22") }
    var availableFrom by remember { mutableStateOf("Tomorrow") }
    var selectedGrade by remember { mutableStateOf(QualityGrade.GRADE_A) }
    var cropDropdownExpanded by remember { mutableStateOf(false) }

    val cropOptions = listOf("Tomato", "Onion", "Banana", "Brinjal", "Coconut", "Ponni Rice", "Groundnut")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = LanguageManager.getString("list_produce", currentLang),
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Segmented Tabs: Voice vs Manual
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFFF1F5F9),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Row(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(4.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (selectedTab == 0) AgriGreenDark else Color.Transparent)
                            .clickable { selectedTab = 0 },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Mic,
                                contentDescription = null,
                                tint = if (selectedTab == 0) Color.White else AgriTextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = LanguageManager.getString("voice_tab", currentLang),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedTab == 0) Color.White else AgriTextSecondary
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(4.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (selectedTab == 1) AgriGreenDark else Color.Transparent)
                            .clickable { selectedTab = 1 },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = null,
                                tint = if (selectedTab == 1) Color.White else AgriTextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = LanguageManager.getString("manual_tab", currentLang),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedTab == 1) Color.White else AgriTextSecondary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // VOICE SECTION
            if (selectedTab == 0) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Big Tap to Speak Microphone Button
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(if (isListening) Color(0xFFDC2626) else AgriGreenDark)
                            .clickable {
                                isListening = !isListening
                                if (isListening) {
                                    // Simulate voice input speech transcription
                                    val simulatedSpoken = if (currentLang == "ta") {
                                        "500 கிலோ தக்காளி, நாளை கிடைக்கும்"
                                    } else {
                                        "500 kg tomato, ready tomorrow"
                                    }
                                    spokenFeedback = simulatedSpoken
                                    val parsed = VoiceParser.parseSpokenAgriInput(simulatedSpoken)
                                    cropName = parsed.crop
                                    quantityKg = parsed.quantityKg.toInt().toString()
                                    expectedPrice = parsed.expectedPricePerKg.toInt().toString()
                                    availableFrom = parsed.availableFrom
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Tap to speak",
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = if (isListening) "Listening in ${if (currentLang == "ta") "Tamil" else "English"}..." else LanguageManager.getString("tap_to_speak", currentLang),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isListening) Color(0xFFDC2626) else AgriGreenDark
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = LanguageManager.getString("speak_hint", currentLang),
                        fontSize = 13.sp,
                        color = AgriTextMuted,
                        textAlign = TextAlign.Center
                    )

                    // Spoken voice transcription badge
                    if (spokenFeedback != null) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFDCFCE7),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF86EFAC))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AgriGreenPrimary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Heard: \"$spokenFeedback\"",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AgriGreenDark
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            // FORM FIELDS (Populated by voice or editable manually)
            Text(
                text = "Or enter details manually",
                fontSize = 13.sp,
                color = AgriTextSecondary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Crop Dropdown
            Text(text = LanguageManager.getString("crop", currentLang), fontSize = 13.sp, color = AgriTextSecondary)
            Spacer(modifier = Modifier.height(4.dp))
            Box {
                OutlinedTextField(
                    value = cropName,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { cropDropdownExpanded = true }) {
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { cropDropdownExpanded = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AgriGreenPrimary,
                        unfocusedBorderColor = Color(0xFFCBD5E1)
                    )
                )
                DropdownMenu(
                    expanded = cropDropdownExpanded,
                    onDismissRequest = { cropDropdownExpanded = false }
                ) {
                    cropOptions.forEach { opt ->
                        DropdownMenuItem(
                            text = { Text(opt) },
                            onClick = {
                                cropName = opt
                                cropDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quantity (kg)
            Text(text = LanguageManager.getString("quantity_kg", currentLang), fontSize = 13.sp, color = AgriTextSecondary)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = quantityKg,
                onValueChange = { quantityKg = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AgriGreenPrimary,
                    unfocusedBorderColor = Color(0xFFCBD5E1)
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Expected Price (per kg)
            Text(text = LanguageManager.getString("expected_price", currentLang), fontSize = 13.sp, color = AgriTextSecondary)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = expectedPrice,
                onValueChange = { expectedPrice = it },
                prefix = { Text("₹ ", fontWeight = FontWeight.Bold, color = AgriGreenDark) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AgriGreenPrimary,
                    unfocusedBorderColor = Color(0xFFCBD5E1)
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Available From
            Text(text = LanguageManager.getString("available_from", currentLang), fontSize = 13.sp, color = AgriTextSecondary)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = availableFrom,
                onValueChange = { availableFrom = it },
                trailingIcon = {
                    Icon(Icons.Default.CalendarToday, contentDescription = null, tint = AgriGreenPrimary)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AgriGreenPrimary,
                    unfocusedBorderColor = Color(0xFFCBD5E1)
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Quality Grade Chips
            Text(text = "Quality Grade", fontSize = 13.sp, color = AgriTextSecondary)
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                QualityGrade.values().forEach { grade ->
                    FilterChip(
                        selected = selectedGrade == grade,
                        onClick = { selectedGrade = grade },
                        label = { Text(grade.label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AgriGreenContainer,
                            selectedLabelColor = AgriGreenDark
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            // Next Button -> Leads to Price Advisor
            Button(
                onClick = {
                    val q = quantityKg.toDoubleOrNull() ?: 500.0
                    val p = expectedPrice.toDoubleOrNull() ?: 22.0
                    onProceedToPriceAdvisor(cropName, q, p, availableFrom, selectedGrade)
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

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
