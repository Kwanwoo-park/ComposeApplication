package com.example.composeapplication.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.example.composeapplication.R
import com.example.composeapplication.model.ContentDTO
import com.example.composeapplication.ui.drawer.ItemDetail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

var user: FirebaseUser? = null

@Composable
fun Home(num: String, contentDTOs: MutableList<ContentDTO>) {
    Log.d("pkw", "Home: $num")

    user = FirebaseAuth.getInstance().currentUser

    Log.d("pkw", "ContentDTO: ${contentDTOs.size}")

    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .background(color = colorResource(id = R.color.baseBackground))) {
        items(contentDTOs) { ItemDetail(contentDTO = it)}
    }
}