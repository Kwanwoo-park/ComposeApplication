package com.example.composeapplication.ui.drawer

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.composeapplication.database
import com.example.composeapplication.ui.login.LoginActivity
import com.example.composeapplication.ui.login.LogoutActivity
//import com.example.composeapplication.ui.login.editor
//import com.example.composeapplication.ui.login.intent
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

@Composable
fun Home(openDrawer: () -> Unit, num: String?, name: String?) {
    Column(modifier = Modifier.fillMaxSize()) {
        Topbar(
            title = "Home",
            buttonIcon = Icons.Filled.Menu,
            onButtonClicked = { openDrawer() }
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "${name}님의 메인화면")
        }
    }
}

@Composable
fun Account(openDrawer: () -> Unit, num: String?, name: String?) {
    val snackbarState = remember { SnackbarHostState() }
    var coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        Topbar(
            title = "Account",
            buttonIcon = Icons.Filled.Menu,
            onButtonClicked = { openDrawer() }
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Account.", style = MaterialTheme.typography.h4)
            Button(onClick = {
                if (snackbarState.currentSnackbarData != null) {
                    snackbarState.currentSnackbarData?.dismiss()
                    return@Button
                }

                coroutineScope.launch {
                    val result = snackbarState.showSnackbar(
                        "${name}님 \n로그아웃하시면 앱이 종료됩니다.",
                        "확인",
                        SnackbarDuration.Short
                    ).let {
                        when(it) {
                            SnackbarResult.Dismissed -> Log.d("pkw", "Account: Snackbar close")
                            SnackbarResult.ActionPerformed -> {
//                                editor.apply()
//                                editor.putString("id", "")
//                                editor.putString("password", "")
//                                editor.commit()

                                exitProcess(0)
                            }
                        }
                    }
                }
            }) {
                Text(text = "logout")
            }
            SnackbarHost(hostState = snackbarState, modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}

/*
* if (snackBarState.currentSnackbarData != null) {
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
                        }*/

@Composable
fun Help(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        Topbar(
            title = "Help",
            buttonIcon = Icons.Filled.ArrowBack,
            onButtonClicked = {navController.popBackStack()}
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Help.", style = MaterialTheme.typography.h4)
        }
    }
}