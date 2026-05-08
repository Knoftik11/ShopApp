package com.example.shoproz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.shoproz.ui.nav.AppNavGraph
import com.example.shoproz.ui.theme.ShopRozTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShopRozTheme {
                AppNavGraph()
            }
        }
    }
}