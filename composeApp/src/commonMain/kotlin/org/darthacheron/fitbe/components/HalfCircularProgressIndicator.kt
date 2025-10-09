package org.darthacheron.fitbe.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun HalfCircleProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = Color.Blue,
    backgroundColor: Color = Color.LightGray,
    strokeWidth: Dp = 12.dp
) {
    Canvas(modifier = modifier) {
        val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        val arcRadius = size.minDimension / 2

        val topLeft =
            Offset(
                (size.width - arcRadius * 2) / 2,
                (size.height - arcRadius) / 2
            )

        // Draw background arc (semi-circle)
        drawArc(
            color = backgroundColor,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = topLeft,
            size = Size(arcRadius * 2, arcRadius),
            style = stroke
        )

        // Draw progress arc (semi-circle)
        drawArc(
            color = color,
            startAngle = 180f,
            sweepAngle = 180f * progress.coerceIn(0f, 1f),
            useCenter = false,
            topLeft = topLeft,
            size = Size(arcRadius * 2, arcRadius),
            style = stroke
        )
    }
}