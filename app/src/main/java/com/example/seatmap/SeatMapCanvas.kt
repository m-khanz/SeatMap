package com.mkhanz.seatmap

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun SeatMapCanvas(
    seats: List<PlaneSeat>,
    selectedSeat: PlaneSeat?,
    scale: Float,
    offset: Offset,
    textMeasurer: TextMeasurer,
    reusablePath: Path,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer {
                translationX = offset.x
                translationY = offset.y
                scaleX = scale
                scaleY = scale
                transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 0f)
            }
    ) {
        val drawCenterX = 0f

        drawSoftWings(drawCenterX, wingY = 2200f)
        drawRefinedFuselage(drawCenterX, length = 5600f, width = 840f)

        seats.forEach { seat ->
            if (seat.isExitRow && seat.label.contains("A")) {
                drawExitDashes(drawCenterX, seat.y - 65f)
            }
            val isSelected = selectedSeat?.id == seat.id
            drawModernSeat(
                centerX = drawCenterX,
                seat = seat,
                isSelected = isSelected,
                textMeasurer = textMeasurer,
                reusablePath = reusablePath
            )
        }
    }
}

fun DrawScope.drawSoftWings(centerX: Float, wingY: Float) {
    val wingSpan = 1700f
    val wingRoot = 380f
    val wingTipY = wingY + 1100f

    val wingPath = Path().apply {
        moveTo(centerX + wingRoot, wingY)
        lineTo(centerX + wingSpan, wingTipY)
        quadraticBezierTo(
            centerX + wingSpan + 50f,
            wingTipY + 200f,
            centerX + wingSpan - 50f,
            wingTipY + 250f
        )
        lineTo(centerX + wingRoot, wingY + 900f)
        moveTo(centerX - wingRoot, wingY)
        lineTo(centerX - wingSpan, wingTipY)
        quadraticBezierTo(
            centerX - wingSpan - 50f,
            wingTipY + 200f,
            centerX - wingSpan + 50f,
            wingTipY + 250f
        )
        lineTo(centerX - wingRoot, wingY + 900f)
    }

    drawPath(wingPath, Color.White, style = Fill)
    drawPath(
        wingPath,
        brush = Brush.linearGradient(
            colors = listOf(Color(0xFF2E1065), Color(0xFF3B82F6)),
            start = Offset(centerX, wingY),
            end = Offset(centerX + wingSpan, wingTipY)
        ),
        style = Stroke(width = 3f)
    )
}

fun DrawScope.drawRefinedFuselage(centerX: Float, length: Float, width: Float) {
    val noseHeight = 450f
    val taperStart = length - 300f

    val fuselagePath = Path().apply {
        moveTo(centerX, -noseHeight)
        cubicTo(
            centerX + width * 0.5f,
            -noseHeight,
            centerX + width / 2,
            0f,
            centerX + width / 2,
            600f
        )
        lineTo(centerX + width / 2, taperStart)
        cubicTo(
            centerX + width / 2,
            length,
            centerX + width / 6,
            length + 150f,
            centerX,
            length + 150f
        )
        cubicTo(
            centerX - width / 6,
            length + 150f,
            centerX - width / 2,
            length,
            centerX - width / 2,
            taperStart
        )
        lineTo(centerX - width / 2, 600f)
        cubicTo(centerX - width / 2, 0f, centerX - width * 0.5f, -noseHeight, centerX, -noseHeight)
    }

    drawPath(
        fuselagePath,
        Color.Black.copy(alpha = 0.05f),
        style = Stroke(width = 60f, cap = StrokeCap.Round)
    )
    drawPath(fuselagePath, Color.White, style = Fill)
    drawPath(
        fuselagePath,
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFF312E81),
                Color(0xFFA78BFA).copy(alpha = 0.5f),
                Color(0xFF312E81)
            ),
            start = Offset(centerX, -noseHeight),
            end = Offset(centerX, length)
        ),
        style = Stroke(width = 5f)
    )

    val windowPath = Path().apply {
        moveTo(centerX - 10f, -noseHeight + 120f)
        lineTo(centerX - 90f, -noseHeight + 140f)
        quadraticBezierTo(centerX - 120f, -noseHeight + 180f, centerX - 100f, -noseHeight + 210f)
        lineTo(centerX - 15f, -noseHeight + 170f)
        close()
        moveTo(centerX + 10f, -noseHeight + 120f)
        lineTo(centerX + 90f, -noseHeight + 140f)
        quadraticBezierTo(centerX + 120f, -noseHeight + 180f, centerX + 100f, -noseHeight + 210f)
        lineTo(centerX + 15f, -noseHeight + 170f)
        close()
    }
    drawPath(
        windowPath,
        Brush.linearGradient(
            listOf(Color(0xFF020617), Color(0xFF0EA5E9), Color(0xFF0F172A)),
            start = Offset(centerX - 100f, -noseHeight + 100f),
            end = Offset(centerX + 100f, -noseHeight + 220f)
        ),
        style = Fill
    )

    val glintPath = Path().apply {
        moveTo(centerX + 20f, -noseHeight + 130f)
        quadraticBezierTo(centerX + 60f, -noseHeight + 135f, centerX + 80f, -noseHeight + 150f)
        quadraticBezierTo(centerX + 50f, -noseHeight + 145f, centerX + 20f, -noseHeight + 130f)
        moveTo(centerX - 20f, -noseHeight + 130f)
        quadraticBezierTo(centerX - 60f, -noseHeight + 135f, centerX - 80f, -noseHeight + 150f)
        quadraticBezierTo(centerX - 50f, -noseHeight + 145f, centerX - 20f, -noseHeight + 130f)
    }
    drawPath(glintPath, Color.White.copy(alpha = 0.7f), style = Fill)
    drawArc(
        Color(0xFFCBD5E1),
        0f,
        180f,
        false,
        Offset(centerX - 40f, -noseHeight + 30f),
        Size(80f, 40f),
        style = Stroke(width = 2f)
    )
}

fun DrawScope.drawExitDashes(centerX: Float, y: Float) {
    drawLine(
        brush = Brush.horizontalGradient(
            colors = listOf(Color(0xFFFF6B6B), Color(0xFFFF8F8F), Color(0xFFFF6B6B))
        ),
        start = Offset(centerX - 350f, y),
        end = Offset(centerX + 350f, y),
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f)),
        strokeWidth = 3f
    )
}

fun DrawScope.drawModernSeat(
    centerX: Float,
    seat: PlaneSeat,
    isSelected: Boolean,
    textMeasurer: TextMeasurer,
    reusablePath: Path
) {
    val width = 95f
    val height = 105f
    val absX = centerX + seat.x
    val absY = seat.y
    val cornerRadius = CornerRadius(22f)

    if (seat.status == SeatStatus.OCCUPIED) {
        drawRoundRect(Color(0xFFF1F5F9), Offset(absX, absY), Size(width, height), cornerRadius)
        clipRect(left = absX, top = absY, right = absX + width, bottom = absY + height) {
            drawLine(
                Color(0xFFCBD5E1),
                Offset(absX + 20f, absY + height - 20f),
                Offset(absX + width - 20f, absY + 20f),
                strokeWidth = 3f
            )
        }
    } else {
        if (isSelected) {
            drawRoundRect(
                Brush.linearGradient(
                    listOf(Color(0xFF2E1065), Color(0xFF3B82F6)),
                    start = Offset(absX, absY),
                    end = Offset(absX + width, absY + height)
                ),
                topLeft = Offset(absX, absY),
                size = Size(width, height),
                cornerRadius = cornerRadius
            )
        } else {
            drawRoundRect(Color.White, Offset(absX, absY), Size(width, height), cornerRadius)
            drawRoundRect(
                Brush.verticalGradient(
                    colors = if (seat.type == SeatClass.ECONOMY_PLUS) {
                        listOf(Color(0xFF6366F1), Color(0xFF8B5CF6))
                    } else {
                        listOf(Color(0xFFCBD5E1), Color(0xFF94A3B8))
                    }
                ),
                topLeft = Offset(absX, absY),
                size = Size(width, height),
                cornerRadius = cornerRadius,
                style = Stroke(width = if (seat.type == SeatClass.ECONOMY_PLUS) 3f else 2f)
            )
        }

        reusablePath.rewind()
        reusablePath.moveTo(absX + 16f, absY + 34f)
        reusablePath.quadraticBezierTo(absX + width / 2, absY + 24f, absX + width - 16f, absY + 34f)

        if (!isSelected) {
            drawPath(reusablePath, Color(0xFFCBD5E1), style = Stroke(width = 2f))
        } else {
            drawPath(reusablePath, Color.White.copy(alpha = 0.4f), style = Stroke(width = 2f))
        }

        val textResult = textMeasurer.measure(
            text = seat.label,
            style = TextStyle(
                color = if (isSelected) Color.White else Color(0xFF334155),
                fontWeight = if (isSelected) FontWeight.Black else FontWeight.SemiBold,
                fontSize = 14.sp
            )
        )
        drawText(
            textResult,
            topLeft = Offset(
                absX + width / 2 - textResult.size.width / 2,
                (absY + height * 0.62f) - textResult.size.height / 2
            )
        )
    }
}
