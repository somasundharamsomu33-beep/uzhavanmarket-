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
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import com.example.ui.components.AgriLandscapeHero
import com.example.ui.theme.*
import com.example.util.LanguageManager

/**
 * Screen 4: Farmer Home Dashboard
 * Top Greeting: "Good Morning, Ramasamy 👋", avatar
 * Hero banner: "Better Price for Your Produce - List now →"
 * 4 Big Action Cards: List Produce, My Listings, Orders, Payments
 * Recent Activity: "Your tomato listing is live (2 hours ago)"
 * Farmer Bottom Navigation: Home, Listings, Orders, Profile
 */
@Composable
fun FarmerDashboardScreen(
    currentLang: String,
    userProfile: UserProfile,
    onNavigateToListProduce: () -> Unit,
    onNavigateToMarketPrices: () -> Unit,
    onNavigateToListings: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToPayments: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onProfileClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AgriBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Top Header: Greeting + Avatar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${LanguageManager.getString("good_morning", currentLang)},",
                        fontSize = 14.sp,
                        color = AgriTextSecondary
                    )
                }
                Text(
                    text = "${userProfile.name} 👋",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgriGreenDark
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onNavigateToNotifications) {
                    BadgedBox(
                        badge = {
                            Badge(containerColor = Color(0xFFEF4444)) {
                                Text("2", fontSize = 10.sp, color = Color.White)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                            tint = AgriGreenDark
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(AgriGreenContainer)
                        .clickable { onProfileClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = AgriGreenDark,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Hero Card: "Better Price for Your Produce"
        AgriLandscapeHero(
            farmerName = userProfile.name,
            onListClick = onNavigateToListProduce
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 4 Large Touch-Friendly Primary Action Cards
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            FarmerActionGridItem(
                title = LanguageManager.getString("list_produce", currentLang),
                icon = Icons.Default.AddCircleOutline,
                iconColor = AgriGreenPrimary,
                bgColor = Color(0xFFF0FDF4),
                modifier = Modifier.weight(1f),
                onClick = onNavigateToListProduce
            )

            FarmerActionGridItem(
                title = "Today's Price",
                icon = Icons.Default.TrendingUp,
                iconColor = Color(0xFF0284C7),
                bgColor = Color(0xFFF0F9FF),
                modifier = Modifier.weight(1f),
                onClick = onNavigateToMarketPrices
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            FarmerActionGridItem(
                title = LanguageManager.getString("orders", currentLang),
                icon = Icons.Default.AssignmentTurnedIn,
                iconColor = Color(0xFFEA580C),
                bgColor = Color(0xFFFFF7ED),
                modifier = Modifier.weight(1f),
                onClick = onNavigateToOrders
            )

            FarmerActionGridItem(
                title = LanguageManager.getString("payments", currentLang),
                icon = Icons.Default.AccountBalanceWallet,
                iconColor = Color(0xFF16A34A),
                bgColor = Color(0xFFF0FDF4),
                modifier = Modifier.weight(1f),
                onClick = onNavigateToPayments
            )
        }

        Spacer(modifier = Modifier.height(26.dp))

        // Recent Activity Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = LanguageManager.getString("recent_activity", currentLang),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = AgriGreenDark
            )
            Text(
                text = "View All",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = AgriGreenPrimary,
                modifier = Modifier.clickable { onNavigateToOrders() }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Activity 1
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFDCFCE7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AgriGreenPrimary, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Your tomato listing is live",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AgriTextPrimary
                        )
                        Text(
                            text = "2 hours ago • 500 kg @ ₹18/kg",
                            fontSize = 12.sp,
                            color = AgriTextMuted
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF1F5F9))

                // Activity 2
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFEF3C7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Payment received: ₹2,040",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AgriTextPrimary
                        )
                        Text(
                            text = "From Hotel Sunrise • Net into Bank",
                            fontSize = 12.sp,
                            color = AgriTextMuted
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun FarmerActionGridItem(
    title: String,
    icon: ImageVector,
    iconColor: Color,
    bgColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier.height(110.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = AgriGreenDark
            )
        }
    }
}
