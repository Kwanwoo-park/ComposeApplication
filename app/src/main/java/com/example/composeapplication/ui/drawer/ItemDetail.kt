package com.example.composeapplication.ui.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composeapplication.R
import com.example.composeapplication.ui.screens.*
import com.example.composeapplication.ui.theme.ComposeApplicationTheme
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ItemDetail() {
    contentDTOs = ArrayList()
    contentUidList = ArrayList()

    var uid = FirebaseAuth.getInstance().currentUser?.uid


    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)) {
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher),
                contentDescription = "user_profile",
                modifier = Modifier
                    .width(35.dp)
                    .height(35.dp)
                    .padding(7.5.dp)
            )
            Text(
                text = "user_name"
            )
        }
        
        Image(
            painter = painterResource(id = R.color.baseTextColor),
            contentDescription = "main_image",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .fillMaxSize(0.4f)
        )

        Row(modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(8.dp, 0.dp, 0.dp, 0.dp)) {
            Image(
                painter = painterResource(id = R.drawable.ic_favorite_border),
                contentDescription = "heart",
                modifier = Modifier
                    .width(35.dp)
                    .height(35.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.ic_chat_black),
                contentDescription = "comment",
                modifier = Modifier
                    .width(35.dp)
                    .height(35.dp)
            )
        }

        Text(
            text = "좋아요 0개",
            modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp)
        )

        Text(
            text = "사진 내용",
            modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 35.dp)
                .fillMaxWidth()

        )
    }
}