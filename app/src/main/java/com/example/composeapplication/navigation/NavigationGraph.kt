package com.example.composeapplication.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composeapplication.ui.*

//@Composable
//fun NavigationGraph(startRoute: NAV_ROUTE = NAV_ROUTE.MAIN) {
//    val navController = rememberNavController()
//
//    val routeAction = remember(navController) { RouteAction(navController) }
//
//    NavHost(navController, startRoute.routeName) {
//        composable(NAV_ROUTE.MAIN.routeName) {
//            MainScreen(routeAction = routeAction)
//        }
//        composable(NAV_ROUTE.LOGIN.routeName) {
//            LoginScreen(routeAction = routeAction)
//        }
//        composable(NAV_ROUTE.REGISTER.routeName) {
//            RegisterScreen(routeAction = routeAction)
//        }
//        composable(NAV_ROUTE.USER_PROFILE.routeName) {
//            UserProfileScreen(routeAction = routeAction)
//        }
//        composable(NAV_ROUTE.SETTING.routeName) {
//            SettingScreen(routeAction = routeAction)
//        }
//    }
//}