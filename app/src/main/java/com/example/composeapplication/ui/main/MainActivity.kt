package com.example.composeapplication.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composeapplication.ui.drawer.*
import com.example.composeapplication.ui.login.LoginActivity
import com.example.composeapplication.ui.theme.ComposeApplicationTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                }
            }
        }
    }

//    override fun onBackPressed() {
//        if (System.currentTimeMillis() - waitTime >= 2000) {
//            waitTime = System.currentTimeMillis()
//            Toast.makeText(this, "뒤로가기 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
//        }
//        else{
//            finish()
//        }
//    }

    @Composable
    fun MainScreen() {
        val navController = rememberNavController()
        
        Surface(color = MaterialTheme.colors.background) {
            val drawerState = rememberDrawerState(DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            val openDrawer = {
                scope.launch {
                    drawerState.open()
                }
            }
            
            ModalDrawer(
                drawerState = drawerState,
                gesturesEnabled = drawerState.isOpen,
                drawerContent = {
                    Drawer(onDestinationClicked = {route ->  
                        scope.launch { 
                            drawerState.close()
                        }
                        navController.navigate(route) {
                            popUpTo = navController.graph.getStartDestination()
                            launchSingleTop = true
                        }
                    })
                }
            ) {
                NavHost(
                    navController = navController,
                    startDestination = DrawerActivity.Home.route
                ) {
                    composable(DrawerActivity.Home.route) {
                        if (auth.currentUser?.uid.isNullOrEmpty()){
                            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                            finish()
                        }
                        Home(
                            openDrawer = {
                                openDrawer()
                            }
                        )
                    }
                    composable(DrawerActivity.Account.route) {
                        Account (
                            openDrawer = {
                                openDrawer()
                            },
                            navController = navController
                        )
                    }
                    composable(DrawerActivity.Help.route) {
                        Help(navController = navController)
                    }
                }
            }
        }
    }
}