package com.example.composeapplication

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.indication
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composeapplication.ui.theme.ComposeApplicationTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : ComponentActivity() {
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
}

val database = FirebaseDatabase.getInstance().getReference("User")
var result = mutableMapOf<String, String>()

enum class NAV_ROUTE(val routeName: String, val description: String, val btnColor: Color) {
    MAIN("MAIN", "Main", Color(0xFF1538E6)),
    LOGIN("LOGIN", "Login", Color(0xFF150ED8)),
    REGISTER("REGISTER", "Register", Color(0xFF690505)),
    USER_PROFILE("USER_PROFILE", "User Profile", Color(0xFFD67411)),
    SETTING("SETTING", "Setting", Color(0xFF19D3D3))
}

class RouteAction(navHostController: NavHostController) {
    val navTo: (NAV_ROUTE) -> Unit = { route ->
        navHostController.navigate(route.routeName)
    }

    val goBack: () -> Unit = {
        navHostController.navigateUp()
    }

//    val toMain: (NAV_ROUTE) -> Unit = {
//        navHostController.navigate(NAV_ROUTE.MAIN.routeName)
//    }
}


@Composable
fun NavigationGraph(startRoute: NAV_ROUTE = NAV_ROUTE.LOGIN) {
    val navController = rememberNavController()

    val routeAction = remember(navController) { RouteAction(navController)}

    NavHost(navController, startRoute.routeName) {
        composable(NAV_ROUTE.MAIN.routeName) {
            MainScreen(routeAction = routeAction)
        }
        composable(NAV_ROUTE.LOGIN.routeName) {
            LoginScreen(routeAction = routeAction)
        }
        composable(NAV_ROUTE.REGISTER.routeName) {
            RegisterScreen(routeAction = routeAction)
        }
        composable(NAV_ROUTE.USER_PROFILE.routeName) {
            UserProfileScreen(routeAction = routeAction)
        }
        composable(NAV_ROUTE.SETTING.routeName) {
            SettingScreen(routeAction = routeAction)
        }
    }
}

@Composable
fun MainScreen(routeAction: RouteAction) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(Modifier.padding(16.dp)) {

            NavButton(route = NAV_ROUTE.SETTING, routeAction = routeAction)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(routeAction: RouteAction) {
    val context = LocalContext.current

    val keyboardController = LocalSoftwareKeyboardController.current

    val default_id = "akakslslzz"
    val default_password = "zzqqwoo1310!"

    var id by remember { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Surface(Modifier.fillMaxSize()){
        Box(Modifier.padding(8.dp), Alignment.Center){
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier
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
                    modifier = Modifier
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

                Row(){
                    Button(
                        onClick = {
                            if (id == default_id && password == default_password)
                                routeAction.navTo(NAV_ROUTE.MAIN)
                            else
                                Toast.makeText(context, "아이디 또는 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT)
                                    .show()
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

@Composable
fun RegisterScreen(routeAction: RouteAction) {
    Surface(Modifier.fillMaxSize()) {
        Box(Modifier.padding(8.dp), Alignment.Center) {
            Button(onClick = { getNumber() }
            ) {
                Text(text = "회원가입")
            }
        }
    }
}

fun getNumber() {
    database.addListenerForSingleValueEvent(object: ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            var i = 0
            for (column: DataSnapshot in snapshot.children) {
                if (column.key != i.toString()) break
                else i++
            }

            result.put("number", i.toString())
            result.put("name", "pkw")
            result.put("id", "akakslslzz")
            result.put("password", "zzqqwoo1310!")
            setDatabase()
            Log.d("PKW", "general_num: $i")
        }

        override fun onCancelled(error: DatabaseError) {
            //
        }
    })
}

fun setDatabase() {
    database.child(result["number"].toString()).child("value").setValue(result)
    database.push()
}

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

@Composable
fun SettingScreen(routeAction: RouteAction) {
    Surface(Modifier.fillMaxSize()) {
        Box(Modifier.padding(8.dp), Alignment.Center) {
            Text(text = "설정 화면", style = TextStyle(Color.White, 22.sp, FontWeight.Medium))
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

@Composable
fun ColumnScope.NavButton(route: NAV_ROUTE, routeAction: RouteAction) {
    Button(onClick = {
        routeAction.navTo(route)
    },
        colors = ButtonDefaults.buttonColors(backgroundColor = route.btnColor),
    ) {
        Text(
            text = route.description,
            style = TextStyle(Color.White, 22.sp, FontWeight.Medium)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeApplicationTheme {
        NavigationGraph()
    }
}