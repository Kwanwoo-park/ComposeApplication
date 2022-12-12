package com.example.composeapplication.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.compose.rememberNavController
import com.example.composeapplication.navigation.NAV_ROUTE
import com.example.composeapplication.NavButton
import com.example.composeapplication.navigation.RouteAction

@Composable
fun MainScreen(routeAction: RouteAction) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(Modifier.padding(16.dp)) {
            NavButton(route = NAV_ROUTE.SETTING, routeAction = routeAction)
//            Log.d("pkw", "MainScreen: ${Screen()}")
        }
    }
}

//@Composable
//fun Screen(): Context{
//    val nav = rememberNavController().context
//    return nav
//}