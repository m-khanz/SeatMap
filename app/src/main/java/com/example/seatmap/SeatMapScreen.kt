package com.example.seatmap

import android.app.Activity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SeatMap(modifier: Modifier = Modifier) {
    val seats = remember { generatePreciseLayout() }

    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var isInteracting by remember { mutableStateOf(false) }

    var isConfirmed by remember { mutableStateOf(false) }
    var showBoardingPass by remember { mutableStateOf(false) }
    var confirmedSeat by remember { mutableStateOf<PlaneSeat?>(null) }

    var isHeaderExpanded by remember { mutableStateOf(false) }
    var layoutReady by remember { mutableStateOf(false) }
    var selectedSeat by remember { mutableStateOf<PlaneSeat?>(null) }

    val textMeasurer = rememberTextMeasurer()
    val sharedPath = remember { Path() }

    val minZoom = 0.5f
    val maxZoom = 3.0f
    val bgColor = Color(0xFFF1F5F9)

    val view = LocalView.current
    DisposableEffect(showBoardingPass) {
        val window = (view.context as? Activity)?.window
        if (window != null) {
            val wic = WindowCompat.getInsetsController(window, view)
            wic.isAppearanceLightStatusBars = !showBoardingPass
        }
        onDispose {
            val disposeWindow = (view.context as? Activity)?.window
            if (disposeWindow != null) {
                WindowCompat.getInsetsController(disposeWindow, view).isAppearanceLightStatusBars = true
            }
        }
    }

    LaunchedEffect(isConfirmed) {
        if (isConfirmed) {
            delay(500)

            val startScale = scale
            val startOffset = offset

            val zoomOutTargetScale = 0.35f
            val zoomOutTargetY = startOffset.y + 300f

            animate(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 1200,
                    easing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)
                )
            ) { value, _ ->
                scale = startScale + (zoomOutTargetScale - startScale) * value
                val currentY = startOffset.y + (zoomOutTargetY - startOffset.y) * value
                offset = Offset(startOffset.x, currentY)
            }

            delay(10)

            val launchStartOffset = offset
            animate(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = CubicBezierEasing(0.5f, 0.0f, 0.8f, 0.5f)
                )
            ) { value, _ ->
                val targetY = launchStartOffset.y - 6000f
                val newY = launchStartOffset.y + (targetY - launchStartOffset.y) * value
                offset = Offset(launchStartOffset.x, newY)
                scale = zoomOutTargetScale * (1f - (value * 0.1f))
            }

            showBoardingPass = true
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
            .onSizeChanged { layoutSize ->
                if (layoutReady) return@onSizeChanged
                val fuselageWidth = 900f
                val screenWidth = layoutSize.width.toFloat()
                val idealScale = (screenWidth * 0.85f) / fuselageWidth
                scale = idealScale
                offset = Offset(layoutSize.width / 2f, layoutSize.height / 3f)
                layoutReady = true
            }
            .pointerInput(Unit) {
                detectTapGestures { tapOffset ->
                    if (isConfirmed) return@detectTapGestures

                    val localTap = (tapOffset - offset) / scale
                    val hitSeat = seats.find { seat ->
                        val seatW = 95f
                        val seatH = 105f
                        localTap.x >= seat.x && localTap.x <= seat.x + seatW &&
                            localTap.y >= seat.y && localTap.y <= seat.y + seatH
                    }
                    if (hitSeat != null && hitSeat.status == SeatStatus.AVAILABLE) {
                        selectedSeat = if (selectedSeat?.id == hitSeat.id) null else hitSeat
                        if (selectedSeat != null) isHeaderExpanded = false
                    } else {
                        selectedSeat = null
                    }
                }
            }
            .pointerInput(Unit) {
                detectTransformGestures(
                    onGesture = { centroid, pan, zoom, _ ->
                        if (isConfirmed) return@detectTransformGestures
                        isInteracting = true
                        val oldScale = scale
                        val newScale = (scale * zoom).coerceIn(minZoom, maxZoom)
                        val contentCentroid = (centroid - offset) / oldScale
                        scale = newScale
                        offset = centroid - (contentCentroid * newScale) + pan
                    }
                )
            }
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        if (event.changes.all { !it.pressed }) {
                            isInteracting = false
                        }
                    }
                }
            }
    ) {
        SeatMapCanvas(
            seats = seats,
            selectedSeat = selectedSeat,
            scale = scale,
            offset = offset,
            textMeasurer = textMeasurer,
            reusablePath = sharedPath
        )

        AnimatedVisibility(
            visible = selectedSeat != null && !isConfirmed,
            enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0F172A).copy(alpha = 0.5f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { selectedSeat = null }
            )
        }

        Column(
            Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))

            AnimatedVisibility(
                visible = selectedSeat == null && (!isInteracting || isHeaderExpanded) && !isConfirmed,
                enter = slideInVertically { -it } + fadeIn(animationSpec = tween(500)),
                exit = slideOutVertically { -it } + fadeOut(animationSpec = tween(300))
            ) {
                ModernFloatingHeader(
                    isExpanded = isHeaderExpanded,
                    onExpandChange = { isHeaderExpanded = it }
                )
            }

            Spacer(Modifier.weight(1f))

            AnimatedVisibility(
                visible = selectedSeat != null && !isConfirmed,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = spring(dampingRatio = 0.8f, stiffness = Spring.StiffnessLow)
                ) + fadeIn(),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = spring(dampingRatio = 1f, stiffness = Spring.StiffnessLow)
                ) + fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                50.dp,
                                spotColor = Color(0xFF000000),
                                shape = RoundedCornerShape(40.dp)
                            ),
                        shape = RoundedCornerShape(40.dp),
                        border = BorderStroke(1.dp, Color(0xFF334155)),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A))
                    ) {
                        AnimatedContent(
                            targetState = selectedSeat,
                            label = "SeatAnimation"
                        ) { seat ->
                            if (seat != null) {
                                SeatInfoContent(
                                    seat = seat,
                                    onClose = { selectedSeat = null },
                                    onConfirm = {
                                        confirmedSeat = seat
                                        isConfirmed = true
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showBoardingPass && confirmedSeat != null) {
            BoardingPassTicket(seat = confirmedSeat!!)
        }
    }

    LaunchedEffect(scale, offset) {
        if (isInteracting) {
            delay(150)
            isInteracting = false
        }
    }
}

@Preview
@Composable
fun PreviewMotion() {
    SeatMap()
}
