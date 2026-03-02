package com.mkhanz.seatmap.experience

import androidx.compose.ui.graphics.Color

enum class CabinZone { BUSINESS, ECONOMY }

enum class SeatState { AVAILABLE, SELECTED, OCCUPIED, PREMIUM }

enum class SeatFeature { WINDOW, EXIT_NEARBY, EXTRA_LEGROOM }

data class SeatUiModel(
    val id: String,
    val label: String,
    val zone: CabinZone,
    val state: SeatState,
    val features: Set<SeatFeature> = emptySet()
)

data class SeatLegendItem(
    val label: String,
    val state: SeatState,
    val color: Color
)

data class SeatTag(
    val id: String,
    val label: String,
    val feature: SeatFeature
)

data class FlightContext(
    val flightCode: String,
    val origin: String,
    val destination: String,
    val dateLabel: String,
    val aircraftLabel: String
)
