package com.example.shoproz.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    onLoggedOut: () -> Unit,
    vm: ProfileViewModel = koinViewModel()
) {
    val user by vm.user.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Профиль", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))

        if (user != null) {
            Text("Имя: ${user!!.name}")
            Text("Email: ${user!!.email}")
            Text("Роль: ${user!!.role}")
        } else {
            Text("Нет данных пользователя")
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                vm.logout()
                onLoggedOut()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Выйти")
        }
    }
}
