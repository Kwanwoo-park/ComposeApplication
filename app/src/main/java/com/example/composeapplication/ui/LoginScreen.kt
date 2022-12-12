package com.example.composeapplication.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composeapplication.navigation.NAV_ROUTE
import com.example.composeapplication.navigation.RouteAction
import com.example.composeapplication.database
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(routeAction: RouteAction) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var check = false

    var default_id = ""
    var default_password = ""
    var name = ""

    var id by remember { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Surface(Modifier.fillMaxSize()){
        Box(Modifier.padding(8.dp), Alignment.Center){
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                    ,modifier = Modifier
                        .padding(20.dp)
                ) {
                    Text(
                        text = "ID",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(20.dp)
                    )
                    TextField(
                        value = id,
                        onValueChange = { id = it },
                        label = { Text(text = "Enter ID") },
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text).copy(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = {
                            defaultKeyboardAction(imeAction = ImeAction.Next)
                        })
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                    ,modifier = Modifier
                        .padding(20.dp)
                ) {
                    Text(
                        text = "PW",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(12.dp)
                    )
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(text = "Enter password") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password).copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            keyboardController?.hide()
                        })
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically){
                    Button(
                        onClick = {
                            database.addListenerForSingleValueEvent(object: ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    for (column: DataSnapshot in snapshot.children) {
                                        default_id = column.child("email").value.toString()
                                        default_password = column.child("password").value.toString()
                                        name = column.child("name").value.toString()
                                        if (id == default_id && password == default_password){
                                            Toast.makeText(context, "${name}님 환영합니다.", Toast.LENGTH_SHORT).show()
                                            check = true
                                            routeAction.navTo(NAV_ROUTE.MAIN)
                                            break
                                        }
                                    }
                                    if (!check) Toast.makeText(context, "아이디 또는 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }
                            })
                        },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(text = "Login")
                    }

                    Button(
                        onClick = {routeAction.navTo(NAV_ROUTE.REGISTER)},
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(text = "회원가입")
                    }
                }
            }
        }
    }
}