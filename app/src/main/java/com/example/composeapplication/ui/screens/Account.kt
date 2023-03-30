package com.example.composeapplication.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavController
import com.example.composeapplication.R
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Account(navController: NavController) {

    val auth = FirebaseAuth.getInstance()

    Column(modifier = Modifier.fillMaxSize()
        .background(color = colorResource(id = R.color.baseBackground))) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Account.", style = MaterialTheme.typography.h4)
            Button(onClick = {
                Log.d("pkw", "Account: ${auth.currentUser?.uid}")
                auth.signOut()
                navController.popBackStack()
            }) {
                Text(text = "logout")
            }
        }
    }
}