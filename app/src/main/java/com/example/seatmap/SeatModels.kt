package com.mkhanz.seatmap

/** Represents a single rendered seat in the aircraft layout. */
data class PlaneSeat(
    val id: String,
    val label: String,
    val type: SeatClass,
    val status: SeatStatus,
    val x: Float,
    val y: Float,
    val isExitRow: Boolean = false,
    val legroom: String = "31\"",
    val price: Int = 120
)

enum class SeatClass { BUSINESS, ECONOMY, ECONOMY_PLUS }
enum class SeatStatus { AVAILABLE, OCCUPIED, SELECTED }
