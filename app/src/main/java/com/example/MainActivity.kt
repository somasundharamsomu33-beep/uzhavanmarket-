package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.AppRepository
import com.example.model.*
import com.example.ui.screens.*
import com.example.ui.theme.AgriGreenDark
import com.example.ui.theme.AgriGreenPrimary
import com.example.ui.theme.UzhavanMarketTheme
import com.example.util.LanguageManager

sealed class Screen {
    object Splash : Screen()
    object ChooseRole : Screen()
    object FarmerRegistration : Screen()
    object FarmerHome : Screen()
    object TodaysMarketPrice : Screen()
    data class ListProduce(
        val initialCrop: String? = null,
        val initialPricePerKg: Double? = null
    ) : Screen()
    data class PriceAdvisor(
        val crop: String,
        val quantity: Double,
        val expectedPrice: Double,
        val availableFrom: String,
        val grade: QualityGrade
    ) : Screen()
    object BuyerHome : Screen()
    data class ProductDetails(val produce: ProduceItem) : Screen()
    object Cart : Screen()
    object OrderSummary : Screen()
    object Payment : Screen()
    data class PaymentSuccess(val order: OrderRecord) : Screen()
    data class OrderTracking(val order: OrderRecord) : Screen()
    object OrdersList : Screen()
    object MyListings : Screen()
    object EditProfile : Screen()
    data class EditListing(val listing: ProduceItem) : Screen()
    object PaymentHistory : Screen()
    object Profile : Screen()
    object Notifications : Screen()
    object BrandEnd : Screen()
    object AdminDashboard : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UzhavanMarketTheme {
                MainAppContent()
            }
        }
    }
}

@Composable
fun MainAppContent() {
    val currentLang by AppRepository.currentLanguage.collectAsStateWithLifecycle()
    val currentUser by AppRepository.currentUser.collectAsStateWithLifecycle()
    val produceList by AppRepository.listings.collectAsStateWithLifecycle()
    val cartItems by AppRepository.cart.collectAsStateWithLifecycle()
    val orders by AppRepository.orders.collectAsStateWithLifecycle()
    val incomeList by AppRepository.farmerIncomeList.collectAsStateWithLifecycle()
    val notifications by AppRepository.notifications.collectAsStateWithLifecycle()

    var currentScreen by remember { mutableStateOf<Screen>(Screen.Splash) }
    val screenStack = remember { mutableStateListOf<Screen>(Screen.Splash) }

    fun navigateTo(screen: Screen) {
        screenStack.add(screen)
        currentScreen = screen
    }

    fun navigateBack() {
        if (screenStack.size > 1) {
            screenStack.removeAt(screenStack.lastIndex)
            currentScreen = screenStack.last()
        }
    }

    BackHandler(enabled = screenStack.size > 1) {
        navigateBack()
    }

    // Determine bottom bar visibility
    val isFarmerMainTab = currentUser.role == UserRole.FARMER && (
            currentScreen is Screen.FarmerHome ||
                    currentScreen is Screen.OrdersList ||
                    currentScreen is Screen.Profile
            )

    val isBuyerMainTab = currentUser.role == UserRole.BUYER && (
            currentScreen is Screen.BuyerHome ||
                    currentScreen is Screen.OrdersList ||
                    currentScreen is Screen.Profile
            )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (isFarmerMainTab) {
                FarmerBottomNav(
                    currentScreen = currentScreen,
                    currentLang = currentLang,
                    onNavigate = { screen ->
                        screenStack.clear()
                        screenStack.add(screen)
                        currentScreen = screen
                    }
                )
            } else if (isBuyerMainTab) {
                BuyerBottomNav(
                    currentScreen = currentScreen,
                    currentLang = currentLang,
                    onNavigate = { screen ->
                        screenStack.clear()
                        screenStack.add(screen)
                        currentScreen = screen
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
                when (screen) {
                    is Screen.Splash -> {
                        SplashScreen(
                            currentLang = currentLang,
                            onGetStarted = { navigateTo(Screen.ChooseRole) },
                            onLoginClick = { navigateTo(Screen.ChooseRole) },
                            onSelectLanguage = { AppRepository.setLanguage(it) }
                        )
                    }

                    is Screen.ChooseRole -> {
                        ChooseRoleScreen(
                            currentLang = currentLang,
                            onRoleSelected = { role ->
                                AppRepository.setUserRole(role)
                                when (role) {
                                    UserRole.FARMER -> navigateTo(Screen.FarmerRegistration)
                                    UserRole.BUYER -> {
                                        screenStack.clear()
                                        screenStack.add(Screen.BuyerHome)
                                        currentScreen = Screen.BuyerHome
                                    }
                                    UserRole.ADMIN -> navigateTo(Screen.AdminDashboard)
                                }
                            }
                        )
                    }

                    is Screen.FarmerRegistration -> {
                        FarmerRegistrationScreen(
                            currentLang = currentLang,
                            onBack = { navigateBack() },
                            onRegisterSuccess = { name, phone, district, village ->
                                AppRepository.updateUserProfile(name, phone, district, village, UserRole.FARMER)
                                screenStack.clear()
                                screenStack.add(Screen.FarmerHome)
                                currentScreen = Screen.FarmerHome
                            },
                            onLoginClick = {
                                screenStack.clear()
                                screenStack.add(Screen.FarmerHome)
                                currentScreen = Screen.FarmerHome
                            }
                        )
                    }

                    is Screen.FarmerHome -> {
                        FarmerDashboardScreen(
                            currentLang = currentLang,
                            userProfile = currentUser,
                            onNavigateToListProduce = { navigateTo(Screen.ListProduce()) },
                            onNavigateToMarketPrices = { navigateTo(Screen.TodaysMarketPrice) },
                            onNavigateToListings = { navigateTo(Screen.MyListings) },
                            onNavigateToOrders = { navigateTo(Screen.OrdersList) },
                            onNavigateToPayments = { navigateTo(Screen.PaymentHistory) },
                            onNavigateToNotifications = { navigateTo(Screen.Notifications) },
                            onProfileClick = { navigateTo(Screen.Profile) }
                        )
                    }

                    is Screen.TodaysMarketPrice -> {
                        TodaysMarketPriceScreen(
                            currentLang = currentLang,
                            onBack = { navigateBack() },
                            onUsePriceInListing = { crop, pricePerKg ->
                                navigateTo(Screen.ListProduce(initialCrop = crop, initialPricePerKg = pricePerKg))
                            }
                        )
                    }

                    is Screen.ListProduce -> {
                        ListProduceScreen(
                            currentLang = currentLang,
                            initialCrop = screen.initialCrop,
                            initialPricePerKg = screen.initialPricePerKg,
                            onBack = { navigateBack() },
                            onProceedToPriceAdvisor = { crop, qty, price, availableFrom, grade ->
                                navigateTo(Screen.PriceAdvisor(crop, qty, price, availableFrom, grade))
                            }
                        )
                    }

                    is Screen.PriceAdvisor -> {
                        val advisorInfo = AppRepository.getMarketPriceAdvisor(screen.crop, currentUser.district)
                        PriceAdvisorScreen(
                            currentLang = currentLang,
                            crop = screen.crop,
                            expectedPrice = screen.expectedPrice,
                            quantityKg = screen.quantity,
                            marketInfo = advisorInfo,
                            onBack = { navigateBack() },
                            onConfirmAndPublish = {
                                AppRepository.addProduceListing(
                                    crop = screen.crop,
                                    quantityKg = screen.quantity,
                                    pricePerKg = screen.expectedPrice,
                                    availableFrom = screen.availableFrom,
                                    grade = screen.grade,
                                    description = ""
                                )
                                screenStack.clear()
                                screenStack.add(Screen.FarmerHome)
                                currentScreen = Screen.FarmerHome
                            }
                        )
                    }

                    is Screen.BuyerHome -> {
                        BuyerHomeScreen(
                            currentLang = currentLang,
                            produceList = produceList,
                            cartCount = cartItems.size,
                            onProductClick = { item -> navigateTo(Screen.ProductDetails(item)) },
                            onCartClick = { navigateTo(Screen.Cart) },
                            onNotificationsClick = { navigateTo(Screen.Notifications) }
                        )
                    }

                    is Screen.ProductDetails -> {
                        ProductDetailsScreen(
                            currentLang = currentLang,
                            produce = screen.produce,
                            onBack = { navigateBack() },
                            onAddToCart = { produce, qty ->
                                AppRepository.addToCart(produce, qty)
                            }
                        )
                    }

                    is Screen.Cart -> {
                        CartScreen(
                            currentLang = currentLang,
                            cartItems = cartItems,
                            onBack = { navigateBack() },
                            onQuantityChange = { produceId, delta ->
                                AppRepository.updateCartQuantity(produceId, delta)
                            },
                            onClearAll = { AppRepository.clearCart() },
                            onProceedToCheckout = { navigateTo(Screen.OrderSummary) }
                        )
                    }

                    is Screen.OrderSummary -> {
                        OrderSummaryScreen(
                            currentLang = currentLang,
                            cartItems = cartItems,
                            onBack = { navigateBack() },
                            onProceedToPayment = { navigateTo(Screen.Payment) }
                        )
                    }

                    is Screen.Payment -> {
                        val subtotal = cartItems.sumOf { it.produce.pricePerKg * it.quantityKg }
                        val total = subtotal + 150.0 + (subtotal * 0.05)
                        PaymentScreen(
                            currentLang = currentLang,
                            totalAmount = total,
                            onBack = { navigateBack() },
                            onProcessPayment = { method ->
                                val placedOrder = AppRepository.placeOrder(method)
                                navigateTo(Screen.PaymentSuccess(placedOrder))
                            }
                        )
                    }

                    is Screen.PaymentSuccess -> {
                        PaymentSuccessScreen(
                            currentLang = currentLang,
                            order = screen.order,
                            onTrackOrder = { navigateTo(Screen.OrderTracking(screen.order)) },
                            onBackToHome = {
                                val target = if (currentUser.role == UserRole.FARMER) Screen.FarmerHome else Screen.BuyerHome
                                screenStack.clear()
                                screenStack.add(target)
                                currentScreen = target
                            }
                        )
                    }

                    is Screen.OrderTracking -> {
                        OrderTrackingScreen(
                            currentLang = currentLang,
                            order = screen.order,
                            onBack = { navigateBack() }
                        )
                    }

                    is Screen.OrdersList -> {
                        OrdersListScreen(
                            currentLang = currentLang,
                            orders = orders,
                            onOrderClick = { o -> navigateTo(Screen.OrderTracking(o)) }
                        )
                    }

                    is Screen.MyListings -> {
                        MyListingsScreen(
                            currentLang = currentLang,
                            listings = produceList,
                            onBack = { navigateBack() },
                            onEditListing = { item -> navigateTo(Screen.EditListing(item)) },
                            onAddNewListing = { navigateTo(Screen.ListProduce()) }
                        )
                    }

                    is Screen.EditListing -> {
                        EditListingScreen(
                            currentLang = currentLang,
                            listing = screen.listing,
                            onBack = { navigateBack() },
                            onSaveListing = { updated ->
                                AppRepository.updateProduceListing(updated)
                                navigateBack()
                            }
                        )
                    }

                    is Screen.EditProfile -> {
                        EditProfileScreen(
                            currentLang = currentLang,
                            initialProfile = currentUser,
                            onBack = { navigateBack() },
                            onSaveProfile = { name, phone, district, village, farmSize, mainCrops ->
                                AppRepository.updateFullProfile(name, phone, district, village, farmSize, mainCrops)
                                navigateBack()
                            }
                        )
                    }

                    is Screen.PaymentHistory -> {
                        PaymentHistoryScreen(
                            currentLang = currentLang,
                            incomeComparisons = incomeList,
                            onBack = { navigateBack() }
                        )
                    }

                    is Screen.Profile -> {
                        ProfileScreen(
                            currentLang = currentLang,
                            userProfile = currentUser,
                            onEditProfile = { navigateTo(Screen.EditProfile) },
                            onLanguageChange = { AppRepository.setLanguage(it) },
                            onRoleSwitch = { newRole ->
                                AppRepository.setUserRole(newRole)
                                val target = if (newRole == UserRole.FARMER) Screen.FarmerHome else Screen.BuyerHome
                                screenStack.clear()
                                screenStack.add(target)
                                currentScreen = target
                            },
                            onLogout = {
                                navigateTo(Screen.BrandEnd)
                            }
                        )
                    }

                    is Screen.Notifications -> {
                        NotificationsScreen(
                            currentLang = currentLang,
                            notifications = notifications,
                            onBack = { navigateBack() }
                        )
                    }

                    is Screen.BrandEnd -> {
                        BrandEndScreen(
                            onContinue = {
                                screenStack.clear()
                                screenStack.add(Screen.Splash)
                                currentScreen = Screen.Splash
                            }
                        )
                    }

                    is Screen.AdminDashboard -> {
                        AdminDashboardScreen(
                            produceList = produceList,
                            onBack = { navigateBack() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FarmerBottomNav(
    currentScreen: Screen,
    currentLang: String,
    onNavigate: (Screen) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text(LanguageManager.getString("home", currentLang), fontSize = 11.sp) },
            selected = currentScreen is Screen.FarmerHome,
            onClick = { onNavigate(Screen.FarmerHome) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AgriGreenDark,
                indicatorColor = Color(0xFFDCFCE7)
            )
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Assignment, contentDescription = null) },
            label = { Text(LanguageManager.getString("orders", currentLang), fontSize = 11.sp) },
            selected = currentScreen is Screen.OrdersList,
            onClick = { onNavigate(Screen.OrdersList) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AgriGreenDark,
                indicatorColor = Color(0xFFDCFCE7)
            )
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text(LanguageManager.getString("profile", currentLang), fontSize = 11.sp) },
            selected = currentScreen is Screen.Profile,
            onClick = { onNavigate(Screen.Profile) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AgriGreenDark,
                indicatorColor = Color(0xFFDCFCE7)
            )
        )
    }
}

@Composable
fun BuyerBottomNav(
    currentScreen: Screen,
    currentLang: String,
    onNavigate: (Screen) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text(LanguageManager.getString("home", currentLang), fontSize = 11.sp) },
            selected = currentScreen is Screen.BuyerHome,
            onClick = { onNavigate(Screen.BuyerHome) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AgriGreenDark,
                indicatorColor = Color(0xFFDCFCE7)
            )
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Assignment, contentDescription = null) },
            label = { Text(LanguageManager.getString("orders", currentLang), fontSize = 11.sp) },
            selected = currentScreen is Screen.OrdersList,
            onClick = { onNavigate(Screen.OrdersList) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AgriGreenDark,
                indicatorColor = Color(0xFFDCFCE7)
            )
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text(LanguageManager.getString("profile", currentLang), fontSize = 11.sp) },
            selected = currentScreen is Screen.Profile,
            onClick = { onNavigate(Screen.Profile) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AgriGreenDark,
                indicatorColor = Color(0xFFDCFCE7)
            )
        )
    }
}
