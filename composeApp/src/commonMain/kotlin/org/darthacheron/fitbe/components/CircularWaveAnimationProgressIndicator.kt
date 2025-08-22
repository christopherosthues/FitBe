package org.darthacheron.fitbe.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun CircularWaveAnimationProgressIndicator(
    progress: () -> Float, // 0f to 1f
    modifier: Modifier = Modifier,
    size: Dp = 150.dp,
    strokeWidth: Dp = 8.dp,
    label: String = "${(progress() * 100).toInt()}%"
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave_animation")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "wave_phase"
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val totalSize = size.toPx()
            val stroke = strokeWidth.toPx()
            val innerDiameter = totalSize - stroke * 2
            val radius = innerDiameter / 2f
            val center = Offset(totalSize / 2f, totalSize / 2f)

            // Clip to circle
            val clipPath = Path().apply {
                addOval(Rect(center = center, radius = radius))
            }

            clipPath(clipPath) {
                val waveTop = totalSize - stroke - (innerDiameter * progress())
                val amplitude = 8f
                val wavelength = innerDiameter / 1.5f
                val step = 5f

                val wavePath = Path().apply {
                    moveTo(stroke, totalSize)            // Bottom-left corner
                    lineTo(stroke, waveTop)              // Move up to start of wave

                    var x = stroke
                    while (x <= innerDiameter + stroke) {
                        val y = waveTop + amplitude * sin((x / wavelength) * 2f * PI.toFloat() + phase)
                        lineTo(x, y)
                        x += step
                    }

                    lineTo(innerDiameter + stroke, totalSize) // Bottom-right
                    close()
                }

                drawPath(
                    path = wavePath,
                    color = Color.Cyan
                )
            }
        }

        CircularProgressIndicator(
            progress = progress,
            modifier = Modifier.matchParentSize(),
            color = Color.Blue,
            strokeWidth = strokeWidth,
            trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
            strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
        )

        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}