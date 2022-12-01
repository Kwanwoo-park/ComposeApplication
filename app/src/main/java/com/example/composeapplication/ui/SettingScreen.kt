package com.example.composeapplication.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.composeapplication.navigation.RouteAction

@Composable
fun SettingScreen(routeAction: RouteAction) {
    Surface(Modifier.fillMaxSize()) {
        Box(Modifier.padding(8.dp), Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {

                }
            }
        }
    }
}