package org.darthacheron.fitbe.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SegmentedCircularProgressIndicator(
    values: List<Float>,
    colors: List<Color>,
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
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Butt)
            val diameter = this.size.minDimension - strokeWidth.toPx()

            val topLeft = Offset(
                x = (this.size.width - diameter) / 2,
                y = (this.size.height - diameter) / 2
            )

            val totalAngle = 280f
            val startAngle = -230f

            // Background Arc
            drawArc(
                color = backgroundColor,
                startAngle = startAngle,
                sweepAngle = totalAngle,
                useCenter = false,
                topLeft = topLeft,
                size = Size(diameter, diameter),
                style = stroke
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
}
