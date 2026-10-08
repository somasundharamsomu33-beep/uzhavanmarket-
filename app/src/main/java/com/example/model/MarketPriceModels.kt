package com.example.model

/**
 * Standard normalized mandi market price record from agricultural government sources
 * (AGMARKNET, data.gov.in, e-NAM, State Marketing Boards)
 */
data class NormalizedMarketPrice(
    val id: String,
    val state: String,
    val district: String,
    val mandi: String,
    val commodity: String,
    val variety: String = "Local",
    val grade: String = "FAQ",
    val arrivalDate: String, // YYYY-MM-DD
    val minPrice: Double,     // in original unit (e.g. ₹/Quintal)
    val maxPrice: Double,     // in original unit
    val modalPrice: Double,   // in original unit (most representative price)
    val unit: String = "Quintal",
    val rawUnit: String = "Quintal",
    val pricePerKg: Double,   // Converted approx ₹/kg
    val source: String,       // AGMARKNET / data.gov.in / e-NAM / TN-Agri
    val sourceUrl: String = "https://agmarknet.gov.in",
    val fetchedAt: String,
    val dataTimestamp: String
)

data class MandiPriceComparison(
    val mandiName: String,
    val district: String,
    val modalPrice: Double,
    val pricePerKg: Double,
    val minPrice: Double,
    val maxPrice: Double,
    val date: String,
    val distanceKm: Double? = null,
    val estimatedTransportPerKg: Double? = null
)

data class PriceTrendPoint(
    val date: String,
    val formattedDate: String,
    val minPrice: Double,
    val modalPrice: Double,
    val maxPrice: Double,
    val pricePerKg: Double
)

enum class PriceTrendDirection(val label: String, val arrow: String) {
    UPWARD("Price increasing", "↑"),
    STABLE("Price stable", "→"),
    DOWNWARD("Price decreasing", "↓"),
    INSUFFICIENT_DATA("Not enough data to calculate trend", "•")
}

data class PriceTrendAnalysis(
    val direction: PriceTrendDirection,
    val percentageChange: Double,
    val summaryText: String
)

data class PriceAlert(
    val id: String,
    val commodity: String,
    val state: String,
    val district: String,
    val mandi: String,
    val targetPricePerKg: Double,
    val isTriggered: Boolean = false,
    val createdAt: String
)
