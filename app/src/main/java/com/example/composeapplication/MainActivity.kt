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
var number = ""

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
                            database.addListenerForSingleValueEvent(object: ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    for (column: DataSnapshot in snapshot.children) {
                                        default_id = column.child("id").value.toString()
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterScreen(routeAction: RouteAction) {
    var name by remember { mutableStateOf("")}
    var id by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordCheck by remember { mutableStateOf("") }
    var idCheck = false

    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    
    Surface(Modifier.fillMaxSize()) {
        Box(Modifier.padding(8.dp), Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally){
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "이름", 
                        modifier = Modifier.padding(8.dp), 
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 15.sp)
                    TextField(value = name, 
                        onValueChange = {name = it},
                        modifier = Modifier.padding(8.dp),
                        label = { Text(text = "이름을 입력해주세요")},
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
                        color = Color.Black,
                        fontSize = 15.sp)
                    TextField(value = id,
                        onValueChange = { id = it },
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(0.5f),
                        label = { Text(text = "ID를 입력해주세요")},
                        textStyle = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.Black
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text).copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            keyboardController?.hide()
                        })
                    )
                    Button(onClick =
                    {
                        database.addListenerForSingleValueEvent(object: ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.value != null){
                                    Log.d("PKW", "onDataChange: ${snapshot.value}")
                                    for (column: DataSnapshot in snapshot.children) {
                                        if (column.child("id").value == id) {
                                            idCheck = true
                                            Toast.makeText(context, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show()
                                        }
                                        else {
                                            idCheck = false
                                            Toast.makeText(context, "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show()
                                        }
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
                    Text(
                        text = "비밀번호",
                        modifier = Modifier.padding(8.dp),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.padding(8.dp),
                        label = { Text(text = "비밀번호를 입력해주세요")},
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
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
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
                    if (idCheck && (passwordCheck == password) && name != "") {
                        result["name"] = name
                        result["id"] = id
                        result["password"] = password
                        getNumber()
                        routeAction.navTo(NAV_ROUTE.LOGIN)
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

fun getNumber() {
    database.addListenerForSingleValueEvent(object: ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            var i = 0
            for (column: DataSnapshot in snapshot.children) {
                if (column.key != i.toString()) break
                else i++
            }

            number = i.toString()

            setDatabase()
        }

        override fun onCancelled(error: DatabaseError) {
            //
        }
    })
}

fun setDatabase() {
    database.child(number).setValue(result)
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