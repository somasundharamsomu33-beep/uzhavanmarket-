package com.example.util

import com.example.model.QualityGrade

data class VoiceListingParsedResult(
    val crop: String,
    val quantityKg: Double,
    val unit: String = "kg",
    val expectedPricePerKg: Double,
    val availableFrom: String,
    val grade: QualityGrade = QualityGrade.GRADE_A,
    val rawText: String
)

object VoiceParser {
    /**
     * Parses natural spoken Tamil and English agri listings into structured form
     * Examples:
     * - "500 kg tomato, ready tomorrow"
     * - "500 கிலோ தக்காளி, நாளை கிடைக்கும்"
     * - "1000 kg onion ready in 2 days at 25 rupees"
     * - "300 கிலோ வெங்காயம் 18 ரூபாய்"
     */
    fun parseSpokenAgriInput(spokenText: String): VoiceListingParsedResult {
        val lower = spokenText.lowercase().trim()

        // Extract Crop
        val crop = when {
            lower.contains("tomato") || lower.contains("தக்காளி") || lower.contains("thakkali") -> "Tomato"
            lower.contains("onion") || lower.contains("வெங்காயம்") || lower.contains("vengayam") -> "Onion"
            lower.contains("banana") || lower.contains("வாழை") || lower.contains("vazhai") -> "Banana"
            lower.contains("brinjal") || lower.contains("கத்தரி") || lower.contains("kathari") -> "Brinjal"
            lower.contains("coconut") || lower.contains("தேங்காய்") || lower.contains("thengai") -> "Coconut"
            lower.contains("rice") || lower.contains("நெல்") || lower.contains("அரிசி") -> "Rice / Paddy"
            lower.contains("groundnut") || lower.contains("வேர்க்கடலை") || lower.contains("kadalai") -> "Groundnut"
            else -> "Tomato" // Default identified produce
        }

        // Extract Quantity
        // Looks for numbers before "kg", "kilo", "கிலோ", or standalone numbers
        val numbers = Regex("""\d+""").findAll(lower).map { it.value.toDoubleOrNull() ?: 0.0 }.toList()
        val quantity = if (numbers.isNotEmpty()) numbers[0] else 500.0

        // Extract Expected Price if mentioned
        val price = if (numbers.size > 1) {
            numbers[1]
        } else {
            // Recommends based on crop standard price benchmark
            when (crop) {
                "Tomato" -> 22.0
                "Onion" -> 24.0
                "Banana" -> 16.0
                "Brinjal" -> 28.0
                "Coconut" -> 32.0
                else -> 25.0
            }
        }

        // Available From
        val availableFrom = when {
            lower.contains("tomorrow") || lower.contains("நாளை") -> "Tomorrow"
            lower.contains("today") || lower.contains("இன்று") -> "Today"
            lower.contains("2 days") || lower.contains("2 நாள்") -> "In 2 days"
            else -> "Tomorrow"
        }

        return VoiceListingParsedResult(
            crop = crop,
            quantityKg = quantity,
            unit = "kg",
            expectedPricePerKg = price,
            availableFrom = availableFrom,
            grade = QualityGrade.GRADE_A,
            rawText = spokenText
        )
    }
}
