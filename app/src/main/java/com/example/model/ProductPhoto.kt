package com.example.model

enum class ImageSourceType(val label: String) {
    FARMER_UPLOAD("Farmer Real Photo"),
    AI_REPRESENTATIVE("Representative Image"),
    ADMIN_APPROVED("Verified Photo")
}

data class ProductPhoto(
    val id: String,
    val caption: String = "",
    val sourceType: ImageSourceType = ImageSourceType.FARMER_UPLOAD,
    val isPrimary: Boolean = true
)
