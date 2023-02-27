package com.example.composeapplication.ui.intro

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
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
import com.example.composeapplication.database
import com.example.composeapplication.ui.login.LoginActivity
import com.example.composeapplication.ui.login.editor
import com.example.composeapplication.ui.login.sharedPreferences
import com.example.composeapplication.ui.main.MainActivity
import com.example.composeapplication.ui.theme.ComposeApplicationTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

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

    private fun checkSharePreference() {
        val test_id = sharedPreferences.getString("id", "")
        val test_password = sharedPreferences.getString("password", "")
        Log.d("pkw", "checkSharePreference: $test_id, $test_password")
        if (test_id != "" && test_password != "") {
            database.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (column: DataSnapshot in snapshot.children) {
                        val num = column.key
                        val name = column.child("name").value.toString()
                        val id = column.child("email").value.toString()
                        val password = column.child("password").value.toString()

                        if (id == test_id && password == test_password) {
                            intent = Intent(this@IntroActivity, MainActivity::class.java)
                            intent.putExtra("user_num", num)
                            Toast.makeText(this@IntroActivity, "${name}님 환영합니다.", Toast.LENGTH_SHORT).show()
                            startActivity(intent)
                            finish()
                            break
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
        else {
            intent = Intent(this@IntroActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
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

    @Composable
    fun IntroScreen() {
        val snackBarState = remember { SnackbarHostState() }

        val coroutineScope = rememberCoroutineScope()

        if (!isNetworkAvailable(this)) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "인터넷 연결을 확인해주세요",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Button(onClick = {
                        if (snackBarState.currentSnackbarData != null) {
                            snackBarState.currentSnackbarData?.dismiss()
                            return@Button
                        }
                        coroutineScope.launch {
                            val result = snackBarState.showSnackbar(
                                "어플리케이션을 종료합니다.",
                                "확인",
                                SnackbarDuration.Short
                            ).let {
                                when(it) {
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

                    SnackbarHost(hostState = snackBarState, modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            }
        }
        else{
            val context = LocalContext.current

            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            editor = sharedPreferences.edit()

            checkSharePreference()

            Surface(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
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