package com.example.composeapplication.ui.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.composeapplication.R

sealed class DrawerActivity(val title: String, val icon: Int, val route: String) {
    object Home : DrawerActivity("Home", R.drawable.ic_home,"home")
    object Search: DrawerActivity("Search", R.drawable.ic_search, "search")
    object Gallery: DrawerActivity("Gallery", R.drawable.ic_add_a_photo, "gallery")
    object Favorite: DrawerActivity("Favorite", R.drawable.ic_favorite, "favorite")
    object Account : DrawerActivity("Account", R.drawable.ic_account, "account")

}

private val screens = listOf(
    DrawerActivity.Home,
    DrawerActivity.Search,
    DrawerActivity.Gallery,
    DrawerActivity.Favorite,
    DrawerActivity.Account
)

@Composable
fun BottomNavigation(navController: NavController) {
    androidx.compose.material.BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        contentColor = Color(R.color.teal_200)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        screens.forEach {item ->
            BottomNavigationItem(
                icon =  {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = item.title,
                            modifier = Modifier
                                .width(26.dp)
                                .height(26.dp)
                        )
                },
                label = { Text(item.title, fontSize = 9.sp)},
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = Color.Gray,
                selected = currentRoute == item.route,
                alwaysShowLabel = false,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(it) {saveState = true}
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
        }
    }
}