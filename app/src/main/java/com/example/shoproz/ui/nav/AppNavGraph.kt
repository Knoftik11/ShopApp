package com.example.shoproz.ui.nav

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.shoproz.data.repo.AuthRepository
import org.koin.compose.koinInject

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val authRepo: AuthRepository = koinInject()
    val loggedIn by authRepo.isLoggedIn.collectAsState(initial = false)

    val startRoute = if (loggedIn) Routes.CATALOG else Routes.LOGIN

    val backStack by navController.currentBackStackEntryAsState()
    val current = backStack?.destination?.route

    val showBottomBar = current in listOf(
        Routes.CATALOG, Routes.CART, Routes.ORDERS, Routes.PROFILE
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    BottomTab.values().forEach { tab ->
                        NavigationBarItem(
                            selected = current == tab.route,
                            onClick = {
                                navController.navigate(tab.route) {
                                    launchSingleTop = true
                                    popUpTo(Routes.CATALOG) { saveState = true }
                                    restoreState = true
                                }
                            },
                            icon = { Icon(tab.icon, contentDescription = tab.label) },
                            label = { Text(tab.label) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = startRoute,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            composable(Routes.LOGIN) {
                Text("Логин — заглушка")
            }
            composable(Routes.REGISTER) {
                Text("Регистрация — заглушка")
            }
            composable(Routes.CATALOG) {
                Text("Каталог — заглушка")
            }
            composable(
                route = Routes.PRODUCT,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entry ->
                val id = entry.arguments?.getInt("id") ?: 0
                Text("Товар #$id — заглушка")
            }
            composable(Routes.CART) {
                Text("Корзина — заглушка")
            }
            composable(Routes.CHECKOUT) {
                Text("Оформление — заглушка")
            }
            composable(Routes.ORDERS) {
                Text("Мои заказы — заглушка")
            }
            composable(Routes.PROFILE) {
                Text("Профиль — заглушка")
            }
        }
    }
}

private enum class BottomTab(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    CATALOG(Routes.CATALOG, "Каталог", Icons.Filled.Star),
    CART(Routes.CART, "Корзина", Icons.Filled.ShoppingCart),
    ORDERS(Routes.ORDERS, "Заказы", Icons.Filled.Star),
    PROFILE(Routes.PROFILE, "Профиль", Icons.Filled.Person);
}
