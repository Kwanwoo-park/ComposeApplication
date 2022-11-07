package com.example.composeapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composeapplication.ui.theme.ComposeApplicationTheme

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

enum class NAV_ROUTE(val routeName: String, val description: String, val btnColor: Color) {
    MAIN("MAIN", "메인 화면", Color(0xFF1538E6)),
    LOGIN("LOGIN", "Login", Color(0xFF150ED8)),
    REGISTER("REGISTER", "회원가입 화면", Color(0xFF690505)),
    USER_PROFILE("USER_PROFILE", "유저 프로필 화면", Color(0xFFD67411)),
    SETTING("SETTING", "설정 화면", Color(0xFF19D3D3))
}

class RouteAction(navHostController: NavHostController) {
    val navTo: (NAV_ROUTE) -> Unit = { route ->
        navHostController.navigate(route.routeName)
    }

    val goBack: () -> Unit = {
        navHostController.navigateUp()
    }

    val toMain: () -> Unit = {
        navHostController.navigate(NAV_ROUTE.MAIN.routeName)
    }
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

            NavButton(route = NAV_ROUTE.LOGIN, routeAction = routeAction)
        }
    }
}

@Composable
fun LoginScreen(routeAction: RouteAction) {
    Surface(Modifier.fillMaxSize()) {
        Box(Modifier.padding(8.dp), Alignment.Center) {
            Text(text = "로그인 화면", style = TextStyle(Color.White, 22.sp, FontWeight.Medium))
            Button(onClick = routeAction.toMain,
                modifier = Modifier
                    .padding(16.dp)
                    .offset(y = 100.dp)
            ) {
                Text(text = "메인 화면으로 가기")
            }
        }
    }
}

@Composable
fun RegisterScreen(routeAction: RouteAction) {
    Surface(Modifier.fillMaxSize()) {
        Box(Modifier.padding(8.dp), Alignment.Center) {
            Text(text = "회원가입 화면", style = TextStyle(Color.White, 22.sp, FontWeight.Medium))
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