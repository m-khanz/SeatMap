package com.example.seatmap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import com.example.seatmap.ui.theme.SeatMapTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SeatMapTheme {
                Scaffold { innerPadding ->
                    SeatMap(modifier = androidx.compose.ui.Modifier.padding(innerPadding))
                }
            }
        }
    }
}
