package com.mkhanz.seatmap

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.ConfirmationNumber
import androidx.compose.material.icons.rounded.FlightTakeoff
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ModernFloatingHeader(
    isExpanded: Boolean,
    onExpandChange: (Boolean) -> Unit
) {
    val expansionProgress = remember { Animatable(0f) }

    val collapsedWidth = 220.dp
    val collapsedHeight = 56.dp
    val config = LocalConfiguration.current
    val expandedWidth = (config.screenWidthDp.dp - 32.dp)
    val expandedHeight = 340.dp

    val fluidEasing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)

    LaunchedEffect(isExpanded) {
        expansionProgress.animateTo(
            targetValue = if (isExpanded) 1f else 0f,
            animationSpec = tween(durationMillis = 600, easing = fluidEasing)
        )
    }

    val currentWidth by remember {
        derivedStateOf {
            lerp(
                collapsedWidth,
                expandedWidth,
                expansionProgress.value
            )
        }
    }
    val currentHeight by remember {
        derivedStateOf {
            lerp(
                collapsedHeight,
                expandedHeight,
                expansionProgress.value
            )
        }
    }
    val cornerRadius by remember { derivedStateOf { lerp(28.dp, 32.dp, expansionProgress.value) } }

    val collapsedContentAlpha = (1f - expansionProgress.value * 3f).coerceIn(0f, 1f)
    val expandedContentAlpha = ((expansionProgress.value - 0.3f) * 2f).coerceIn(0f, 1f)

    Box(
        modifier = Modifier
            .padding(top = 0.dp)
            .fillMaxWidth()
            .padding(horizontal = if (isExpanded) 16.dp else 0.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .width(currentWidth)
                .height(currentHeight)
                .shadow(20.dp, RoundedCornerShape(cornerRadius), spotColor = Color(0x80000000))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onExpandChange(!isExpanded) },
            shape = RoundedCornerShape(cornerRadius),
            color = Color.Transparent,
            border = BorderStroke(1.dp, Color(0xFF4338CA).copy(alpha = 0.5f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.linearGradient(listOf(Color(0xFF0F172A), Color(0xFF312E81))))
            ) {
                if (collapsedContentAlpha > 0) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer { alpha = collapsedContentAlpha },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("LHR", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(Modifier.width(12.dp))
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text("HND", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(Modifier.width(16.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20))
                                .background(Color(0xFF000000).copy(alpha = 0.3f))
                                .border(
                                    1.dp,
                                    Brush.linearGradient(listOf(Color(0xFFA78BFA), Color(0xFF22D3EE))),
                                    RoundedCornerShape(20)
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                "12h 40m",
                                style = TextStyle(
                                    brush = Brush.linearGradient(colors = listOf(Color(0xFFA78BFA), Color(0xFF22D3EE))),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            )
                        }
                    }
                }

                if (expandedContentAlpha > 0) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                            .graphicsLayer {
                                alpha = expandedContentAlpha
                                translationY = (1f - expandedContentAlpha) * 20f
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("LHR", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(Modifier.width(16.dp))
                            Icon(
                                Icons.Rounded.FlightTakeoff,
                                contentDescription = null,
                                tint = Color(0xFFA78BFA),
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(Modifier.width(16.dp))
                            Text("HND", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        Text(
                            "Feb 28, 2026 • 12h 40m",
                            color = Color(0xFF94A3B8),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(Modifier.height(24.dp))
                        HorizontalDivider(color = Color(0xFF334155))
                        Spacer(Modifier.height(24.dp))

                        Row(Modifier.fillMaxWidth()) {
                            DetailColumn("Flight", "JL44", Alignment.Start, Modifier.weight(1f))
                            DetailColumn("Class", "Economy", Alignment.CenterHorizontally, Modifier.weight(1f))
                            DetailColumn("Seat", "--", Alignment.End, Modifier.weight(1f))
                        }

                        Spacer(Modifier.height(16.dp))

                        Row(Modifier.fillMaxWidth()) {
                            DetailColumn("Passenger", "Kyriakos G.", Alignment.Start, Modifier.weight(1f))
                            DetailColumn("Terminal", "3", Alignment.CenterHorizontally, Modifier.weight(1f))
                            DetailColumn("Gate", "42", Alignment.End, Modifier.weight(1f))
                        }

                        Spacer(Modifier.weight(1f))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.05f))
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Rounded.ConfirmationNumber,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Spacer(Modifier.width(12.dp))
                                    Column {
                                        Text("Boarding", color = Color(0xFF94A3B8), fontSize = 11.sp)
                                        Text(
                                            "10:40 AM",
                                            color = Color.White,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(100))
                                        .background(Color(0xFF059669).copy(alpha = 0.2f))
                                        .border(1.dp, Color(0xFF059669), RoundedCornerShape(100))
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        "On Time",
                                        color = Color(0xFF34D399),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailColumn(
    label: String,
    value: String,
    alignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = alignment
    ) {
        Text(label, color = Color(0xFF94A3B8), fontSize = 12.sp, fontWeight = FontWeight.Medium)
        Text(value, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}
