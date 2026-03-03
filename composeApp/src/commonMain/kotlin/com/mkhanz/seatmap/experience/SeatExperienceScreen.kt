package com.mkhanz.seatmap.experience

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush

private enum class SeatFlowStep { INTRO, LIGHT_PICKER, SECTION, PICKER, REVIEW, CHECKOUT, PAYMENT }

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SeatExperienceFlow() {
    var step by remember { mutableStateOf(SeatFlowStep.INTRO) }
    var seats by remember { mutableStateOf(SeatExperienceData.seats()) }
    var activeFilters by remember { mutableStateOf(setOf<String>()) }
    var selectedSection by remember { mutableStateOf("FRONT") }
    var selectedCabin by remember { mutableStateOf(CabinZone.BUSINESS) }

    val selectedSeat = seats.firstOrNull { it.state == SeatState.SELECTED }

    AnimatedContent(
        targetState = step,
        transitionSpec = {
            (fadeIn(animationSpec = tween(280)) togetherWith fadeOut(animationSpec = tween(220)))
                .using(SizeTransform(clip = false))
        },
        label = "seatFlow"
    ) { currentStep ->
        when (currentStep) {
            SeatFlowStep.INTRO -> IntroStepScreen(
                flight = SeatExperienceData.flight,
                onContinue = { step = SeatFlowStep.LIGHT_PICKER }
            )

            SeatFlowStep.LIGHT_PICKER -> LightSeatSelectionScreen(
                flight = SeatExperienceData.flight,
                seats = seats,
                onSeatSelect = { tapped ->
                    if (tapped.state != SeatState.OCCUPIED) {
                        seats = seats.map {
                            when {
                                it.id == tapped.id -> it.copy(state = SeatState.SELECTED)
                                it.state == SeatState.SELECTED -> it.copy(state = SeatState.AVAILABLE)
                                else -> it
                            }
                        }
                    }
                },
                onBack = { step = SeatFlowStep.INTRO },
                onConfirm = { if (selectedSeat != null) step = SeatFlowStep.SECTION }
            )

            SeatFlowStep.SECTION -> SectionStepScreen(
                flight = SeatExperienceData.flight,
                selectedSection = selectedSection,
                selectedCabin = selectedCabin,
                onSectionChange = { selectedSection = it },
                onCabinChange = { selectedCabin = it },
                onContinue = { step = SeatFlowStep.PICKER },
                onBack = { step = SeatFlowStep.LIGHT_PICKER }
            )

            SeatFlowStep.PICKER -> PickerStepScreen(
                flight = SeatExperienceData.flight,
                seats = seats,
                activeFilters = activeFilters,
                selectedCabin = selectedCabin,
                onToggleFilter = { id ->
                    activeFilters = if (activeFilters.contains(id)) activeFilters - id else activeFilters + id
                },
                onSeatSelect = { tapped ->
                    if (tapped.state != SeatState.OCCUPIED) {
                        seats = seats.map {
                            when {
                                it.id == tapped.id -> it.copy(state = SeatState.SELECTED)
                                it.state == SeatState.SELECTED -> it.copy(state = SeatState.AVAILABLE)
                                else -> it
                            }
                        }
                    }
                },
                onBack = { step = SeatFlowStep.SECTION },
                onReview = { if (selectedSeat != null) step = SeatFlowStep.REVIEW }
            )

            SeatFlowStep.REVIEW -> ReviewStepScreen(
                selectedSeat = selectedSeat,
                onBack = { step = SeatFlowStep.PICKER },
                onConfirm = { step = SeatFlowStep.CHECKOUT }
            )

            SeatFlowStep.CHECKOUT -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(SeatExperienceColors.BackgroundStart, SeatExperienceColors.BackgroundEnd)
                        )
                    )
            ) {
                CheckoutScreen(
                    selectedSeat = selectedSeat,
                    onBack = { step = SeatFlowStep.REVIEW },
                    onProceedToPayment = { step = SeatFlowStep.PAYMENT }
                )
            }

            SeatFlowStep.PAYMENT -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(SeatExperienceColors.BackgroundStart, SeatExperienceColors.BackgroundEnd)
                        )
                    )
            ) {
                PaymentScreen(
                    selectedSeat = selectedSeat,
                    onBack = { step = SeatFlowStep.CHECKOUT }
                )
            }
        }
    }
}
