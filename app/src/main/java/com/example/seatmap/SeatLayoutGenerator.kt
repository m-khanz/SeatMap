package com.mkhanz.seatmap

import kotlin.random.Random

/**
 * Generates the deterministic seat grid used by the canvas.
 *
 * The fixed seed keeps occupied/available placement stable across recompositions,
 * preserving a consistent UI and predictable interactions.
 */
fun generatePreciseLayout(): List<PlaneSeat> {
    val list = mutableListOf<PlaneSeat>()
    var yCursor = 180f
    val random = Random(42)
    val rightX = listOf(30f, 135f, 240f)
    val leftX = listOf(-345f, -240f, -135f)
    val allX = leftX + rightX
    val labels = listOf("A", "B", "C", "D", "E", "F")

    for (row in 5..40) {
        val isExit = row == 16 || row == 17
        if (isExit) yCursor += 100f
        allX.forEachIndexed { index, x ->
            val status = if (random.nextFloat() > 0.7f) SeatStatus.OCCUPIED else SeatStatus.AVAILABLE
            val legroom = if (isExit) "36\"" else "31\""
            val price = if (isExit) 165 else 120
            val type = if (isExit || row < 8) SeatClass.ECONOMY_PLUS else SeatClass.ECONOMY
            val finalPrice = if (type == SeatClass.ECONOMY_PLUS) price + 30 else price
            list.add(
                PlaneSeat(
                    id = "$row-${labels[index]}",
                    label = "${row}${labels[index]}",
                    type = type,
                    status = status,
                    x = x,
                    y = yCursor,
                    isExitRow = isExit,
                    legroom = legroom,
                    price = finalPrice
                )
            )
        }
        yCursor += 140f
    }
    return list
}
