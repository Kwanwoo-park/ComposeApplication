package com.example.composeapplication.ui.screens

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.example.composeapplication.model.ContentDTO
import com.example.composeapplication.ui.drawer.ItemDetail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

var user: FirebaseUser? = null

lateinit var contentDTOs: MutableList<ContentDTO>
lateinit var contentUidList: MutableList<String>

@Composable
fun Home(home: String) {
    Log.d("pkw", "Home: $home")

    contentDTOs = ArrayList()
    contentUidList = ArrayList()

    user = FirebaseAuth.getInstance().currentUser

    LazyColumn {
        items(
            items = contentDTOs,
            itemContent = { ItemDetail() }
        )
    }
}