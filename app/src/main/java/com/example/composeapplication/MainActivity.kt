package com.example.composeapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.composeapplication.navigation.NavigationGraph
import com.example.composeapplication.ui.theme.ComposeApplicationTheme
import com.google.firebase.database.FirebaseDatabase

class MainActivity : ComponentActivity() {
    var waitTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavigationGraph()
                }
            }
        }
    }

//    override fun onBackPressed() {
//        if (){
//            if (System.currentTimeMillis() - waitTime >= 1500) {
//                waitTime = System.currentTimeMillis()
//                Toast.makeText(this, "뒤로가기 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
//            }
//            else {
//                finish()
//            }
//        }
//        else {
//            super.onBackPressed()
//        }
//    }
}

val database = FirebaseDatabase.getInstance().getReference("User")
var result = mutableMapOf<String, String>()
var number = ""

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeApplicationTheme {
        NavigationGraph()
    }
}