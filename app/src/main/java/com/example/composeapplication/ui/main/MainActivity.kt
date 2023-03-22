package com.example.composeapplication.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composeapplication.database
import com.example.composeapplication.ui.drawer.*
import com.example.composeapplication.ui.login.LoginActivity
import com.example.composeapplication.ui.screens.*
import com.example.composeapplication.ui.theme.ComposeApplicationTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

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


    @Composable
    fun MainScreen() {
        var num = "1"
        database.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (column in snapshot.children) {
                    if (auth.currentUser?.email == column.child("email").value.toString()) {
                        num = column.key!!
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        Log.d("pkw", "MainScreen: $num")

        val navController = rememberNavController()
        
        Scaffold(
            bottomBar = { BottomNavigation(navController = navController)}
        ) {
            Box(modifier = Modifier.padding(it)) {
                NavHost(
                    navController = navController,
                    startDestination = DrawerActivity.Home.route
                ) {
                    composable(DrawerActivity.Home.route) {
                        if (auth.currentUser?.uid.isNullOrEmpty()){
                            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                            finish()
                        }
                        Home(num)
                    }
                    composable(DrawerActivity.Search.route) {
                        Search ()
                    }
                    composable(DrawerActivity.Gallery.route) {
                        Gallery ()
                    }
                    composable(DrawerActivity.Favorite.route) {
                        Favorite ()
                    }
                    composable(DrawerActivity.Account.route) {
                        Account (
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}