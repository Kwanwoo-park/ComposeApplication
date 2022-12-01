package com.example.composeapplication.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composeapplication.navigation.RouteAction

@Composable
fun UserProfileScreen(routeAction: RouteAction) {
    Surface(Modifier.fillMaxSize()) {
        Box(Modifier.padding(8.dp), Alignment.Center) {
            Text(text = "유저 프로필 화면", style = TextStyle(Color.White, 22.sp, FontWeight.Medium))
            Button(onClick = routeAction.goBack,
                modifier = Modifier
                    .padding(16.dp)
                    .offset(y = 100.dp)
            ) {
                Text(text = "뒤로가기")
            }
        }
    }
}