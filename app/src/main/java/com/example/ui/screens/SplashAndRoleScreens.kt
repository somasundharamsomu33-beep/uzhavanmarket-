package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserRole
import com.example.ui.components.UzhavanBrandLogo
import com.example.ui.theme.*
import com.example.util.AppLanguage
import com.example.util.LanguageManager

/**
 * Screen 1: Splash Screen
 * Features the farmer standing overlooking fertile agricultural green fields with a smartphone,
 * Uzhavan Market logo and tagline, "Get Started", and "Already have an account? Login".
 */
@Composable
fun SplashScreen(
    currentLang: String,
    onGetStarted: () -> Unit,
    onLoginClick: () -> Unit,
    onSelectLanguage: (String) -> Unit
) {
    var showLangDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Language Selector Pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Surface(
                    onClick = { showLangDialog = true },
                    shape = RoundedCornerShape(20.dp),
                    color = AgriGreenContainer,
                    border = null
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Language",
                            tint = AgriGreenDark,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppLanguage.values().firstOrNull { it.code == currentLang }?.nativeName ?: "English",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriGreenDark
                        )
                    }
                }
            }

            // Center Branding & Hero
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Large Leaf Brand Icon
                UzhavanBrandLogo(
                    size = 56.dp,
                    showText = false
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Uzhavan\nMarket",
                    fontSize = 32.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AgriGreenDark,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Fresh Produce. Direct Buyers.\nBetter Prices.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = AgriGreenPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Beautiful Agricultural Landscape Illustration with Farmer holding smartphone
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFFE0F2FE), Color(0xFFDCFCE7), Color(0xFFBBF7D0))
                            )
                        )
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height

                        // Golden Sun
                        drawCircle(color = Color(0xFFFEF08A), radius = w * 0.16f, center = Offset(w * 0.85f, h * 0.25f))

                        // Distant Mountain/Hill Range
                        val hill1 = Path().apply {
                            moveTo(0f, h * 0.65f)
                            cubicTo(w * 0.3f, h * 0.55f, w * 0.6f, h * 0.62f, w, h * 0.52f)
                            lineTo(w, h)
                            lineTo(0f, h)
                            close()
                        }
                        drawPath(hill1, color = Color(0xFF86EFAC))

                        // Middle Paddy / Crop Field (Bright Green)
                        val hill2 = Path().apply {
                            moveTo(0f, h * 0.75f)
                            cubicTo(w * 0.4f, h * 0.68f, w * 0.7f, h * 0.72f, w, h * 0.65f)
                            lineTo(w, h)
                            lineTo(0f, h)
                            close()
                        }
                        drawPath(hill2, color = Color(0xFF22C55E))

                        // Foreground Furrows / Crops Rows
                        for (i in 0..6) {
                            val yOffset = h * 0.78f + i * 14f
                            drawLine(
                                color = Color(0xFF166534),
                                start = Offset(0f, yOffset),
                                end = Offset(w, yOffset - 18f),
                                strokeWidth = 3f
                            )
                        }

                        // Farmer silhouette in foreground (looking over farm with smartphone)
                        // Farmer Body (light cream shirt)
                        drawRoundRect(
                            color = Color(0xFFEA580C), // Turban/hat
                            topLeft = Offset(w * 0.18f, h * 0.45f),
                            size = Size(w * 0.22f, h * 0.12f),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(16f, 16f)
                        )
                        // Face
                        drawCircle(color = Color(0xFFD97706), radius = w * 0.08f, center = Offset(w * 0.26f, h * 0.58f))
                        // Farmer Torso / White Kurta
                        drawRoundRect(
                            color = Color.White,
                            topLeft = Offset(w * 0.15f, h * 0.65f),
                            size = Size(w * 0.32f, h * 0.35f),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(24f, 24f)
                        )
                        // Smartphone in hand
                        drawRoundRect(
                            color = Color(0xFF0F172A),
                            topLeft = Offset(w * 0.38f, h * 0.68f),
                            size = Size(w * 0.08f, h * 0.14f),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
                        )
                        // Smartphone green screen
                        drawRoundRect(
                            color = AgriGreenLight,
                            topLeft = Offset(w * 0.39f, h * 0.69f),
                            size = Size(w * 0.06f, h * 0.12f),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
                        )
                    }
                }
            }

            // Bottom Action Area
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Button(
                    onClick = onGetStarted,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(27.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AgriGreenDark)
                ) {
                    Text(
                        text = LanguageManager.getString("get_started", currentLang),
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

    if (showLangDialog) {
        AlertDialog(
            onDismissRequest = { showLangDialog = false },
            title = {
                Text(text = "Select Language / மொழியை தேர்ந்தெடுக்கவும்", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            },
            text = {
                Column {
                    AppLanguage.values().forEach { lang ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectLanguage(lang.code)
                                    showLangDialog = false
                                }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = lang.nativeName, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Text(text = lang.displayName, fontSize = 12.sp, color = AgriTextMuted)
                            }
                            if (lang.code == currentLang) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = AgriGreenPrimary)
                            }
                        }
                        HorizontalDivider(color = Color(0xFFF1F5F9))
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLangDialog = false }) {
                    Text("Close", color = AgriGreenDark)
                }
            }
        )
    }
}

/**
 * Screen 2: Choose Role (Farmer / Buyer / Admin)
 * Two large cards with illustrations and descriptions matching reference screen 2.
 */
@Composable
fun ChooseRoleScreen(
    currentLang: String,
    onRoleSelected: (UserRole) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Welcome to\nUzhavan Market",
                fontSize = 26.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.Bold,
                color = AgriGreenDark
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = LanguageManager.getString("choose_role", currentLang),
                fontSize = 14.sp,
                color = AgriTextSecondary
            )

            Spacer(modifier = Modifier.height(32.dp))

            // FARMER Card
            RoleSelectionCard(
                title = LanguageManager.getString("farmer_role_title", currentLang),
                description = LanguageManager.getString("farmer_role_desc", currentLang),
                isFarmer = true,
                onClick = { onRoleSelected(UserRole.FARMER) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // BUYER Card
            RoleSelectionCard(
                title = LanguageManager.getString("buyer_role_title", currentLang),
                description = LanguageManager.getString("buyer_role_desc", currentLang),
                isFarmer = false,
                onClick = { onRoleSelected(UserRole.BUYER) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Small Admin Access Option
            Card(
                onClick = { onRoleSelected(UserRole.ADMIN) },
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
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0E7FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            tint = Color(0xFF4338CA),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = LanguageManager.getString("admin_role_title", currentLang),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                        Text(
                            text = LanguageManager.getString("admin_role_desc", currentLang),
                            fontSize = 11.sp,
                            color = AgriTextMuted
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = AgriTextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Bottom Slogan
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = LanguageManager.getString("building_ecosystem", currentLang),
                fontSize = 12.sp,
                color = AgriTextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun RoleSelectionCard(
    title: String,
    description: String,
    isFarmer: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = if (isFarmer) Color(0xFFF0FDF4) else Color(0xFFFEFCE8)),
        border = androidx.compose.foundation.BorderStroke(
            1.5.dp,
            if (isFarmer) Color(0xFFBBF7D0) else Color(0xFFFEF08A)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Role Avatar / Icon Box
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(if (isFarmer) Color(0xFFDCFCE7) else Color(0xFFFEF9C3)),
                contentAlignment = Alignment.Center
            ) {
                if (isFarmer) {
                    Canvas(modifier = Modifier.size(36.dp)) {
                        val w = size.width
                        val h = size.height
                        // Hat
                        drawRoundRect(
                            color = Color(0xFFD97706),
                            topLeft = Offset(w * 0.15f, h * 0.2f),
                            size = Size(w * 0.7f, h * 0.25f),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
                        )
                        // Head
                        drawCircle(color = Color(0xFFB45309), radius = w * 0.25f, center = Offset(w * 0.5f, h * 0.5f))
                        // Green collar
                        drawCircle(color = AgriGreenPrimary, radius = w * 0.35f, center = Offset(w * 0.5f, h * 1.05f))
                    }
                } else {
                    Icon(
                        imageVector = Icons.Default.Storefront,
                        contentDescription = "Shop Buyer",
                        tint = Color(0xFFB45309),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isFarmer) AgriGreenDark else Color(0xFF713F12)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = AgriTextSecondary,
                    lineHeight = 16.sp
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = if (isFarmer) AgriGreenDark else Color(0xFFB45309),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
