package com.example.service.marketprice

import com.example.model.NormalizedMarketPrice

data class MarketPriceQuery(
    val state: String,
    val district: String,
    val mandi: String,
    val commodity: String,
    val variety: String = "All",
    val date: String = "latest" // YYYY-MM-DD or "latest"
)

/**
 * Common interface for agricultural market price data providers
 * Allows swapping AGMARKNET, data.gov.in, e-NAM, or State Marketing Boards
 */
interface MarketPriceProvider {
    val providerName: String
    val priority: Int

    suspend fun isAvailable(): Boolean

    suspend fun getAvailableStates(): List<String>

    suspend fun getDistrictsForState(state: String): List<String>

    suspend fun getMandisForDistrict(state: String, district: String): List<String>

    suspend fun getCommoditiesForMandi(state: String, district: String, mandi: String): List<String>

    suspend fun fetchMarketPrices(query: MarketPriceQuery): List<NormalizedMarketPrice>
}
