package com.example.composeapplication.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.composeapplication.BackHandler
import com.example.composeapplication.navigation.NAV_ROUTE
import com.example.composeapplication.NavButton
import com.example.composeapplication.navigation.RouteAction
import kotlinx.coroutines.coroutineScope

@Composable
fun MainScreen(routeAction: RouteAction) {
    BackHandler(enable = true)  {}

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(Modifier.padding(16.dp)) {
            NavButton(route = NAV_ROUTE.SETTING, routeAction = routeAction)
        }
    }
}