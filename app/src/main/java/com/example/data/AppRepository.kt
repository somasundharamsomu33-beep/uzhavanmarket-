package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

object AppRepository {

    // Current User Profile
    private val _currentUser = MutableStateFlow(
        UserProfile(
            id = "farmer_ramasamy_01",
            name = "Ramasamy",
            phone = "+91 98765 43210",
            role = UserRole.FARMER,
            district = "Tiruvannamalai",
            village = "Vellore",
            farmSizeAcres = 4.5,
            preferredLanguage = "en",
            verified = true,
            rating = 4.8,
            reviewCount = 12
        )
    )
    val currentUser: StateFlow<UserProfile> = _currentUser.asStateFlow()

    // Current App Language
    private val _currentLanguage = MutableStateFlow("en")
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    // Produce Listings
    private val _listings = MutableStateFlow<List<ProduceItem>>(getInitialProduce())
    val listings: StateFlow<List<ProduceItem>> = _listings.asStateFlow()

    // Cart Items (for Buyer)
    private val _cart = MutableStateFlow<List<CartItem>>(
        listOf(
            CartItem(
                produce = getInitialProduce()[0], // Tomato 100kg
                quantityKg = 100.0
            )
        )
    )
    val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

    // Orders List
    private val _orders = MutableStateFlow<List<OrderRecord>>(getInitialOrders())
    val orders: StateFlow<List<OrderRecord>> = _orders.asStateFlow()

    // Farmer Income History (Comparison vs local mandi)
    private val _farmerIncomeList = MutableStateFlow<List<FarmerSaleComparison>>(getInitialIncomeComparisons())
    val farmerIncomeList: StateFlow<List<FarmerSaleComparison>> = _farmerIncomeList.asStateFlow()

    // Notifications
    private val _notifications = MutableStateFlow<List<NotificationItem>>(getInitialNotifications())
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    // Functions
    fun setLanguage(lang: String) {
        _currentLanguage.value = lang
    }

    fun setUserRole(role: UserRole) {
        _currentUser.value = _currentUser.value.copy(role = role)
    }

    fun updateUserProfile(name: String, phone: String, district: String, village: String, role: UserRole) {
        _currentUser.value = _currentUser.value.copy(
            name = name,
            phone = phone,
            district = district,
            village = village,
            role = role
        )
    }

    fun updateFullProfile(
        name: String,
        phone: String,
        district: String,
        village: String,
        farmSize: Double,
        mainCrops: String
    ) {
        _currentUser.value = _currentUser.value.copy(
            name = name,
            phone = phone,
            district = district,
            village = village,
            farmSizeAcres = farmSize,
            mainCrops = mainCrops
        )
    }

    fun updateProduceListing(updatedItem: ProduceItem) {
        val current = _listings.value.toMutableList()
        val index = current.indexOfFirst { it.id == updatedItem.id }
        if (index >= 0) {
            current[index] = updatedItem
            _listings.value = current
        }
    }

    fun addProduceListing(
        crop: String,
        quantityKg: Double,
        pricePerKg: Double,
        availableFrom: String,
        grade: QualityGrade,
        description: String
    ): ProduceItem {
        val user = _currentUser.value
        val newItem = ProduceItem(
            id = "listing_${UUID.randomUUID().toString().take(6)}",
            nameEn = crop,
            nameTa = when (crop.lowercase()) {
                "tomato" -> "தக்காளி"
                "onion" -> "வெங்காயம்"
                "banana" -> "வாழை"
                "brinjal" -> "கத்தரி"
                "coconut" -> "தேங்காய்"
                else -> crop
            },
            category = when (crop.lowercase()) {
                "banana" -> "Fruits"
                "coconut", "groundnut", "rice" -> "Grains"
                else -> "Vegetables"
            },
            pricePerKg = pricePerKg,
            marketBenchmarkPrice = pricePerKg * 0.9, // Benchmark Agmarknet mandi price
            availableKg = quantityKg,
            grade = grade,
            farmerId = user.id,
            farmerName = user.name,
            village = user.village,
            district = user.district,
            harvestDate = "Today",
            availableFrom = availableFrom,
            description = if (description.isNotBlank()) description else "Fresh and chemical-free $crop harvested locally.",
            isOrganic = true
        )
        _listings.value = listOf(newItem) + _listings.value

        // Also add notification for farmer
        _notifications.value = listOf(
            NotificationItem(
                id = UUID.randomUUID().toString(),
                title = "Your $crop listing is live",
                message = "${quantityKg.toInt()} kg $crop published at ₹${pricePerKg.toInt()}/kg for buyers.",
                timeAgo = "Just now",
                type = "orders"
            )
        ) + _notifications.value

        return newItem
    }

    fun addToCart(produce: ProduceItem, quantityKg: Double) {
        val current = _cart.value.toMutableList()
        val index = current.indexOfFirst { it.produce.id == produce.id }
        if (index >= 0) {
            val existing = current[index]
            current[index] = existing.copy(quantityKg = existing.quantityKg + quantityKg)
        } else {
            current.add(CartItem(produce, quantityKg))
        }
        _cart.value = current
    }

    fun updateCartQuantity(produceId: String, delta: Double) {
        val current = _cart.value.toMutableList()
        val index = current.indexOfFirst { it.produce.id == produceId }
        if (index >= 0) {
            val existing = current[index]
            val newQty = (existing.quantityKg + delta).coerceAtLeast(0.0)
            if (newQty <= 0.0) {
                current.removeAt(index)
            } else {
                current[index] = existing.copy(quantityKg = newQty)
            }
            _cart.value = current
        }
    }

    fun clearCart() {
        _cart.value = emptyList()
    }

    fun placeOrder(paymentMethod: String): OrderRecord {
        val items = _cart.value
        val subtotal = items.sumOf { it.produce.pricePerKg * it.quantityKg }
        val deliveryFee = 150.0
        val platformFee = subtotal * 0.05
        val total = subtotal + deliveryFee + platformFee
        val orderId = "UM${(10000..99999).random()}"

        val order = OrderRecord(
            orderId = orderId,
            buyerName = "Hotel Sunrise",
            deliveryAddress = "123, Main Road, Tiruvannamalai - 606601",
            items = items,
            subtotal = subtotal,
            deliveryFee = deliveryFee,
            platformFee = platformFee,
            totalAmount = total,
            paymentMethod = paymentMethod,
            date = "12 Apr 2025, 10:15 AM",
            status = OrderStatus.CONFIRMED,
            trackingSteps = listOf(
                TrackingStep("Order Confirmed", "10:20 AM", true),
                TrackingStep("Pickup Scheduled", "Today, 2:00 PM", true),
                TrackingStep("Out for Delivery", "Today, 4:00 PM", false),
                TrackingStep("Delivered", "Expected by 6:00 PM", false)
            )
        )

        _orders.value = listOf(order) + _orders.value
        clearCart()

        // Also add comparison sale to farmer income
        val firstItem = items.firstOrNull()
        if (firstItem != null) {
            val usualRate = firstItem.produce.marketBenchmarkPrice
            val uzhavanRate = firstItem.produce.pricePerKg
            val extraPerKg = uzhavanRate - usualRate
            val extraTotal = extraPerKg * firstItem.quantityKg
            val comp = FarmerSaleComparison(
                crop = firstItem.produce.nameEn,
                quantityKg = firstItem.quantityKg,
                usualMarketRate = usualRate,
                uzhavanRate = uzhavanRate,
                transportCost = 0.0,
                platformFee = firstItem.quantityKg * uzhavanRate * 0.03,
                netEarnings = (firstItem.quantityKg * uzhavanRate) * 0.97,
                extraIncome = extraTotal,
                percentIncrease = (extraPerKg / usualRate) * 100
            )
            _farmerIncomeList.value = listOf(comp) + _farmerIncomeList.value
        }

        // Add notification
        _notifications.value = listOf(
            NotificationItem(
                id = UUID.randomUUID().toString(),
                title = "Payment received",
                message = "₹${total.toInt()} for Order #$orderId received via $paymentMethod.",
                timeAgo = "Just now",
                type = "payments"
            ),
            NotificationItem(
                id = UUID.randomUUID().toString(),
                title = "Order #$orderId confirmed",
                message = "Hotel Sunrise ordered fresh produce. Pickup scheduled for 2:00 PM.",
                timeAgo = "Just now",
                type = "orders"
            )
        ) + _notifications.value

        return order
    }

    // Benchmark Agri Market Price Data for Advisor
    fun getMarketPriceAdvisor(cropName: String, location: String): MarketPriceInfo {
        return when (cropName.lowercase()) {
            "tomato" -> MarketPriceInfo(
                cropName = "Tomato",
                location = location,
                currentBenchmark = 20.0,
                minPrice = 18.0,
                maxPrice = 24.0,
                recommendedPrice = 22.0,
                trend = "High demand in Tiruvannamalai mandi"
            )
            "onion" -> MarketPriceInfo(
                cropName = "Onion",
                location = location,
                currentBenchmark = 22.0,
                minPrice = 20.0,
                maxPrice = 26.0,
                recommendedPrice = 24.0,
                trend = "Stable pricing across state markets"
            )
            "banana" -> MarketPriceInfo(
                cropName = "Banana",
                location = location,
                currentBenchmark = 14.0,
                minPrice = 12.0,
                maxPrice = 18.0,
                recommendedPrice = 16.0,
                trend = "Festive demand boosting prices"
            )
            "brinjal" -> MarketPriceInfo(
                cropName = "Brinjal",
                location = location,
                currentBenchmark = 25.0,
                minPrice = 22.0,
                maxPrice = 30.0,
                recommendedPrice = 28.0,
                trend = "Moderate supplies available"
            )
            else -> MarketPriceInfo(
                cropName = cropName,
                location = location,
                currentBenchmark = 20.0,
                minPrice = 18.0,
                maxPrice = 24.0,
                recommendedPrice = 22.0,
                trend = "Agmarknet average rate"
            )
        }
    }

    private fun getInitialProduce(): List<ProduceItem> {
        return listOf(
            ProduceItem(
                id = "p_tomato_01",
                nameEn = "Tomato",
                nameTa = "தக்காளி",
                category = "Vegetables",
                pricePerKg = 18.0,
                marketBenchmarkPrice = 16.0,
                availableKg = 500.0,
                grade = QualityGrade.GRADE_A,
                farmerId = "farmer_ramasamy_01",
                farmerName = "Ramasamy",
                village = "Vellore",
                district = "Tiruvannamalai",
                harvestDate = "12 Apr 2025",
                availableFrom = "Tomorrow",
                description = "Fresh and chemical-free tomatoes, harvested today morning from rich red loam soil.",
                isOrganic = true
            ),
            ProduceItem(
                id = "p_onion_02",
                nameEn = "Onion",
                nameTa = "வெங்காயம்",
                category = "Vegetables",
                pricePerKg = 16.0,
                marketBenchmarkPrice = 14.0,
                availableKg = 300.0,
                grade = QualityGrade.GRADE_A,
                farmerId = "farmer_ramasamy_01",
                farmerName = "Ramasamy",
                village = "Vellore",
                district = "Tiruvannamalai",
                harvestDate = "11 Apr 2025",
                availableFrom = "Today",
                description = "High pungency small shallots and red bell onions, cured and sundried.",
                isOrganic = false
            ),
            ProduceItem(
                id = "p_banana_03",
                nameEn = "Banana",
                nameTa = "வாழை",
                category = "Fruits",
                pricePerKg = 14.0,
                marketBenchmarkPrice = 12.0,
                availableKg = 250.0,
                grade = QualityGrade.GRADE_A,
                farmerId = "farmer_ramasamy_01",
                farmerName = "Ramasamy",
                village = "Polur",
                district = "Tiruvannamalai",
                harvestDate = "10 Apr 2025",
                availableFrom = "Today",
                description = "Naturally ripened Robusta and Poovan bananas, sweet and pesticide-free.",
                isOrganic = true
            ),
            ProduceItem(
                id = "p_brinjal_04",
                nameEn = "Brinjal",
                nameTa = "கத்தரிக்காய்",
                category = "Vegetables",
                pricePerKg = 24.0,
                marketBenchmarkPrice = 20.0,
                availableKg = 180.0,
                grade = QualityGrade.GRADE_B,
                farmerId = "farmer_muthu_02",
                farmerName = "Muthuvel",
                village = "Chetpet",
                district = "Tiruvannamalai",
                harvestDate = "12 Apr 2025",
                availableFrom = "Tomorrow",
                description = "Tender green-striped country brinjals, soft seed texture.",
                isOrganic = true
            ),
            ProduceItem(
                id = "p_coconut_05",
                nameEn = "Coconut",
                nameTa = "தேங்காய்",
                category = "Others",
                pricePerKg = 30.0,
                marketBenchmarkPrice = 25.0,
                availableKg = 600.0,
                grade = QualityGrade.GRADE_A,
                farmerId = "farmer_selvam_03",
                farmerName = "Selvam",
                village = "Arani",
                district = "Tiruvannamalai",
                harvestDate = "08 Apr 2025",
                availableFrom = "Ready Now",
                description = "Large mature oil-rich coconuts with thick meat and sweet water.",
                isOrganic = true
            ),
            ProduceItem(
                id = "p_rice_06",
                nameEn = "Ponni Rice",
                nameTa = "பொன்னி அரிசி",
                category = "Grains",
                pricePerKg = 48.0,
                marketBenchmarkPrice = 42.0,
                availableKg = 1200.0,
                grade = QualityGrade.GRADE_A,
                farmerId = "farmer_ramasamy_01",
                farmerName = "Ramasamy",
                village = "Vellore",
                district = "Tiruvannamalai",
                harvestDate = "01 Apr 2025",
                availableFrom = "Ready Now",
                description = "Aged 1-year premium raw Ponni paddy milled cleanly, fragrant and long grain.",
                isOrganic = true
            )
        )
    }

    private fun getInitialOrders(): List<OrderRecord> {
        val tomato = getInitialProduce()[0]
        val onion = getInitialProduce()[1]
        val banana = getInitialProduce()[2]

        return listOf(
            OrderRecord(
                orderId = "UM12345",
                buyerName = "Hotel Sunrise",
                deliveryAddress = "Hotel Sunrise, 123 Main Road, Tiruvannamalai - 606601",
                items = listOf(CartItem(tomato, 100.0)),
                subtotal = 1800.0,
                deliveryFee = 150.0,
                platformFee = 90.0,
                totalAmount = 2040.0,
                paymentMethod = "UPI",
                date = "12 Apr 2025, 10:15 AM",
                status = OrderStatus.OUT_FOR_DELIVERY,
                trackingSteps = listOf(
                    TrackingStep("Order Confirmed", "10:20 AM", true),
                    TrackingStep("Pickup Scheduled", "Today, 2:00 PM", true),
                    TrackingStep("Out for Delivery", "Today, 4:00 PM", true),
                    TrackingStep("Delivered", "Expected by 6:00 PM", false)
                )
            ),
            OrderRecord(
                orderId = "UM11980",
                buyerName = "Sri Krishna Mess",
                deliveryAddress = "Car Street, Tiruvannamalai",
                items = listOf(CartItem(onion, 50.0)),
                subtotal = 800.0,
                deliveryFee = 50.0,
                platformFee = 40.0,
                totalAmount = 850.0,
                paymentMethod = "UPI",
                date = "8 Apr 2025",
                status = OrderStatus.DELIVERED,
                trackingSteps = listOf(
                    TrackingStep("Order Confirmed", "8 Apr, 9:00 AM", true),
                    TrackingStep("Pickup Scheduled", "8 Apr, 11:30 AM", true),
                    TrackingStep("Out for Delivery", "8 Apr, 1:00 PM", true),
                    TrackingStep("Delivered", "8 Apr, 2:15 PM", true)
                )
            ),
            OrderRecord(
                orderId = "UM11024",
                buyerName = "Annapoorna Caterers",
                deliveryAddress = "Gandhi Nagar, Tiruvannamalai",
                items = listOf(CartItem(banana, 200.0)),
                subtotal = 2600.0,
                deliveryFee = 100.0,
                platformFee = 100.0,
                totalAmount = 2800.0,
                paymentMethod = "Bank Transfer",
                date = "3 Apr 2025",
                status = OrderStatus.DELIVERED,
                trackingSteps = listOf(
                    TrackingStep("Order Confirmed", "3 Apr, 8:00 AM", true),
                    TrackingStep("Pickup Scheduled", "3 Apr, 10:00 AM", true),
                    TrackingStep("Out for Delivery", "3 Apr, 12:00 PM", true),
                    TrackingStep("Delivered", "3 Apr, 1:45 PM", true)
                )
            )
        )
    }

    private fun getInitialIncomeComparisons(): List<FarmerSaleComparison> {
        return listOf(
            FarmerSaleComparison(
                crop = "Tomato",
                quantityKg = 500.0,
                usualMarketRate = 18.0,
                uzhavanRate = 22.0,
                transportCost = 0.0,
                platformFee = 330.0,
                netEarnings = 10670.0,
                extraIncome = 2000.0,
                percentIncrease = 22.2
            ),
            FarmerSaleComparison(
                crop = "Onion",
                quantityKg = 300.0,
                usualMarketRate = 14.0,
                uzhavanRate = 18.0,
                transportCost = 0.0,
                platformFee = 160.0,
                netEarnings = 5240.0,
                extraIncome = 1200.0,
                percentIncrease = 28.5
            )
        )
    }

    private fun getInitialNotifications(): List<NotificationItem> {
        return listOf(
            NotificationItem(
                id = "notif_1",
                title = "Payment received",
                message = "₹2,040 for Order #UM12345\n12 Apr, 10:45 AM",
                timeAgo = "12 Apr, 10:45 AM",
                type = "payments"
            ),
            NotificationItem(
                id = "notif_2",
                title = "Pickup scheduled",
                message = "Your produce will be picked up\n12 Apr, 1:30 PM",
                timeAgo = "12 Apr, 1:30 PM",
                type = "orders"
            ),
            NotificationItem(
                id = "notif_3",
                title = "New buyer message",
                message = "Hotel Sunrise is interested in your tomato listing.\n12 Apr, 11:30 AM",
                timeAgo = "12 Apr, 11:30 AM",
                type = "orders"
            ),
            NotificationItem(
                id = "notif_4",
                title = "Price update",
                message = "Tomato price increased to ₹22/kg in your area.\n11 Apr, 9:15 AM",
                timeAgo = "11 Apr, 9:15 AM",
                type = "price"
            )
        )
    }
}
