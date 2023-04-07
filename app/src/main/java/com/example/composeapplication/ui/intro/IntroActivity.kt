package com.example.composeapplication.ui.intro

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.example.composeapplication.databaseImage
import com.example.composeapplication.model.ContentDTO
import com.example.composeapplication.ui.login.LoginActivity
import com.example.composeapplication.ui.theme.ComposeApplicationTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

lateinit var contentDTOs: MutableList<ContentDTO>
lateinit var contentUidList: MutableList<String>

class IntroActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                   IntroScreen()
                }
            }
        }
    }

    private fun isNetworkAvailabe(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false

            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        }
        else {
            return connectivityManager.activeNetworkInfo?.isConnected ?: false
        }
    }

    private fun moveMainPage() {
        databaseImage.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (contentDTOs.size != 0) contentDTOs.clear()

                for (column in snapshot.children) {
                    var contentDTO = ContentDTO()
                    contentDTO.explain = column.child("explain").value.toString()
                    contentDTO.favoriteCount = column.child("favoriteCount").value.toString().toInt()
                    contentDTO.imageUrl = column.child("imageUrl").value.toString()
                    contentDTO.timestamp = column.child("timestamp").value.toString().toLong()
                    contentDTO.uid = column.child("uid").value.toString()
                    contentDTO.userId = column.child("userId").value.toString()
                    Log.d("pkw", "onDataChange: $contentDTO")
                    contentDTOs.add(contentDTO)
                }
                startActivity(Intent(this@IntroActivity, LoginActivity::class.java))
                finish()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    @Composable
    fun IntroScreen() {
        contentDTOs = mutableListOf()
        contentUidList = mutableListOf()

        val snackbarState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

        if (!isNetworkAvailabe(this)) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "인터넷 연결을 확인해주세요.",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Button(onClick = {
                        if (snackbarState.currentSnackbarData != null) {
                            snackbarState.currentSnackbarData?.dismiss()
                            return@Button
                        }
                        coroutineScope.launch {
                            val result = snackbarState.showSnackbar(
                                "어플리케이션을 종료합니다",
                                "확인",
                                SnackbarDuration.Short
                            ).let {
                                when (it) {
                                    SnackbarResult.Dismissed -> Log.d("pkw", "IntroScreen: Snackbar close")
                                    SnackbarResult.ActionPerformed -> {
                                        Log.d("pkw", "IntroScreen: Snackbar action")

                                        ActivityCompat.finishAffinity(this@IntroActivity)
                                        exitProcess(0)
                                    }
                                }
                            }
                        }
                    }) {
                        Text(text = "종료")
                    }


                    SnackbarHost(hostState = snackbarState, modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            }
        }
        else {
            moveMainPage()

            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Park kwan woo",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "ComposeApplication",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}