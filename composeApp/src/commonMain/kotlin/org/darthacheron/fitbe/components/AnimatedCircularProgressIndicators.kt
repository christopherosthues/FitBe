package org.darthacheron.fitbe.components

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedSegmentedCircularProgressIndicator(
    values: List<Float>,
    colors: List<Color>,
    centerColor: Color = MaterialTheme.colorScheme.primary,
    centerText: String,
    modifier: Modifier = Modifier,
    bottomText: String? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
    size: Dp = 150.dp,
    strokeWidth: Dp = 8.dp,
    contentDescription: String? = null
) {
    val animatedValues = values.map {
        animateFloatAsState(targetValue = it, animationSpec = tween(durationMillis = 1000)).value
    }
    val animatedSumOfValues = animatedValues.sum()

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(size).padding(all = 16.dp)
    ) {
        AnimatedCenterBackground(size, centerColor)

        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            val diameter = this.size.minDimension - strokeWidth.toPx()

            val topLeft = Offset(
                x = (this.size.width - diameter) / 2,
                y = (this.size.height - diameter) / 2
            )

            val totalAngle = 280f
            val startAngle = -230f

            // Background Arc
            drawBackgroundProgressArc(
                backgroundColor = backgroundColor,
                startAngle = startAngle,
                sweepAngle = totalAngle,
                topLeft = topLeft,
                diameter = diameter,
                stroke = stroke
            )

            if (animatedSumOfValues > 0) {
                var currentStartAngle = startAngle
                animatedValues.forEachIndexed { index, value ->
                    val sweepAngle = (value / animatedSumOfValues) * totalAngle
                    drawArc(
                        color = colors[index],
                        startAngle = currentStartAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        topLeft = topLeft,
                        size = Size(diameter, diameter),
                        style = stroke
                    )
                    currentStartAngle += sweepAngle
                }
            }
        }

        ProgressText(centerText, size, strokeWidth, contentDescription, bottomText)
    }
}

@Composable
fun AnimatedSemiCircularProgressIndicator(
    progress: Float,
    centerText: String,
    modifier: Modifier = Modifier,
    bottomText: String? = null,
    color: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
    size: Dp = 150.dp,
    strokeWidth: Dp = 8.dp,
    contentDescription: String? = null
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000)
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(size).padding(all = 16.dp)
    ) {
        AnimatedCenterBackground(size, color)

        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            val diameter = this.size.minDimension - strokeWidth.toPx()

            val topLeft = Offset(
                x = (this.size.width - diameter) / 2,
                y = (this.size.height - diameter) / 2
            )

            val startAngle = -230f
            val sweepAngle = 280f

            // Background Arc
            drawBackgroundProgressArc(
                backgroundColor = backgroundColor,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                topLeft = topLeft,
                diameter = diameter,
                stroke = stroke
            )

            // Progress Arc
            drawBackgroundProgressArc(
                backgroundColor = color,
                startAngle = startAngle,
                sweepAngle = 280 * animatedProgress.coerceIn(0f, 1f),
                topLeft = topLeft,
                diameter = diameter,
                stroke = stroke
            )
        }

        ProgressText(centerText, size, strokeWidth, contentDescription, bottomText)
    }
}

private fun DrawScope.drawBackgroundProgressArc(
    backgroundColor: Color,
    startAngle: Float,
    sweepAngle: Float,
    topLeft: Offset,
    diameter: Float,
    stroke: Stroke
) {
    drawArc(
        color = backgroundColor,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        topLeft = topLeft,
        size = Size(diameter, diameter),
        style = stroke
    )
}

@Composable
private fun AnimatedCenterBackground(size: Dp, color: Color) {
    val infiniteTransition = rememberInfiniteTransition()
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f, // Animate from 0 to 1
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .blur(20.dp)
            .drawWithCache {
                val brushSize = size.toPx()
                val brush = Brush.radialGradient(
                    colors = listOf(color.copy(alpha = 0.3f), Color.Transparent),
                    center = Offset(
                        offset * brushSize,
                        offset * brushSize
                    ), // Move center based on animation
                    radius = brushSize,
                    tileMode = TileMode.Mirror
                )
                onDrawBehind {
                    drawRect(brush = brush)
                }
            }
    )
}

@Composable
private fun ProgressText(
    centerText: String,
    size: Dp,
    strokeWidth: Dp,
    contentDescription: String?,
    bottomText: String?
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = centerText,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .width(size - strokeWidth - strokeWidth)
                    .semantics {
                        if (contentDescription != null) {
                            this.contentDescription = contentDescription
                        }
                    }
        )
        bottomText?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier =
                    Modifier
                        .width(size - strokeWidth - strokeWidth - 16.dp)
                        .clearAndSetSemantics { }
            )
        }
    }
}
