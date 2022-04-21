package com.example.profilecardlayout.ui.theme

import android.annotation.SuppressLint
import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)


val veryLightGrey = Color(0x5CDCDCDC)

val lightGreen200 = Color(0x9932CD32)

/** Extenssion function for Custom Color */
val Colors.lightGreen: Color
    @Composable
    get() = lightGreen200

// another Extenssion function for custom colors
val lightRed200 = Color(0xFFE66E66)
val Colors.lightRed: Color
    @Composable
    get() = lightRed200