package com.example.service.marketprice

import com.example.model.NormalizedMarketPrice
import java.util.concurrent.ConcurrentHashMap

/**
 * In-memory client cache with TTL to avoid redundant requests to upstream data sources.
 * Adheres to cache TTL guidelines and stores last fetch timestamps.
 */
object MarketPriceCache {
    private val cache = ConcurrentHashMap<String, CacheEntry>()
    private const val DEFAULT_TTL_MILLIS = 30 * 60 * 1000L // 30 minutes cache TTL

    data class CacheEntry(
        val data: List<NormalizedMarketPrice>,
        val cachedAt: Long
    )

    fun makeKey(query: MarketPriceQuery): String {
        return "${query.state}_${query.district}_${query.mandi}_${query.commodity}_${query.variety}_${query.date}".lowercase()
    }

    fun get(key: String): List<NormalizedMarketPrice>? {
        val entry = cache[key] ?: return null
        val now = System.currentTimeMillis()
        if (now - entry.cachedAt > DEFAULT_TTL_MILLIS) {
            cache.remove(key)
            return null
        }
        return entry.data
    }

    fun put(key: String, data: List<NormalizedMarketPrice>) {
        cache[key] = CacheEntry(data, System.currentTimeMillis())
    }

    fun clear() {
        cache.clear()
    }
}
