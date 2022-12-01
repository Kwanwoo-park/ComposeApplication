package com.example.composeapplication.navigation

import androidx.compose.ui.graphics.Color

enum class NAV_ROUTE(val routeName: String, val description: String, val btnColor: Color) {
    MAIN("MAIN", "Main", Color(0xFF1538E6)),
    LOGIN("LOGIN", "Login", Color(0xFF150ED8)),
    REGISTER("REGISTER", "Register", Color(0xFF690505)),
    USER_PROFILE("USER_PROFILE", "User Profile", Color(0xFFD67411)),
    SETTING("SETTING", "Setting", Color(0xFF19D3D3))
}