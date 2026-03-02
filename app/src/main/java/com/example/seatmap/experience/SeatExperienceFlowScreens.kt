package com.mkhanz.seatmap.experience

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp

@Composable
fun IntroStepScreen(flight: FlightContext, onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(SeatExperienceColors.BackgroundStart, SeatExperienceColors.BackgroundEnd)))
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Spacer(Modifier.height(18.dp))
        FlightHeroHeader(flight)
        AircraftHeroCard(sectionLabel = "BUSINESS CLASS")
        SeatLegend(SeatExperienceData.legend)
        Spacer(Modifier.height(6.dp))
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            PrimaryActionButton(label = "Start Seat Selection", onClick = onContinue)
        }
        Spacer(Modifier.height(10.dp))
    }
}

@Composable
fun SectionStepScreen(
    flight: FlightContext,
    selectedSection: String,
    selectedCabin: CabinZone,
    onSectionChange: (String) -> Unit,
    onCabinChange: (CabinZone) -> Unit,
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(SeatExperienceColors.BackgroundStart, SeatExperienceColors.BackgroundEnd)))
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SeatTopBar(route = "${flight.origin}  →  ${flight.destination}", onBack = onBack)
        AircraftHeroCard(sectionLabel = "SECTION: $selectedSection")
        SectionPicker(
            options = listOf("FRONT", "MID SECTION", "REAR"),
            selected = selectedSection,
            onSelect = onSectionChange
        )
        SectionPicker(
            options = listOf("BUSINESS CLASS", "ECONOMY CLASS"),
            selected = if (selectedCabin == CabinZone.BUSINESS) "BUSINESS CLASS" else "ECONOMY CLASS",
            onSelect = { option -> onCabinChange(if (option.startsWith("BUSINESS")) CabinZone.BUSINESS else CabinZone.ECONOMY) }
        )
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            PrimaryActionButton(label = "Open Seat Map", onClick = onContinue)
        }
    }
}

@Composable
fun PickerStepScreen(
    flight: FlightContext,
    seats: List<SeatUiModel>,
    activeFilters: Set<String>,
    selectedCabin: CabinZone,
    onToggleFilter: (String) -> Unit,
    onSeatSelect: (SeatUiModel) -> Unit,
    onBack: () -> Unit,
    onReview: () -> Unit
) {
    val selectedSeat = seats.firstOrNull { it.state == SeatState.SELECTED }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(SeatExperienceColors.BackgroundStart, SeatExperienceColors.BackgroundEnd)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SeatMapHeaderStrip(
                flight = flight,
                cabinLabel = if (selectedCabin == CabinZone.BUSINESS) "BUSINESS CLASS" else "ECONOMY CLASS",
                onBack = onBack
            )
            AircraftHeroCard(sectionLabel = if (selectedCabin == CabinZone.BUSINESS) "SECTION: FORWARD BUSINESS CABIN" else "SECTION: MAIN ECONOMY CABIN")
            SeatFilterRow(filters = SeatExperienceData.filters, activeFilters = activeFilters, onToggleFilter = onToggleFilter)
            SeatLegend(SeatExperienceData.legend.filter { it.state != SeatState.PREMIUM })

            if (selectedCabin == CabinZone.BUSINESS) {
                CabinSectionHeader("BUSINESS CLASS", "1-2-1")
                SeatGrid(
                    seats = seats.filter { it.zone == CabinZone.BUSINESS },
                    onSeatClick = onSeatSelect,
                    emphasisFilter = { seat -> matchesFilter(activeFilters, seat) }
                )
                Spacer(Modifier.height(10.dp))
                CabinSectionHeader("MAIN CABIN")
                EconomyGrid(
                    seats = seats.filter { it.zone == CabinZone.ECONOMY }.take(20),
                    onSeatClick = onSeatSelect,
                    emphasisFilter = { seat -> matchesFilter(activeFilters, seat) }
                )
            } else {
                CabinSectionHeader("ECONOMY CLASS", "3-4-3")
                EconomyGrid(
                    seats = seats.filter { it.zone == CabinZone.ECONOMY },
                    onSeatClick = onSeatSelect,
                    emphasisFilter = { seat -> matchesFilter(activeFilters, seat) }
                )
            }

            Spacer(Modifier.height(190.dp))
        }

        SeatBottomSheetBar(
            selectedSeat = selectedSeat,
            onProceed = onReview,
            enabled = selectedSeat != null,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun ReviewStepScreen(selectedSeat: SeatUiModel?, onBack: () -> Unit, onConfirm: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(SeatExperienceColors.BackgroundStart, SeatExperienceColors.BackgroundEnd)))
            .navigationBarsPadding()
    ) {
        ReviewScreenCard(selectedSeat = selectedSeat, onConfirm = onConfirm, onBack = onBack)
    }
}

private fun matchesFilter(activeFilters: Set<String>, seat: SeatUiModel): Boolean {
    if (activeFilters.isEmpty()) return true
    return activeFilters.any { filterId ->
        when (filterId) {
            "window" -> seat.features.contains(SeatFeature.WINDOW)
            "exit" -> seat.features.contains(SeatFeature.EXIT_NEARBY)
            "legroom" -> seat.features.contains(SeatFeature.EXTRA_LEGROOM)
            else -> false
        }
    }
}
