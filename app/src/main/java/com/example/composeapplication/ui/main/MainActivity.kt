package com.example.composeapplication.ui.main

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composeapplication.databaseUser
import com.example.composeapplication.ui.drawer.*
import com.example.composeapplication.ui.login.LoginActivity
import com.example.composeapplication.ui.screens.*
import com.example.composeapplication.ui.theme.ComposeApplicationTheme
import com.google.firebase.auth.FirebaseAuth
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
        databaseUser.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (column in snapshot.children) {
                    if (auth.currentUser?.email == column.child("email").value.toString()) {
                        num = column.key!!
                        break
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)

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
                        if (ContextCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                            Gallery(num, navController)
                        }
                        else {
                            Toast.makeText(this@MainActivity, "스토리지 읽기 권한이 없습니다.", Toast.LENGTH_SHORT).show()
                        }
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