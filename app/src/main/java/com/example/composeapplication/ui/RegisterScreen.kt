package com.example.composeapplication.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
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
import com.example.composeapplication.createAccount
import com.example.composeapplication.navigation.RouteAction
import com.example.composeapplication.database
import com.example.composeapplication.getNumber
import com.example.composeapplication.result
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterScreen(routeAction: RouteAction) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordCheck by remember { mutableStateOf("") }
    var idCheck = true

    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    Surface(Modifier.fillMaxSize()) {
        Box(Modifier.padding(8.dp), Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally){
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "??????",
                        modifier = Modifier.padding(8.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp)
                    TextField(value = name,
                        onValueChange = {name = it},
                        modifier = Modifier.padding(8.dp),
                        label = { Text(text = "????????? ??????????????????") },
                        textStyle = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.Black
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text).copy(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = {
                            defaultKeyboardAction(ImeAction.Next)
                        })
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "ID",
                        modifier = Modifier.padding(8.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp)
                    TextField(value = email,
                        onValueChange = { email = it },
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(0.5f),
                        label = { Text(text = "ID??? ??????????????????") },
                        textStyle = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text).copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            keyboardController?.hide()
                        })
                    )
                    Button(onClick =
                    {
                        database.addListenerForSingleValueEvent(object: ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.value != null){
                                    Log.d("PKW", "onDataChange: ${snapshot.value}")
                                    for (column: DataSnapshot in snapshot.children) {
                                        if (column.child("email").value == email) {
                                            idCheck = false
                                            Toast.makeText(context, "?????? ???????????? ??????????????????.", Toast.LENGTH_SHORT).show()
                                            break
                                        }
                                        else idCheck = true
                                    }
                                    if (idCheck) {
                                        idCheck = true
                                        Toast.makeText(context, "?????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                else {
                                    idCheck = true
                                    Toast.makeText(context, "?????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                    },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(text = "????????????", )
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "????????????",
                        modifier = Modifier.padding(8.dp),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.padding(8.dp),
                        label = { Text(text = "??????????????? ??????????????????") },
                        textStyle = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.Black
                        ),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password).copy(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = {
                            defaultKeyboardAction(ImeAction.Next)
                        })
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "???????????? ??????",
                        modifier = Modifier.padding(5.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    TextField(
                        value = passwordCheck,
                        onValueChange = { passwordCheck = it },
                        modifier = Modifier.padding(5.dp),
                        label = {
                            if (passwordCheck == password ) Text(text = "correct")
                            else Text(text = "incorrect")
                        },
                        textStyle = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color.Black
                        ),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password).copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            keyboardController?.hide()
                        })
                    )
                }
                Button(onClick = {
                    if (email != "" && idCheck && (passwordCheck == password) && name != "") {
                        result["name"] = name
                        result["email"] = email
                        result["password"] = password
                        createAccount(email, password)
                        routeAction.goBack()
                    }
                    else Toast.makeText(context, "?????? ???????????? ?????? ????????? ????????????.", Toast.LENGTH_SHORT).show()
                }
                ) {
                    Text(text = "????????????")
                }
            }
        }
    }
}