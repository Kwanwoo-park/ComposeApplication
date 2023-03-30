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

lateinit var contentDTOs: MutableList<ContentDTO>
lateinit var contentUidList: MutableList<String>

@Composable
fun Home(num: String) {
    Log.d("pkw", "Home: $num")

    contentDTOs = ArrayList()
    contentUidList = ArrayList()

    user = FirebaseAuth.getInstance().currentUser

    LazyColumn(modifier = Modifier.fillMaxSize()
        .background(color = colorResource(id = R.color.baseBackground))) {
        items(
            items = contentDTOs,
            itemContent = { ItemDetail() }
        )
    }
}