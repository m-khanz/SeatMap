package com.mkhanz.seatmap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mkhanz.seatmap.experience.SeatExperienceFlow
import com.mkhanz.seatmap.ui.theme.SeatMapTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SeatMapTheme {
                androidx.compose.material3.Surface(modifier = Modifier.fillMaxSize()) {
                    SeatExperienceFlow()
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF020615)
@Composable
fun PreviewMotion() {
    SeatMapTheme {
        androidx.compose.material3.Surface(modifier = Modifier.fillMaxSize()) {
            SeatExperienceFlow()
        }
    }
}
