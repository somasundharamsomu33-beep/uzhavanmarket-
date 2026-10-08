package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserProfile
import com.example.model.UserRole
import com.example.ui.theme.*
import com.example.util.AppLanguage
import com.example.util.LanguageManager

/**
 * Screen 16: Profile / Settings Screen
 * Farmer Profile: Ramasamy, Farmer, Tiruvannamalai
 * Options:
 * 1. My Profile
 * 2. Farm Details
 * 3. Bank Details
 * 4. Notifications Toggle
 * 5. Language Selector (Tamil, English, Telugu, Kannada, Malayalam, Hindi)
 * 6. Help & Support
 * 7. Logout
 */
@Composable
fun ProfileScreen(
    currentLang: String,
    userProfile: UserProfile,
    onEditProfile: () -> Unit = {},
    onLanguageChange: (String) -> Unit,
    onRoleSwitch: (UserRole) -> Unit,
    onLogout: () -> Unit
) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var showLangDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AgriBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = LanguageManager.getString("profile_settings", currentLang),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = AgriGreenDark
            )

            // ✏️ Edit Profile Button
            OutlinedButton(
                onClick = onEditProfile,
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, AgriGreenPrimary),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null, tint = AgriGreenPrimary, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Edit Profile", color = AgriGreenPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Profile Avatar Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(AgriGreenContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Avatar",
                        tint = AgriGreenDark,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = userProfile.name,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgriTextPrimary
                    )
                    Text(
                        text = "${userProfile.role.name.lowercase().replaceFirstChar { it.uppercase() }} • ${userProfile.village}, ${userProfile.district}",
                        fontSize = 12.sp,
                        color = AgriTextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = userProfile.phone,
                        fontSize = 12.sp,
                        color = AgriTextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Settings Menu
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                ProfileMenuItem(
                    icon = Icons.Default.AccountCircle,
                    title = "My Profile (Edit Name & Phone)",
                    onClick = onEditProfile
                )

                HorizontalDivider(color = Color(0xFFF1F5F9))

                ProfileMenuItem(
                    icon = Icons.Default.Agriculture,
                    title = "Farm Details (Edit Acreage & Crops)",
                    onClick = onEditProfile
                )

                HorizontalDivider(color = Color(0xFFF1F5F9))

                ProfileMenuItem(
                    icon = Icons.Default.AccountBalance,
                    title = "Bank Details for Direct Payout",
                    onClick = {}
                )

                HorizontalDivider(color = Color(0xFFF1F5F9))

                // Notifications Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = AgriGreenPrimary, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(text = "Notifications", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = AgriTextPrimary)
                    }
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = AgriGreenPrimary)
                    )
                }

                HorizontalDivider(color = Color(0xFFF1F5F9))

                // Language Selection Item
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showLangDialog = true }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Language, contentDescription = null, tint = AgriGreenPrimary, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(text = LanguageManager.getString("language", currentLang), fontSize = 14.sp, fontWeight = FontWeight.Medium, color = AgriTextPrimary)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = AppLanguage.values().firstOrNull { it.code == currentLang }?.nativeName ?: "English",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriGreenDark
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = AgriTextMuted, modifier = Modifier.size(16.dp))
                    }
                }

                HorizontalDivider(color = Color(0xFFF1F5F9))

                // Switch Role Item
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val newRole = if (userProfile.role == UserRole.FARMER) UserRole.BUYER else UserRole.FARMER
                            onRoleSwitch(newRole)
                        }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.SwapHoriz, contentDescription = null, tint = Color(0xFF0284C7), modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(
                            text = if (userProfile.role == UserRole.FARMER) "Switch to Buyer Mode" else "Switch to Farmer Mode",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = AgriTextPrimary
                        )
                    }
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = AgriTextMuted, modifier = Modifier.size(16.dp))
                }

                HorizontalDivider(color = Color(0xFFF1F5F9))

                ProfileMenuItem(
                    icon = Icons.Default.HelpOutline,
                    title = "Help & Support",
                    onClick = {}
                )

                HorizontalDivider(color = Color(0xFFF1F5F9))

                // Logout Item
                ProfileMenuItem(
                    icon = Icons.AutoMirrored.Filled.Logout,
                    title = LanguageManager.getString("logout", currentLang),
                    titleColor = Color(0xFFDC2626),
                    iconColor = Color(0xFFDC2626),
                    onClick = onLogout
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    if (showLangDialog) {
        AlertDialog(
            onDismissRequest = { showLangDialog = false },
            title = {
                Text(text = "Choose Language / மொழி", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            },
            text = {
                Column {
                    AppLanguage.values().forEach { lang ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onLanguageChange(lang.code)
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
                    Text("Done", color = AgriGreenDark)
                }
            }
        )
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    titleColor: Color = AgriTextPrimary,
    iconColor: Color = AgriGreenPrimary,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(14.dp))
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = titleColor)
        }
        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = AgriTextMuted, modifier = Modifier.size(16.dp))
    }
}
