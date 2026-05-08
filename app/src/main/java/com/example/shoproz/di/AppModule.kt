package com.example.shoproz.di

import com.example.shoproz.data.local.TokenStorage
import com.example.shoproz.data.remote.ApiClient
import com.example.shoproz.data.remote.AuthApi
import com.example.shoproz.data.remote.CartApi
import com.example.shoproz.data.remote.OrderApi
import com.example.shoproz.data.remote.ProductApi
import com.example.shoproz.data.repo.AuthRepository
import com.example.shoproz.data.repo.CartRepository
import com.example.shoproz.data.repo.OrderRepository
import com.example.shoproz.data.repo.ProductRepository
import com.example.shoproz.ui.auth.AuthViewModel
import com.example.shoproz.ui.cart.CartViewModel
import com.example.shoproz.ui.catalog.CatalogViewModel
import com.example.shoproz.ui.product.ProductDetailViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { TokenStorage(androidContext()) }
    single { ApiClient(get()) }

    single { AuthApi(get()) }
    single { ProductApi(get()) }
    single { CartApi(get()) }
    single { OrderApi(get()) }

    single { AuthRepository(get(), get()) }
    single { ProductRepository(get()) }
    single { CartRepository(get()) }
    single { OrderRepository(get()) }

    viewModel { AuthViewModel(get()) }
    viewModel { CatalogViewModel(get()) }
    viewModel { ProductDetailViewModel(get(), get()) }
    viewModel { CartViewModel(get(), get()) }
}
