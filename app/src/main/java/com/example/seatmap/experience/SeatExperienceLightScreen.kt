package com.mkhanz.seatmap.experience

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AirlineSeatReclineNormal
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.FlightTakeoff
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val LightBg = Color(0xFFF3F5F9)
private val LightCard = Color(0xFFFFFFFF)
private val LightBorder = Color(0xFFD6DEE9)
private val LightText = Color(0xFF111B3A)
private val LightMuted = Color(0xFF7284A4)
private val LightBlue = Color(0xFF1A4FCB)
private val LightGold = Color(0xFFC9AB6B)

@Composable
fun LightSeatSelectionScreen(
    flight: FlightContext,
    seats: List<SeatUiModel>,
    onSeatSelect: (SeatUiModel) -> Unit,
    onBack: () -> Unit,
    onConfirm: () -> Unit
) {
    val selectedSeat = seats.firstOrNull { it.state == SeatState.SELECTED }
    val business = seats.filter { it.zone == CabinZone.BUSINESS }
    val economy = seats.filter { it.zone == CabinZone.ECONOMY }.take(30)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.size(30.dp)) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, null, tint = LightText)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("SELECT SEAT", color = LightMuted, letterSpacing = 1.6.sp, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(flight.flightCode, color = LightBlue, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                IconButton(onClick = {}, modifier = Modifier.size(30.dp)) {
                    Icon(Icons.Rounded.Info, null, tint = LightText)
                }
            }

            Text(
                "${flight.origin} → ${flight.destination}",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = LightText,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp
            )
            Text(
                "${flight.dateLabel} • ${flight.aircraftLabel}",
                modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                textAlign = TextAlign.Center,
                color = LightMuted,
                fontSize = 13.sp
            )

            Spacer(Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                LightLegendChip("Available", false, false, Modifier.weight(1f))
                LightLegendChip("Selected", true, false, Modifier.weight(1f))
                LightLegendChip("Occupied", false, true, Modifier.weight(1f))
            }

            Spacer(Modifier.height(14.dp))
            Surface(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                color = LightCard,
                shape = RoundedCornerShape(32.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(96.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.FlightTakeoff, null, tint = Color(0xFFB8C3D7), modifier = Modifier.size(42.dp))
                }
            }

            Spacer(Modifier.height(14.dp))
            LightSectionTitle("BUSINESS CLASS • 1-2-1")
            Spacer(Modifier.height(12.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                maxItemsInEachRow = 4
            ) {
                business.forEach { seat ->
                    LightBusinessSeatCell(seat = seat, onClick = { if (seat.state != SeatState.OCCUPIED) onSeatSelect(seat) })
                }
            }

            Spacer(Modifier.height(14.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFE9EDF4),
                border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("GALLEY & LAVATORY", color = LightMuted, letterSpacing = 1.6.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(Modifier.height(14.dp))
            LightSectionTitle("ECONOMY CLASS • 3-4-3")
            Spacer(Modifier.height(12.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                maxItemsInEachRow = 10
            ) {
                economy.forEach { seat ->
                    LightEconomySeatCell(seat = seat, onClick = { if (seat.state != SeatState.OCCUPIED) onSeatSelect(seat) })
                }
            }

            Spacer(Modifier.height(140.dp))
        }

        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding(),
            color = Color(0xFFF9FAFC),
            border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("SELECTED\nSEAT", color = LightMuted, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, lineHeight = 12.sp)
                        Spacer(Modifier.width(10.dp))
                        Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFE3EBFF)) {
                            Text("INCLUDED", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), color = LightBlue, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(selectedSeat?.label ?: "2A", color = LightText, fontSize = 36.sp, fontWeight = FontWeight.ExtraBold)
                    Text("Business Class", color = LightMuted, fontSize = 15.sp)
                }

                Button(
                    onClick = onConfirm,
                    enabled = selectedSeat != null,
                    modifier = Modifier.width(184.dp).height(92.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LightBlue, contentColor = Color.White)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Confirm\nSelection", textAlign = TextAlign.Center, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(6.dp))
                        Icon(Icons.AutoMirrored.Rounded.ArrowForward, null)
                    }
                }
            }
        }
    }
}

@Composable
private fun LightLegendChip(label: String, selected: Boolean, occupied: Boolean, modifier: Modifier = Modifier) {
    val bg = if (selected) Color(0xFFE3EBFF) else Color(0xFFE9EEF5)
    val dotColor = when {
        selected -> LightBlue
        occupied -> Color(0xFF99A7BF)
        else -> Color(0xFFC3CDD9)
    }
    Surface(modifier = modifier, shape = RoundedCornerShape(24.dp), color = bg, border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDCE4EF))) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(Modifier.size(10.dp).clip(CircleShape).background(dotColor))
            Spacer(Modifier.width(8.dp))
            Text(label, color = if (selected) LightBlue else LightMuted, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun LightSectionTitle(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(Modifier.weight(1f).height(1.dp).background(Color(0xFFD2DBE8)))
        Text(text, color = Color(0xFF8B9CB7), letterSpacing = 1.8.sp, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Box(Modifier.weight(1f).height(1.dp).background(Color(0xFFD2DBE8)))
    }
}

@Composable
private fun LightBusinessSeatCell(seat: SeatUiModel, onClick: () -> Unit) {
    val bg = when (seat.state) {
        SeatState.SELECTED -> LightBlue
        SeatState.OCCUPIED -> Color(0xFFD2D9E5)
        else -> LightCard
    }
    val border = when (seat.state) {
        SeatState.PREMIUM -> LightGold
        else -> LightBorder
    }
    val textColor = when (seat.state) {
        SeatState.SELECTED -> Color.White
        SeatState.OCCUPIED -> Color(0xFF90A0B9)
        SeatState.PREMIUM -> LightGold
        else -> Color(0xFF8B9AB3)
    }

    Box(
        modifier = Modifier
            .size(78.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .border(1.dp, border, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        when (seat.state) {
            SeatState.OCCUPIED -> Icon(Icons.Rounded.Close, null, tint = textColor)
            SeatState.SELECTED -> Icon(Icons.Rounded.AirlineSeatReclineNormal, null, tint = Color.White)
            SeatState.PREMIUM -> Icon(Icons.Rounded.Star, null, tint = LightGold)
            else -> Text(seat.label, color = textColor, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun LightEconomySeatCell(seat: SeatUiModel, onClick: () -> Unit) {
    val bg = when (seat.state) {
        SeatState.OCCUPIED -> Color(0xFFE2E8F1)
        SeatState.SELECTED -> Color(0xFFDDE7FF)
        else -> LightCard
    }
    val border = if (seat.state == SeatState.PREMIUM) LightGold else LightBorder
    val color = when (seat.state) {
        SeatState.OCCUPIED -> Color(0xFFABB8CC)
        SeatState.SELECTED -> LightBlue
        SeatState.PREMIUM -> LightGold
        else -> Color(0xFF8B9AB3)
    }

    Box(
        modifier = Modifier
            .size(width = 46.dp, height = 56.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(bg)
            .border(1.dp, border, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (seat.state == SeatState.OCCUPIED) {
            Icon(Icons.Rounded.Close, null, tint = color, modifier = Modifier.size(16.dp))
        } else {
            Text(seat.label.takeLast(1), color = color, fontWeight = FontWeight.SemiBold)
        }
    }
}
