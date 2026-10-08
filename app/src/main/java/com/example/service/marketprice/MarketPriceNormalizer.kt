package com.example.service.marketprice

import com.example.model.NormalizedMarketPrice

object MarketPriceNormalizer {

    /**
     * Normalizes commodity names across different government spellings
     * E.g. "Tomatoes", "TOMATO", "Tomato(Local)" -> "Tomato"
     */
    fun normalizeCommodity(rawName: String): String {
        val trimmed = rawName.trim().lowercase()
        return when {
            trimmed.contains("tomato") -> "Tomato"
            trimmed.contains("onion") -> "Onion"
            trimmed.contains("banana") -> "Banana"
            trimmed.contains("brinjal") || trimmed.contains("eggplant") -> "Brinjal"
            trimmed.contains("coconut") -> "Coconut"
            trimmed.contains("rice") || trimmed.contains("paddy") -> "Paddy (Dhan)"
            trimmed.contains("groundnut") || trimmed.contains("peanut") -> "Groundnut"
            trimmed.contains("wheat") -> "Wheat"
            trimmed.contains("potato") -> "Potato"
            trimmed.contains("cotton") -> "Cotton"
            trimmed.contains("turmeric") -> "Turmeric"
            trimmed.contains("chilli") -> "Green Chilli"
            trimmed.contains("maize") || trimmed.contains("corn") -> "Maize"
            else -> rawName.trim().replaceFirstChar { it.uppercase() }
        }
    }

    /**
     * Converts Quintal, Tonne, or Kg into canonical price per kg
     * Standard: 1 Quintal = 100 kg, 1 Tonne = 1000 kg
     */
    fun calculatePricePerKg(priceValue: Double, unit: String): Double {
        return when (unit.trim().lowercase()) {
            "quintal", "qtl", "quintals" -> priceValue / 100.0
            "tonne", "ton", "tonnes" -> priceValue / 1000.0
            "kg", "kilogram" -> priceValue
            else -> priceValue / 100.0 // Government mandi standard default is Quintal
        }
    }
}
