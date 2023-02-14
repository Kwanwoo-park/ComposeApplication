package com.example.composeapplication.ui.login


import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composeapplication.database
import com.example.composeapplication.ui.intro.IntroActivity
import com.example.composeapplication.ui.main.MainActivity
import com.example.composeapplication.ui.register.RegisterActivity
import com.example.composeapplication.ui.theme.ComposeApplicationTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


lateinit var sharedPreferences: SharedPreferences
lateinit var editor: SharedPreferences.Editor
class LoginActivity: ComponentActivity() {
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d("pkw", "openActivityResultLauncher: sign in success")
        }
        else {
            Log.d("pkw", "openActivityResultLauncher: sign in unsuccess")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    LoginScreen()
                }
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun LoginScreen() {
        lateinit var intent: Intent

        val context = LocalContext.current
        val keyboardController = LocalSoftwareKeyboardController.current
        var check = false

        var name = ""
        var default_id = ""
        var default_password = ""
        var autoLogin by remember { mutableStateOf(false) }

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
                                            val num = column.key
                                            default_id = column.child("email").value.toString()
                                            default_password = column.child("password").value.toString()
                                            name = column.child("name").value.toString()
                                            if (id == default_id && password == default_password){
                                                if (autoLogin) {
                                                    editor.putBoolean("autoLogin", autoLogin)
                                                    editor.apply()
                                                    editor.putString("id", id)
                                                    editor.putString("password", password)
                                                    editor.commit()
                                                }
                                                else {
                                                    editor.putBoolean("auto_login", autoLogin)
                                                    editor.apply()
                                                    editor.putString("id", "")
                                                    editor.putString("password", "")
                                                    editor.commit()
                                                }
                                                Toast.makeText(context, "${name}님 환영합니다.", Toast.LENGTH_SHORT).show()
                                                check = true
                                                intent = Intent(context, MainActivity::class.java)
                                                intent.putExtra("user_num", num)
                                                startActivity(intent)
                                                finish()
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
                            onClick = {
                                val activityLauncher = resultLauncher
                                intent = Intent(context, RegisterActivity::class.java)
                                activityLauncher.launch(intent)
                            },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(text = "회원가입")
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "자동로그인")
                        Checkbox(checked = autoLogin, onCheckedChange = {autoLogin = it })
                    }
                }
            }
        }
    }
}