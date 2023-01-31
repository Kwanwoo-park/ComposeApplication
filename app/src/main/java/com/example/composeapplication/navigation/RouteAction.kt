package com.example.composeapplication.navigation

import androidx.navigation.NavHostController

class RouteAction(navHostController: NavHostController) {
    val navTo: (NAV_ROUTE) -> Unit = { route ->
        navHostController.navigate(route.routeName)
    }


    val goBack: () -> Unit = {
        navHostController.navigateUp()
    }

//    val toMain: (NAV_ROUTE) -> Unit = {
//        navHostController.navigate(NAV_ROUTE.MAIN.routeName)
//    }
}