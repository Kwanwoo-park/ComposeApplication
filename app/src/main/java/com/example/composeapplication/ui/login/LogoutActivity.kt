package com.example.composeapplication.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.composeapplication.ui.theme.ComposeApplicationTheme

class LogoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    LogoutScreen()
                }
            }
        }
    }

    @Composable
    fun LogoutScreen() {
        Box(modifier = Modifier.fillMaxSize()) {
            Button(onClick = {
                editor.apply()
                editor.putString("id", "")
                editor.putString("password", "")
                editor.commit()

                intent = Intent(this@LogoutActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }) {
                Text(text = "Logout")
            }
        }
    }
}