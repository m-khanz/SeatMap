package com.mkhanz.seatmap.experience

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.Help
import androidx.compose.material.icons.rounded.AirlineSeatReclineNormal
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.FlightTakeoff
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.KingBed
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Monitor
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object SeatExperienceColors {
    val BackgroundStart = Color(0xFF040B1E)
    val BackgroundEnd = Color(0xFF020615)
    val Card = Color(0xFF0F162A)
    val CardSoft = Color(0xFF121B31)
    val Border = Color(0xFF25304B)
    val Accent = Color(0xFF2D8CFF)
    val AccentSoft = Color(0x332D8CFF)
    val Gold = Color(0xFFE3C44E)
    val TextPrimary = Color(0xFFF4F7FF)
    val TextMuted = Color(0xFF8C9AB9)
    val Occupied = Color(0xFF1D2434)
}

@Composable
fun SeatTopBar(route: String, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircleIconButton(Icons.AutoMirrored.Rounded.ArrowBack, onClick = onBack)
            CircleIconButton(Icons.Rounded.Info, onClick = {})
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = "SELECT YOUR SPACE",
            color = SeatExperienceColors.TextMuted,
            fontSize = 12.sp,
            letterSpacing = 2.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text(
            text = route,
            color = SeatExperienceColors.TextPrimary,
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SeatMapHeaderStrip(
    flight: FlightContext,
    cabinLabel: String,
    onBack: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xCC090F1D),
        border = androidx.compose.foundation.BorderStroke(1.dp, SeatExperienceColors.Border)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.size(26.dp)) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, null, tint = SeatExperienceColors.TextPrimary)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        cabinLabel,
                        color = SeatExperienceColors.Gold,
                        fontSize = 11.sp,
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "${flight.origin}  →  ${flight.destination}",
                        color = SeatExperienceColors.TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                IconButton(onClick = {}, modifier = Modifier.size(26.dp)) {
                    Icon(Icons.Rounded.Info, null, tint = SeatExperienceColors.Gold)
                }
            }
        }
    }
}

@Composable
fun FlightHeroHeader(flight: FlightContext) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(flight.origin, color = SeatExperienceColors.TextMuted, fontSize = 14.sp, letterSpacing = 1.sp)
                Spacer(Modifier.width(8.dp))
                Box(Modifier.width(38.dp).height(1.dp).background(SeatExperienceColors.Gold.copy(alpha = 0.9f)))
                Spacer(Modifier.width(8.dp))
                Text(flight.destination, color = SeatExperienceColors.TextMuted, fontSize = 14.sp, letterSpacing = 1.sp)
            }
            Text(flight.flightCode, color = SeatExperienceColors.Gold, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
        Spacer(Modifier.height(10.dp))
        Text("Select Your Space", color = SeatExperienceColors.TextPrimary, fontSize = 52.sp, fontWeight = FontWeight.ExtraBold)
        Text("${flight.aircraftLabel} • Game Changer", color = SeatExperienceColors.TextMuted, fontSize = 16.sp)
    }
}

@Composable
private fun CircleIconButton(icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Surface(
        shape = CircleShape,
        color = SeatExperienceColors.Card,
        tonalElevation = 0.dp,
        shadowElevation = 8.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, SeatExperienceColors.Border)
    ) {
        IconButton(onClick = onClick, modifier = Modifier.size(52.dp)) {
            Icon(icon, contentDescription = null, tint = SeatExperienceColors.TextPrimary)
        }
    }
}

@Composable
fun AircraftHeroCard(sectionLabel: String) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        shape = RoundedCornerShape(22.dp),
        color = SeatExperienceColors.Card,
        border = androidx.compose.foundation.BorderStroke(1.dp, SeatExperienceColors.Border)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Canvas(modifier = Modifier.fillMaxWidth().height(92.dp)) {
                val path = Path().apply {
                    moveTo(size.width * 0.06f, size.height * 0.55f)
                    quadraticTo(size.width * 0.35f, size.height * 0.02f, size.width * 0.5f, size.height * 0.02f)
                    quadraticTo(size.width * 0.65f, size.height * 0.02f, size.width * 0.94f, size.height * 0.55f)
                    quadraticTo(size.width * 0.66f, size.height * 0.93f, size.width * 0.5f, size.height * 0.93f)
                    quadraticTo(size.width * 0.34f, size.height * 0.93f, size.width * 0.06f, size.height * 0.55f)
                    close()
                }
                drawPath(path, color = Color(0x111E90FF), style = Stroke(width = 3f))
                drawLine(
                    color = SeatExperienceColors.Accent.copy(alpha = 0.28f),
                    start = Offset(size.width * 0.2f, size.height * 0.42f),
                    end = Offset(size.width * 0.8f, size.height * 0.42f),
                    strokeWidth = 2f
                )
                drawLine(
                    color = SeatExperienceColors.Accent.copy(alpha = 0.28f),
                    start = Offset(size.width * 0.2f, size.height * 0.62f),
                    end = Offset(size.width * 0.8f, size.height * 0.62f),
                    strokeWidth = 2f
                )
            }
            Text(
                sectionLabel,
                color = SeatExperienceColors.TextMuted,
                fontSize = 12.sp,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun SectionPicker(
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        options.forEach { option ->
            val active = option == selected
            Surface(
                modifier = Modifier.clickable { onSelect(option) },
                shape = RoundedCornerShape(22.dp),
                color = if (active) SeatExperienceColors.AccentSoft else SeatExperienceColors.Card,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (active) SeatExperienceColors.Accent else SeatExperienceColors.Border)
            ) {
                Text(
                    option,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
                    color = SeatExperienceColors.TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun SeatFilterRow(
    filters: List<SeatTag>,
    activeFilters: Set<String>,
    onToggleFilter: (String) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        filters.forEach { filter ->
            val active = activeFilters.contains(filter.id)
            Surface(
                modifier = Modifier.clickable { onToggleFilter(filter.id) },
                shape = RoundedCornerShape(22.dp),
                color = if (active) SeatExperienceColors.AccentSoft else SeatExperienceColors.Card,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (active) SeatExperienceColors.Accent else SeatExperienceColors.Border
                )
            ) {
                Text(
                    filter.label,
                    color = SeatExperienceColors.TextPrimary,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
                )
            }
        }
    }
}

@Composable
fun SeatLegend(legend: List<SeatLegendItem>) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        color = SeatExperienceColors.Card,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SeatExperienceColors.Border)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            legend.forEach {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(12.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(it.color)
                            .border(1.dp, SeatExperienceColors.Border, RoundedCornerShape(4.dp))
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(it.label.uppercase(), color = SeatExperienceColors.TextMuted, fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
fun CabinSectionHeader(title: String, subtitle: String = "") {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(Modifier.weight(1f).height(1.dp).background(SeatExperienceColors.Border))
        Text(
            "$title${if (subtitle.isNotBlank()) " • $subtitle" else ""}",
            color = if (title.contains("BUSINESS")) SeatExperienceColors.Gold else SeatExperienceColors.TextMuted,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 2.sp,
            fontSize = 13.sp
        )
        Box(Modifier.weight(1f).height(1.dp).background(SeatExperienceColors.Border))
    }
}

@Composable
fun SeatGrid(
    seats: List<SeatUiModel>,
    onSeatClick: (SeatUiModel) -> Unit,
    emphasisFilter: (SeatUiModel) -> Boolean
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        maxItemsInEachRow = 4
    ) {
        seats.forEach { seat ->
            SeatCell(
                seat = seat,
                highlighted = emphasisFilter(seat),
                onClick = { onSeatClick(seat) }
            )
        }
    }
}

@Composable
fun EconomyGrid(
    seats: List<SeatUiModel>,
    onSeatClick: (SeatUiModel) -> Unit,
    emphasisFilter: (SeatUiModel) -> Boolean
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        maxItemsInEachRow = 10
    ) {
        seats.forEach { seat ->
            SeatCell(
                seat = seat,
                highlighted = emphasisFilter(seat),
                compact = true,
                onClick = { onSeatClick(seat) }
            )
        }
    }
}

@Composable
private fun SeatCell(
    seat: SeatUiModel,
    highlighted: Boolean,
    compact: Boolean = false,
    onClick: () -> Unit
) {
    val isSelected = seat.state == SeatState.SELECTED
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.04f else 1f,
        animationSpec = spring(dampingRatio = 0.7f),
        label = "seatScale"
    )
    val pulse = rememberInfiniteTransition(label = "seatPulse").animateFloat(
        initialValue = 0.25f,
        targetValue = 0.55f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val background = when (seat.state) {
        SeatState.AVAILABLE -> SeatExperienceColors.CardSoft
        SeatState.SELECTED -> SeatExperienceColors.Accent.copy(alpha = 0.26f)
        SeatState.OCCUPIED -> SeatExperienceColors.Occupied
        SeatState.PREMIUM -> Color(0x443E2C00)
    }

    val border = when (seat.state) {
        SeatState.AVAILABLE -> SeatExperienceColors.Border
        SeatState.SELECTED -> SeatExperienceColors.Accent
        SeatState.OCCUPIED -> SeatExperienceColors.Occupied
        SeatState.PREMIUM -> SeatExperienceColors.Gold.copy(alpha = 0.8f)
    }

    val textColor = when (seat.state) {
        SeatState.OCCUPIED -> Color(0xFF4F5E80)
        SeatState.PREMIUM -> SeatExperienceColors.Gold
        else -> SeatExperienceColors.TextPrimary
    }

    val size = if (compact) 42.dp else 78.dp

    Box(
        modifier = Modifier
            .size(size)
            .shadow(
                elevation = if (isSelected) 26.dp else 0.dp,
                shape = RoundedCornerShape(if (compact) 8.dp else 14.dp),
                spotColor = SeatExperienceColors.Accent.copy(alpha = pulse.value)
            )
            .clip(RoundedCornerShape(if (compact) 8.dp else 14.dp))
            .background(background)
            .border(1.2.dp, border, RoundedCornerShape(if (compact) 8.dp else 14.dp))
            .clickable(enabled = seat.state != SeatState.OCCUPIED, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                seat.label,
                color = textColor,
                fontSize = if (compact) 13.sp else 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
            )
            AnimatedVisibility(
                visible = highlighted && !compact,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    if (seat.features.contains(SeatFeature.EXTRA_LEGROOM)) {
                        Icon(Icons.Rounded.AirlineSeatReclineNormal, null, tint = SeatExperienceColors.Accent, modifier = Modifier.size(14.dp))
                    }
                    if (seat.features.contains(SeatFeature.EXIT_NEARBY)) {
                        Icon(Icons.Rounded.Bolt, null, tint = SeatExperienceColors.Accent, modifier = Modifier.size(14.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SeatSummaryCard(
    selectedSeat: SeatUiModel?,
    onProceed: () -> Unit,
    enabled: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        shape = RoundedCornerShape(32.dp),
        color = SeatExperienceColors.Card,
        border = androidx.compose.foundation.BorderStroke(1.dp, SeatExperienceColors.Border),
        shadowElevation = 20.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = selectedSeat?.label ?: "--",
                        color = SeatExperienceColors.Accent,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp
                    )
                    Surface(shape = RoundedCornerShape(10.dp), color = SeatExperienceColors.AccentSoft) {
                        Text(
                            "PREMIUM",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            color = SeatExperienceColors.Accent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Text("Business Class Pod", color = SeatExperienceColors.TextMuted, fontSize = 14.sp)
                Text("$3,450.00", color = SeatExperienceColors.TextPrimary, fontWeight = FontWeight.Black, fontSize = 28.sp)
            }

            Spacer(Modifier.width(12.dp))

            Button(
                onClick = onProceed,
                enabled = enabled,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SeatExperienceColors.Gold,
                    contentColor = Color(0xFF111111),
                    disabledContainerColor = SeatExperienceColors.Border,
                    disabledContentColor = SeatExperienceColors.TextMuted
                ),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Review", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun SeatBottomSheetBar(
    selectedSeat: SeatUiModel?,
    onProceed: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 14.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        color = SeatExperienceColors.Card.copy(alpha = 0.98f),
        border = androidx.compose.foundation.BorderStroke(1.dp, SeatExperienceColors.Border),
        shadowElevation = 18.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AmenityChip(Icons.Rounded.Bolt, "Direct Aisle Access")
                AmenityChip(Icons.Rounded.KingBed, "Full-Flat Bed")
                AmenityChip(Icons.Rounded.Monitor, "17\" HD Screen")
            }
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = SeatExperienceColors.AccentSoft,
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SeatExperienceColors.Accent)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(selectedSeat?.label ?: "--", color = SeatExperienceColors.Accent, fontWeight = FontWeight.Bold)
                            Icon(Icons.Rounded.Star, null, tint = SeatExperienceColors.Accent, modifier = Modifier.size(12.dp))
                        }
                    }
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text("$3,450.00", color = SeatExperienceColors.TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Black)
                        Text("Business Class Pod", color = SeatExperienceColors.TextMuted, fontSize = 12.sp)
                    }
                }
                Spacer(Modifier.width(12.dp))
                Button(
                    onClick = onProceed,
                    enabled = enabled,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SeatExperienceColors.Gold,
                        contentColor = Color(0xFF111111),
                        disabledContainerColor = SeatExperienceColors.Border,
                        disabledContentColor = SeatExperienceColors.TextMuted
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Proceed", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
                        Spacer(Modifier.width(8.dp))
                        Icon(Icons.AutoMirrored.Rounded.ArrowForward, null)
                    }
                }
            }
        }
    }
}

@Composable
fun ReviewScreenCard(selectedSeat: SeatUiModel?, onConfirm: () -> Unit, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            CircleIconButton(Icons.AutoMirrored.Rounded.ArrowBack, onClick = onBack)
            Text("Review Selection", color = SeatExperienceColors.TextPrimary, fontWeight = FontWeight.Bold, fontSize = 24.sp)
            Spacer(Modifier.size(52.dp))
        }

        Spacer(Modifier.height(20.dp))

        Surface(
            color = SeatExperienceColors.Card,
            shape = RoundedCornerShape(28.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, SeatExperienceColors.Border),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                Text("Selected Seat", color = SeatExperienceColors.TextMuted)
                Spacer(Modifier.height(4.dp))
                Text(selectedSeat?.label ?: "--", color = SeatExperienceColors.Accent, fontSize = 40.sp, fontWeight = FontWeight.Black)
                Text("Business Class • Included Amenities", color = SeatExperienceColors.TextMuted)
                Spacer(Modifier.height(14.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    AmenityChip(Icons.Rounded.Bolt, "Direct Aisle Access")
                    AmenityChip(Icons.Rounded.KingBed, "Full-Flat Bed")
                    AmenityChip(Icons.Rounded.Monitor, "17\" HD Screen")
                }
                Spacer(Modifier.height(20.dp))
                Text("Total Price", color = SeatExperienceColors.TextMuted)
                Text("$3,450.00", color = SeatExperienceColors.Gold, fontSize = 36.sp, fontWeight = FontWeight.Black)
                Spacer(Modifier.height(20.dp))
                PrimaryActionButton(label = "Confirm Selection", onClick = onConfirm, enabled = selectedSeat != null)
            }
        }
    }
}

@Composable
private fun AmenityChip(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF202A3F)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = SeatExperienceColors.TextMuted, modifier = Modifier.size(14.dp))
            Spacer(Modifier.width(6.dp))
            Text(text, color = SeatExperienceColors.TextMuted, fontSize = 12.sp)
        }
    }
}

@Composable
fun PrimaryActionButton(label: String, onClick: () -> Unit, enabled: Boolean = true) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth().height(58.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = SeatExperienceColors.Accent,
            contentColor = Color.White,
            disabledContainerColor = SeatExperienceColors.Border,
            disabledContentColor = SeatExperienceColors.TextMuted
        )
    ) {
        Text(label, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun CheckoutScreen(
    selectedSeat: SeatUiModel?,
    onBack: () -> Unit,
    onProceedToPayment: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            CircleIconButton(Icons.AutoMirrored.Rounded.ArrowBack, onClick = onBack)
            Text("Checkout", color = SeatExperienceColors.TextPrimary, fontWeight = FontWeight.Bold, fontSize = 24.sp)
            Spacer(Modifier.size(52.dp))
        }

        Spacer(Modifier.height(28.dp))

        Surface(
            color = SeatExperienceColors.Card,
            shape = RoundedCornerShape(28.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, SeatExperienceColors.Border),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Rounded.FlightTakeoff, null, tint = SeatExperienceColors.Accent, modifier = Modifier.size(42.dp))
                Spacer(Modifier.height(10.dp))
                Text("DXB → JFK", color = SeatExperienceColors.TextPrimary, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
                Text("Selected seat ${selectedSeat?.label ?: "--"}", color = SeatExperienceColors.TextMuted, fontSize = 16.sp)
                Spacer(Modifier.height(24.dp))
                Text("Total", color = SeatExperienceColors.TextMuted)
                Text("$3,450.00", color = SeatExperienceColors.Gold, fontSize = 40.sp, fontWeight = FontWeight.Black)
                Spacer(Modifier.height(24.dp))
                PrimaryActionButton(
                    label = "Proceed to Payment",
                    onClick = onProceedToPayment,
                    enabled = selectedSeat != null
                )
            }
        }
    }
}

@Composable
fun PaymentScreen(
    selectedSeat: SeatUiModel?,
    onBack: () -> Unit
) {
    var cardholder by remember { mutableStateOf("James Morrison") }
    var cardNumber by remember { mutableStateOf("4455 8899 2233 4455") }
    var expiry by remember { mutableStateOf("MM/YY") }
    var cvv by remember { mutableStateOf("•••") }
    var saveCard by remember { mutableStateOf(true) }

    val scroll = rememberScrollState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scroll)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, null, tint = SeatExperienceColors.TextPrimary)
                    }
                    Spacer(Modifier.width(8.dp))
                    Text("Payment", color = SeatExperienceColors.TextPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0x332B1A05),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x55FF7A00))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Rounded.Lock, null, tint = Color(0xFFFF8A00), modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("SECURE", color = Color(0xFFFF8A00), fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = SeatExperienceColors.Card,
                border = androidx.compose.foundation.BorderStroke(1.dp, SeatExperienceColors.Border),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.fillMaxWidth().padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = RoundedCornerShape(14.dp), color = Color(0x4D8A4600)) {
                            Icon(
                                Icons.Rounded.FlightTakeoff,
                                null,
                                tint = Color(0xFFFF8A00),
                                modifier = Modifier.padding(12.dp).size(22.dp)
                            )
                        }
                        Spacer(Modifier.width(14.dp))
                        Column(Modifier.weight(1f)) {
                            Text("FLIGHT DETAILS", color = SeatExperienceColors.TextMuted, letterSpacing = 1.2.sp, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            Text("DXB to LHR", color = SeatExperienceColors.TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                            Text("Seat ${selectedSeat?.label ?: "2A"}   •   EK-201", color = SeatExperienceColors.TextMuted, fontSize = 13.sp)
                        }
                        Text("First Class", color = Color(0xFFFF8A00), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total Amount", color = SeatExperienceColors.TextMuted, fontSize = 14.sp)
                        Text("$3,450.00", color = SeatExperienceColors.TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Black)
                    }
                }
            }

            Spacer(Modifier.height(18.dp))
            Text("PAYMENT METHOD", color = SeatExperienceColors.TextMuted, letterSpacing = 1.4.sp, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(18.dp),
                    color = SeatExperienceColors.Card,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SeatExperienceColors.Border)
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text("PREMIUM TRAVEL", color = SeatExperienceColors.TextMuted, fontSize = 10.sp)
                                Text("World Elite", color = SeatExperienceColors.TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                            }
                            Row {
                                Box(Modifier.size(24.dp).clip(CircleShape).background(Color(0xFFCC001B)))
                                Box(Modifier.size(24.dp).clip(CircleShape).background(Color(0xFFF79E1B))
                                    .graphicsLayer { translationX = -8f })
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        Text("••••  ••••  ••••  4455", color = SeatExperienceColors.TextPrimary, fontSize = 15.sp, letterSpacing = 1.sp)
                        Spacer(Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("JAMES MORRISON", color = SeatExperienceColors.TextMuted, fontSize = 12.sp)
                            Text("12/28", color = SeatExperienceColors.TextMuted, fontSize = 12.sp)
                        }
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.width(90.dp)) {
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = SeatExperienceColors.Card,
                        border = androidx.compose.foundation.BorderStroke(1.dp, SeatExperienceColors.Border),
                        modifier = Modifier.fillMaxWidth().height(86.dp)
                    ) { Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Apple", color = SeatExperienceColors.TextPrimary) } }
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = SeatExperienceColors.Card,
                        border = androidx.compose.foundation.BorderStroke(1.dp, SeatExperienceColors.Border),
                        modifier = Modifier.fillMaxWidth().height(86.dp)
                    ) { Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Google", color = SeatExperienceColors.TextPrimary) } }
                }
            }

            Spacer(Modifier.height(16.dp))
            PaymentInputField(label = "CARDHOLDER NAME", icon = Icons.Rounded.Person, value = cardholder) { cardholder = it }
            Spacer(Modifier.height(8.dp))
            PaymentInputField(label = "CARD NUMBER", icon = Icons.Rounded.CreditCard, value = cardNumber) { cardNumber = it }
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                PaymentInputField(
                    label = "EXPIRY DATE",
                    icon = Icons.Rounded.CreditCard,
                    value = expiry,
                    modifier = Modifier.weight(1f)
                ) { expiry = it }
                PaymentInputField(
                    label = "CVV",
                    icon = Icons.AutoMirrored.Rounded.Help,
                    value = cvv,
                    modifier = Modifier.weight(1f)
                ) { cvv = it }
            }

            Spacer(Modifier.height(10.dp))
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = SeatExperienceColors.Card,
                border = androidx.compose.foundation.BorderStroke(1.dp, SeatExperienceColors.Border),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Rounded.Lock, null, tint = Color(0xFFFF8A00))
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Save for future trips", color = SeatExperienceColors.TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                        Text("Encrypted and secure storage", color = SeatExperienceColors.TextMuted, fontSize = 12.sp)
                    }
                    Switch(
                        checked = saveCard,
                        onCheckedChange = { saveCard = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFFFF7A00),
                            uncheckedThumbColor = Color(0xFF9CA3AF),
                            uncheckedTrackColor = SeatExperienceColors.Border
                        )
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Rounded.Lock, null, tint = SeatExperienceColors.TextMuted, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text("PCI DSS COMPLIANT", color = SeatExperienceColors.TextMuted, fontSize = 12.sp, letterSpacing = 1.4.sp)
            }
            Spacer(Modifier.weight(1f))
            Spacer(Modifier.height(100.dp))
        }

        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color(0xCC0A0F1A))
                .padding(horizontal = 16.dp, vertical = 10.dp),
            color = Color.Transparent
        ) {
            Column {
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF7A00),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth().height(58.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Confirm & Pay $3,450.00", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(10.dp))
                        Icon(Icons.AutoMirrored.Rounded.ArrowForward, null)
                    }
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    "By clicking Pay Now, you agree to the Terms of Service & Privacy Policy",
                    color = SeatExperienceColors.TextMuted,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun PaymentInputField(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    Column(modifier = modifier) {
        Text(label, color = SeatExperienceColors.TextMuted, fontSize = 12.sp, letterSpacing = 1.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(6.dp))
        Surface(
            color = SeatExperienceColors.Card,
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, SeatExperienceColors.Border),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 13.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icon, null, tint = SeatExperienceColors.TextMuted, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(10.dp))
                Text(
                    text = value,
                    color = SeatExperienceColors.TextPrimary,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
