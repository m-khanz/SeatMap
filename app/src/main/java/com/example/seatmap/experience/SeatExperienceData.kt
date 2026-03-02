package com.mkhanz.seatmap.experience

import androidx.compose.ui.graphics.Color

object SeatExperienceData {
    val flight = FlightContext(
        flightCode = "EK201",
        origin = "DXB",
        destination = "JFK",
        dateLabel = "Oct 24, 2023",
        aircraftLabel = "Boeing 777-300ER"
    )

    val filters = listOf(
        SeatTag(id = "window", label = "Window Only", feature = SeatFeature.WINDOW),
        SeatTag(id = "exit", label = "Near Exit", feature = SeatFeature.EXIT_NEARBY),
        SeatTag(id = "legroom", label = "Extra Legroom", feature = SeatFeature.EXTRA_LEGROOM)
    )

    val legend = listOf(
        SeatLegendItem("Available", SeatState.AVAILABLE, Color(0xFF6B7280)),
        SeatLegendItem("Selected", SeatState.SELECTED, Color(0xFF3B82F6)),
        SeatLegendItem("Booked", SeatState.OCCUPIED, Color(0xFF1F2937)),
        SeatLegendItem("Premium", SeatState.PREMIUM, Color(0xFFE6C34A))
    )

    fun seats(): List<SeatUiModel> {
        val business = listOf(
            seat("1A", CabinZone.BUSINESS, SeatState.AVAILABLE, SeatFeature.WINDOW),
            seat("1K", CabinZone.BUSINESS, SeatState.AVAILABLE, SeatFeature.WINDOW),
            seat("2A", CabinZone.BUSINESS, SeatState.AVAILABLE, SeatFeature.WINDOW, SeatFeature.EXTRA_LEGROOM),
            seat("2B", CabinZone.BUSINESS, SeatState.SELECTED, SeatFeature.EXTRA_LEGROOM),
            seat("2E", CabinZone.BUSINESS, SeatState.PREMIUM, SeatFeature.EXIT_NEARBY),
            seat("2K", CabinZone.BUSINESS, SeatState.OCCUPIED, SeatFeature.WINDOW),
            seat("3A", CabinZone.BUSINESS, SeatState.AVAILABLE, SeatFeature.WINDOW),
            seat("3B", CabinZone.BUSINESS, SeatState.AVAILABLE),
            seat("3E", CabinZone.BUSINESS, SeatState.OCCUPIED),
            seat("3K", CabinZone.BUSINESS, SeatState.AVAILABLE, SeatFeature.WINDOW),
            seat("4A", CabinZone.BUSINESS, SeatState.AVAILABLE, SeatFeature.WINDOW),
            seat("4B", CabinZone.BUSINESS, SeatState.AVAILABLE),
            seat("4E", CabinZone.BUSINESS, SeatState.AVAILABLE),
            seat("4K", CabinZone.BUSINESS, SeatState.AVAILABLE, SeatFeature.WINDOW)
        )

        val economyRows = (10..13).flatMap { row ->
            listOf("A", "B", "C", "D", "E", "F", "G", "H", "J", "K").map { col ->
                val label = "$row$col"
                when {
                    label in setOf("10A", "11B", "12F") ->
                        seat(label, CabinZone.ECONOMY, SeatState.OCCUPIED, SeatFeature.WINDOW)
                    label in setOf("10C", "11E", "13K") ->
                        seat(label, CabinZone.ECONOMY, SeatState.PREMIUM)
                    col in setOf("A", "K") ->
                        seat(label, CabinZone.ECONOMY, SeatState.AVAILABLE, SeatFeature.WINDOW)
                    else ->
                        seat(label, CabinZone.ECONOMY, SeatState.AVAILABLE)
                }
            }
        }

        return business + economyRows
    }

    private fun seat(
        label: String,
        zone: CabinZone,
        state: SeatState,
        vararg features: SeatFeature
    ) = SeatUiModel(
        id = label,
        label = label,
        zone = zone,
        state = state,
        features = features.toSet()
    )
}
