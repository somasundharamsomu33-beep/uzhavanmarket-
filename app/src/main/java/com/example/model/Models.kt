package com.example.model

enum class UserRole {
    FARMER,
    BUYER,
    ADMIN
}

enum class QualityGrade(val label: String) {
    GRADE_A("Grade A"),
    GRADE_B("Grade B"),
    GRADE_C("Grade C")
}

enum class OrderStatus(val display: String) {
    CONFIRMED("Order Confirmed"),
    PICKUP_SCHEDULED("Pickup Scheduled"),
    OUT_FOR_DELIVERY("Out for Delivery"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled")
}

data class UserProfile(
    val id: String,
    val name: String,
    val phone: String,
    val role: UserRole,
    val district: String = "Tiruvannamalai",
    val village: String = "Vellore",
    val farmSizeAcres: Double = 5.0,
    val mainCrops: String = "Tomato, Paddy, Groundnut",
    val preferredLanguage: String = "ta",
    val verified: Boolean = true,
    val rating: Double = 4.8,
    val reviewCount: Int = 12
)

data class ProduceItem(
    val id: String,
    val nameEn: String,
    val nameTa: String,
    val category: String, // Vegetables, Fruits, Grains, Others
    val pricePerKg: Double,
    val marketBenchmarkPrice: Double, // Benchmark from Agmarknet / e-NAM
    val availableKg: Double,
    val grade: QualityGrade,
    val farmerId: String,
    val farmerName: String,
    val village: String,
    val district: String,
    val harvestDate: String,
    val availableFrom: String,
    val description: String,
    val isOrganic: Boolean = true,
    val imageType: ImageSourceType = ImageSourceType.FARMER_UPLOAD,
    val isAiRepresentative: Boolean = false,
    val galleryImages: List<String> = emptyList()
)

data class CartItem(
    val produce: ProduceItem,
    val quantityKg: Double
)

data class OrderRecord(
    val orderId: String,
    val buyerName: String,
    val deliveryAddress: String,
    val items: List<CartItem>,
    val subtotal: Double,
    val deliveryFee: Double,
    val platformFee: Double,
    val totalAmount: Double,
    val paymentMethod: String,
    val date: String,
    val status: OrderStatus,
    val trackingSteps: List<TrackingStep>
)

data class TrackingStep(
    val title: String,
    val timeDescription: String,
    val isCompleted: Boolean
)

data class MarketPriceInfo(
    val cropName: String,
    val location: String,
    val currentBenchmark: Double,
    val minPrice: Double,
    val maxPrice: Double,
    val recommendedPrice: Double,
    val trend: String = "Upward (+4% this week)"
)

data class FarmerSaleComparison(
    val crop: String,
    val quantityKg: Double,
    val usualMarketRate: Double,
    val uzhavanRate: Double,
    val transportCost: Double,
    val platformFee: Double,
    val netEarnings: Double,
    val extraIncome: Double,
    val percentIncrease: Double
)

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timeAgo: String,
    val type: String // orders, payments, offers, price
)
