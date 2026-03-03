package com.mkhanz.seatmap

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.mkhanz.seatmap.experience.SeatExperienceFlow
import com.mkhanz.seatmap.ui.theme.SeatMapTheme

@Composable
fun App() {
    SeatMapTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SeatExperienceFlow()
        }
    }
}
