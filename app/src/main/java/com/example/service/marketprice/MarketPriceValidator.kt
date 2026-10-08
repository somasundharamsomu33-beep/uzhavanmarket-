package com.example.service.marketprice

import com.example.model.NormalizedMarketPrice

object MarketPriceValidator {

    data class ValidationResult(
        val isValid: Boolean,
        val reason: String? = null
    )

    /**
     * Rejects invalid or corrupt records before storing/displaying
     * Rule: Minimum <= Modal <= Maximum, numeric > 0, required fields present
     */
    fun validate(price: NormalizedMarketPrice): ValidationResult {
        if (price.commodity.isBlank()) return ValidationResult(false, "Commodity name cannot be blank")
        if (price.state.isBlank()) return ValidationResult(false, "State cannot be blank")
        if (price.district.isBlank()) return ValidationResult(false, "District cannot be blank")
        if (price.mandi.isBlank()) return ValidationResult(false, "Mandi cannot be blank")

        if (price.minPrice <= 0 || price.maxPrice <= 0 || price.modalPrice <= 0) {
            return ValidationResult(false, "Price values must be positive numbers")
        }

        if (price.minPrice > price.modalPrice || price.modalPrice > price.maxPrice) {
            return ValidationResult(
                false,
                "Inconsistent prices: min (${price.minPrice}) must be <= modal (${price.modalPrice}) <= max (${price.maxPrice})"
            )
        }

        if (price.arrivalDate.isBlank()) {
            return ValidationResult(false, "Arrival date cannot be blank")
        }

        return ValidationResult(true)
    }
}
