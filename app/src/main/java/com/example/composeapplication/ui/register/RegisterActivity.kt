package com.example.composeapplication.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
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
import com.example.composeapplication.database
import com.example.composeapplication.getNumber
import com.example.composeapplication.result
import com.example.composeapplication.ui.login.LoginActivity
import com.example.composeapplication.ui.theme.ComposeApplicationTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class RegisterActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    RegisterScreen()
                }
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun RegisterScreen() {
        val intent = Intent()

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
                        Text(text = "이름",
                            modifier = Modifier.padding(8.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp)
                        TextField(value = name,
                            onValueChange = {name = it},
                            modifier = Modifier.padding(8.dp),
                            label = { Text(text = "이름을 입력해주세요") },
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
                        var texts = "아이디를 입력해주세요"
                        Text(text = "ID",
                            modifier = Modifier.padding(8.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp)
                        TextField(value = email,
                            onValueChange = { email = it },
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth(0.5f),
                            label = {
                                    if (email.length !in 8..12 && email.isNotEmpty())
                                        texts = "아이디를 8자에서 12사이로 만들어주세요"
                                    Text(texts)
                            },
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
                                                Toast.makeText(context, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show()
                                                break
                                            }
                                            else idCheck = true
                                        }
                                        if (idCheck) {
                                            idCheck = true
                                            Toast.makeText(context, "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    else {
                                        idCheck = true
                                        Toast.makeText(context, "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }
                            })
                        },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(text = "중복확인", )
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        var texts = "비밀번호를 입력해주세요"
                        Text(
                            text = "비밀번호",
                            modifier = Modifier.padding(8.dp),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        TextField(
                            value = password,
                            onValueChange = { password = it },
                            modifier = Modifier.padding(8.dp),
                            label = {
                                if (password.length !in 10..16 && password != "")
                                    texts = "10자에서 16자 사이로 만들어주세요."
                                Text(text = texts)
                                    },
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
                            text = "비밀번호 확인",
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
                            getNumber()
                            intent.putExtra("comeback", "comeback")
                            setResult(RESULT_OK, intent)
                            finish()
                        }
                        else Toast.makeText(context, "아직 확인되지 않은 부분이 있습니다.", Toast.LENGTH_SHORT).show()
                    }
                    ) {
                        Text(text = "회원가입")
                    }
                }
            }
        }
    }
}