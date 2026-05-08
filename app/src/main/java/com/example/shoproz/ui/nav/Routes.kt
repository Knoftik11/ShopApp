package com.example.shoproz.ui.nav

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val CATALOG = "catalog"
    const val PRODUCT = "product/{id}"
    const val CART = "cart"
    const val CHECKOUT = "checkout"
    const val ORDERS = "orders"
    const val PROFILE = "profile"

    fun product(id: Int) = "product/$id"
}
