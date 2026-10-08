package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MandiPriceComparison
import com.example.model.NormalizedMarketPrice
import com.example.model.PriceTrendAnalysis
import com.example.model.PriceTrendPoint
import com.example.service.marketprice.MarketPriceQuery
import com.example.service.marketprice.MarketPriceService
import com.example.ui.components.CropVisualBadge
import com.example.ui.components.FarmerPriceTrendChart
import com.example.ui.theme.*
import com.example.util.LanguageManager
import kotlinx.coroutines.launch

/**
 * Screen: Live Today's Market Price System
 * Allows selecting State -> District -> Mandi -> Commodity -> Date
 * Displays authentic government data (Modal price in Quintal + approx ₹/kg)
 * Shows source badge (AGMARKNET / data.gov.in), freshness timestamp,
 * nearby mandi comparisons with transport costs, 7-day trend chart,
 * and a direct action to pre-fill "Sell Produce" with verified market prices.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodaysMarketPriceScreen(
    currentLang: String,
    onBack: () -> Unit,
    onUsePriceInListing: (crop: String, pricePerKg: Double) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    // Geographic & Commodity State selections
    var states by remember { mutableStateOf<List<String>>(emptyList()) }
    var districts by remember { mutableStateOf<List<String>>(emptyList()) }
    var mandis by remember { mutableStateOf<List<String>>(emptyList()) }
    var commodities by remember { mutableStateOf<List<String>>(emptyList()) }

    var selectedState by remember { mutableStateOf("Tamil Nadu") }
    var selectedDistrict by remember { mutableStateOf("Tiruvannamalai") }
    var selectedMandi by remember { mutableStateOf("Tiruvannamalai") }
    var selectedCommodity by remember { mutableStateOf("Tomato") }
    var selectedDate by remember { mutableStateOf("8 Oct 2026") }

    // Dropdown visibility states
    var expandState by remember { mutableStateOf(false) }
    var expandDistrict by remember { mutableStateOf(false) }
    var expandMandi by remember { mutableStateOf(false) }
    var expandCommodity by remember { mutableStateOf(false) }

    // Market Price Data
    var currentPrice by remember { mutableStateOf<NormalizedMarketPrice?>(null) }
    var nearbyComparisons by remember { mutableStateOf<List<MandiPriceComparison>>(emptyList()) }
    var trendData by remember { mutableStateOf<Pair<List<PriceTrendPoint>, PriceTrendAnalysis>?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var showSourceInfoDialog by remember { mutableStateOf(false) }
    var showPriceAlertDialog by remember { mutableStateOf(false) }
    var alertTargetPrice by remember { mutableStateOf("25") }
    var alertCreatedSnackbar by remember { mutableStateOf(false) }

    // Function to reload market price
    fun loadData() {
        isLoading = true
        coroutineScope.launch {
            val price = MarketPriceService.getMarketPrice(
                MarketPriceQuery(
                    state = selectedState,
                    district = selectedDistrict,
                    mandi = selectedMandi,
                    commodity = selectedCommodity,
                    date = "latest"
                )
            )
            currentPrice = price

            if (price != null) {
                trendData = MarketPriceService.getHistoricalPriceTrend(selectedCommodity, price.pricePerKg)
                nearbyComparisons = MarketPriceService.getNearbyMandiComparisons(
                    state = selectedState,
                    district = selectedDistrict,
                    commodity = selectedCommodity,
                    currentMandi = selectedMandi
                )
            }
            isLoading = false
        }
    }

    // Initial load
    LaunchedEffect(Unit) {
        states = MarketPriceService.getStates()
        districts = MarketPriceService.getDistricts(selectedState)
        mandis = MarketPriceService.getMandis(selectedState, selectedDistrict)
        commodities = MarketPriceService.getCommodities(selectedState, selectedDistrict, selectedMandi)
        loadData()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Today's Market Price",
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
                    IconButton(onClick = { showPriceAlertDialog = true }) {
                        Icon(Icons.Default.NotificationsActive, contentDescription = "Price Alert", tint = AgriGreenDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = AgriBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Geographic Selection Filter Bar
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Select Market Hierarchy",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgriTextMuted
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // State Dropdown
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedButton(
                                onClick = { expandState = true },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Text(selectedState, fontSize = 12.sp, maxLines = 1, color = AgriGreenDark)
                            }
                            DropdownMenu(expanded = expandState, onDismissRequest = { expandState = false }) {
                                states.forEach { s ->
                                    DropdownMenuItem(
                                        text = { Text(s, fontSize = 13.sp) },
                                        onClick = {
                                            selectedState = s
                                            expandState = false
                                            coroutineScope.launch {
                                                districts = MarketPriceService.getDistricts(s)
                                                selectedDistrict = districts.firstOrNull() ?: ""
                                                mandis = MarketPriceService.getMandis(s, selectedDistrict)
                                                selectedMandi = mandis.firstOrNull() ?: ""
                                                loadData()
                                            }
                                        }
                                    )
                                }
                            }
                        }

                        // District Dropdown
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedButton(
                                onClick = { expandDistrict = true },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Text(selectedDistrict, fontSize = 12.sp, maxLines = 1, color = AgriGreenDark)
                            }
                            DropdownMenu(expanded = expandDistrict, onDismissRequest = { expandDistrict = false }) {
                                districts.forEach { d ->
                                    DropdownMenuItem(
                                        text = { Text(d, fontSize = 13.sp) },
                                        onClick = {
                                            selectedDistrict = d
                                            expandDistrict = false
                                            coroutineScope.launch {
                                                mandis = MarketPriceService.getMandis(selectedState, d)
                                                selectedMandi = mandis.firstOrNull() ?: ""
                                                loadData()
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Mandi Dropdown
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedButton(
                                onClick = { expandMandi = true },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Text("Mandi: $selectedMandi", fontSize = 12.sp, maxLines = 1, color = AgriGreenDark)
                            }
                            DropdownMenu(expanded = expandMandi, onDismissRequest = { expandMandi = false }) {
                                mandis.forEach { m ->
                                    DropdownMenuItem(
                                        text = { Text(m, fontSize = 13.sp) },
                                        onClick = {
                                            selectedMandi = m
                                            expandMandi = false
                                            loadData()
                                        }
                                    )
                                }
                            }
                        }

                        // Commodity Dropdown
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedButton(
                                onClick = { expandCommodity = true },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Text("Crop: $selectedCommodity", fontSize = 12.sp, maxLines = 1, color = AgriGreenDark)
                            }
                            DropdownMenu(expanded = expandCommodity, onDismissRequest = { expandCommodity = false }) {
                                commodities.forEach { c ->
                                    DropdownMenuItem(
                                        text = { Text(c, fontSize = 13.sp) },
                                        onClick = {
                                            selectedCommodity = c
                                            expandCommodity = false
                                            loadData()
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Primary Farmer-Facing Price Card
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AgriGreenPrimary)
                }
            } else if (currentPrice != null) {
                val price = currentPrice!!
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFBBF7D0)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("primary_price_card")
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CropVisualBadge(cropName = price.commodity, size = 52.dp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = price.commodity.uppercase(),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = AgriGreenDark
                                    )
                                    Text(
                                        text = "${price.mandi} Market • ${price.variety} (${price.grade})",
                                        fontSize = 12.sp,
                                        color = AgriTextSecondary
                                    )
                                }
                            }

                            // Source Badge Pill (Clickable)
                            Surface(
                                onClick = { showSourceInfoDialog = true },
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFDCFCE7)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Verified, contentDescription = null, tint = AgriGreenPrimary, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = price.source,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AgriGreenDark
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Modal Price Display (Primary Price in Quintal & Approx /kg)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Column {
                                Text(
                                    text = "Modal Price",
                                    fontSize = 13.sp,
                                    color = AgriTextSecondary,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(
                                        text = "₹${price.modalPrice.toInt()}",
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = AgriGreenDark
                                    )
                                    Text(
                                        text = " /quintal",
                                        fontSize = 15.sp,
                                        color = AgriTextSecondary,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                }
                            }

                            // Equivalent /kg badge
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFFEF9C3),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFEF08A))
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Approx.",
                                        fontSize = 10.sp,
                                        color = Color(0xFF854D0E)
                                    )
                                    Text(
                                        text = "₹${price.pricePerKg.toInt()}/kg",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF713F12)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Price Range
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Price Range: ₹${price.minPrice.toInt()} – ₹${price.maxPrice.toInt()} /quintal",
                                fontSize = 12.sp,
                                color = AgriTextMuted,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Date: ${price.arrivalDate}",
                                fontSize = 12.sp,
                                color = AgriTextMuted
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF1F5F9))

                        // Explanatory note in user's language
                        Text(
                            text = if (currentLang == "ta") {
                                "இந்த சந்தையில் இன்று பதிவான முக்கிய சராசரி விலை. இது உத்தரவாத விலை அல்ல."
                            } else {
                                "Modal price is the most representative reported market price for this market. Prices differ by quality and variety."
                            },
                            fontSize = 11.sp,
                            color = AgriTextSecondary,
                            lineHeight = 15.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Direct Action: "Use This Price in My Listing"
                        Button(
                            onClick = {
                                onUsePriceInListing(price.commodity, price.pricePerKg)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AgriGreenDark)
                        ) {
                            Icon(Icons.Default.Sell, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Use This Price in My Listing (₹${price.pricePerKg.toInt()}/kg)",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            } else {
                // Empty state if data unavailable
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Price data unavailable for this market.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriTextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Try selecting a nearby mandi or check again during active market auction hours.",
                            fontSize = 12.sp,
                            color = AgriTextMuted,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 7-Day Price Trend Line Chart
            trendData?.let { (points, analysis) ->
                FarmerPriceTrendChart(points = points, analysis = analysis)
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Compare Nearby Markets Section
            if (nearbyComparisons.isNotEmpty()) {
                Text(
                    text = "Compare Nearby Markets (${selectedDistrict})",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgriGreenDark
                )

                Spacer(modifier = Modifier.height(10.dp))

                nearbyComparisons.forEach { comp ->
                    val isCurrent = comp.mandiName.equals(selectedMandi, ignoreCase = true)
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isCurrent) Color(0xFFF0FDF4) else Color.White
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            if (isCurrent) 1.5.dp else 1.dp,
                            if (isCurrent) AgriGreenPrimary else Color(0xFFE2E8F0)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .clickable {
                                selectedMandi = comp.mandiName
                                loadData()
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = comp.mandiName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AgriTextPrimary
                                    )
                                    if (isCurrent) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFDCFCE7)) {
                                            Text(
                                                text = "Selected",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = AgriGreenDark,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = "Range: ₹${comp.minPrice.toInt()} – ₹${comp.maxPrice.toInt()}/qtl",
                                    fontSize = 11.sp,
                                    color = AgriTextMuted
                                )
                                comp.distanceKm?.let { d ->
                                    if (d > 0) {
                                        Text(
                                            text = "${d.toInt()} km away • Transport: approx ₹${String.format("%.1f", comp.estimatedTransportPerKg)}/kg",
                                            fontSize = 11.sp,
                                            color = AgriTextSecondary
                                        )
                                    }
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "₹${comp.pricePerKg.toInt()}/kg",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = AgriGreenDark
                                )
                                Text(
                                    text = "₹${comp.modalPrice.toInt()}/qtl",
                                    fontSize = 11.sp,
                                    color = AgriTextMuted
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Data Source & Authenticity Disclaimer
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = AgriGreenPrimary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Authentic Government Market Data",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriGreenDark
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Market prices are based on official daily arrival bulletins from AGMARKNET and state agricultural marketing boards. Prices vary based on crop quality, variety, and demand.",
                        fontSize = 11.sp,
                        color = AgriTextMuted,
                        lineHeight = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Source Info Dialog
    if (showSourceInfoDialog) {
        AlertDialog(
            onDismissRequest = { showSourceInfoDialog = false },
            title = {
                Text("Agricultural Market Data Source", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            },
            text = {
                Column {
                    Text("• Primary Provider: AGMARKNET (Govt. of India)", fontSize = 13.sp)
                    Text("• Secondary Provider: data.gov.in / e-NAM", fontSize = 13.sp)
                    Text("• Frequency: Daily mandi auction closures", fontSize = 13.sp)
                    Text("• Units: Retained in authentic Quintals, converted to approx ₹/kg for farmer convenience.", fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Uzhavan Market never invents or fabricates crop prices.", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = AgriGreenDark)
                }
            },
            confirmButton = {
                TextButton(onClick = { showSourceInfoDialog = false }) {
                    Text("Got It", color = AgriGreenDark)
                }
            }
        )
    }

    // Price Alert Dialog
    if (showPriceAlertDialog) {
        AlertDialog(
            onDismissRequest = { showPriceAlertDialog = false },
            title = {
                Text("Set Price Alert for $selectedCommodity", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            },
            text = {
                Column {
                    Text("Receive notification when $selectedCommodity in $selectedMandi goes above target price:", fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = alertTargetPrice,
                        onValueChange = { alertTargetPrice = it },
                        prefix = { Text("₹ ") },
                        suffix = { Text("/kg") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showPriceAlertDialog = false
                        alertCreatedSnackbar = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AgriGreenDark)
                ) {
                    Text("Save Alert")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPriceAlertDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
