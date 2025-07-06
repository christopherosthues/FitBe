package org.darthacheron.fitbe.home

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.serialization.Serializable
import org.darthacheron.fitbe.dependencies.MyViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.PI
import kotlin.math.sin

@Serializable
object Home

@Composable
@Preview
fun HomeView(navController: NavController) {
    val viewModel = koinViewModel<MyViewModel>()
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(text = viewModel.getHelloWorldString())
//    }
    var progress by remember { mutableStateOf(0.6f) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomCircularProgressIndicator(progress = progress)

        Spacer(modifier = Modifier.height(16.dp))

        Slider(
            value = progress,
            onValueChange = { progress = it },
            Modifier.padding(5.dp)
        )
    }
}

//@Composable
//fun CustomCircularProgressIndicator(
//    progress: Float, // 0f to 1f
//    modifier: Modifier = Modifier,
//    size: Dp = 100.dp,
//    strokeWidth: Dp = 8.dp,
//    label: String = "${(progress * 100).toInt()}%"
//) {
//    Box(
//        modifier = modifier.size(size),
//        contentAlignment = Alignment.Center
//    ) {
//        // Background fill indicator (bottom to top)
////        Canvas(modifier = Modifier.matchParentSize()) {
////            val height = size.toPx()
////            val fillHeight = height * progress
////
////            drawRect(
////                color = Color.LightGray.copy(alpha = 0.3f),
////                topLeft = Offset.Zero,
////                size = Size(size.toPx(), size.toPx())
////            )
////
////            drawRect(
////                color = Color.Cyan,
////                topLeft = Offset(0f, size.toPx() - fillHeight),
////                size = Size(size.toPx(), fillHeight)
////            )
////        }
////        Canvas(modifier = Modifier.matchParentSize()) {
////            val canvasSize = size.toPx()
////            val radius = canvasSize / 2f
////            val center = Offset(radius, radius)
////
////            // Use clipPath to constrain fill to a circle
////            val path = Path().apply {
////                addOval(Rect(center = center, radius = radius))
////            }
////
////            clipPath(path) {
////                val fillHeight = canvasSize * progress
////                drawRect(
////                    color = Color.Cyan,
////                    topLeft = Offset(0f, canvasSize - fillHeight),
////                    size = Size(canvasSize, fillHeight)
////                )
////            }
////        }
//        Canvas(modifier = Modifier.matchParentSize()) {
//            val totalSize = size.toPx()
//            val stroke = strokeWidth.toPx()
//
//            // Shrink the circle to avoid stroke overlap
//            val innerDiameter = totalSize - stroke * 2
//            val radius = innerDiameter / 2f
//            val center = Offset(totalSize / 2f, totalSize / 2f)
//
//            // Create path for inner circle
//            val path = Path().apply {
//                addOval(Rect(center = center, radius = radius))
//            }
//
//            clipPath(path) {
//                val fillHeight = innerDiameter * progress
//
//                // Offset drawing area by stroke so it's inside the outer stroke
//                drawRect(
//                    color = Color.Cyan,
//                    topLeft = Offset(stroke, totalSize - stroke - fillHeight),
//                    size = Size(innerDiameter, fillHeight)
//                )
//            }
//        }
//
//        // Circular Progress Indicator
//        CircularProgressIndicator(
//            progress = { progress },
//            modifier = Modifier.matchParentSize(),
//            color = Color.Blue,
//            strokeWidth = strokeWidth,
//            trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
//            strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
//        )
//
//        // Center Text
//        Text(
//            text = label,
//            style = MaterialTheme.typography.bodyMedium,
//            color = Color.Black,
//            fontWeight = FontWeight.Bold
//        )
//    }
//}

//@Composable
//fun CustomCircularProgressIndicator(
//    progress: Float, // 0f to 1f
//    modifier: Modifier = Modifier,
//    size: Dp = 100.dp,
//    strokeWidth: Dp = 8.dp,
//    label: String = "${(progress * 100).toInt()}%"
//) {
//    val infiniteTransition = rememberInfiniteTransition(label = "wave_anim")
//    val phase by infiniteTransition.animateFloat(
//        initialValue = 0f,
//        targetValue = 2 * PI.toFloat(),
//        animationSpec = infiniteRepeatable(
//            animation = tween(durationMillis = 2000, easing = LinearEasing),
//            repeatMode = RepeatMode.Restart
//        ), label = "wave_phase"
//    )
//
//    Box(
//        modifier = modifier.size(size),
//        contentAlignment = Alignment.Center
//    ) {
//        Canvas(modifier = Modifier.matchParentSize()) {
//            val totalSize = size.toPx()
//            val stroke = strokeWidth.toPx()
//            val innerDiameter = totalSize - stroke * 2
//            val radius = innerDiameter / 2f
//            val center = Offset(totalSize / 2f, totalSize / 2f)
//
//            val circlePath = Path().apply {
//                addOval(Rect(center = center, radius = radius))
//            }
//
//            clipPath(circlePath) {
//                val fillHeight = innerDiameter * progress
//                val topY = totalSize - stroke - fillHeight
//
//                // Wave parameters
//                val waveLength = innerDiameter / 1.5f
//                val amplitude = 6f
//                val waveCount = (innerDiameter / waveLength).toInt() + 1
//
//                val wavePath = Path().apply {
//                    moveTo(stroke, totalSize)
//                    lineTo(stroke, topY)
//
//                    // Create wave along top edge
//                    for (i in 0..waveCount) {
//                        val x = stroke + i * waveLength
//                        val y = topY + amplitude * sin((x / waveLength) * 2 * PI.toFloat() + phase)
//                        lineTo(x, y)
//                    }
//
//                    lineTo(innerDiameter + stroke, totalSize)
//                    close()
//                }
//
//                drawPath(
//                    path = wavePath,
//                    color = Color.Cyan
//                )
//            }
//        }
//
//        CircularProgressIndicator(
//            progress = { progress },
//            modifier = Modifier.matchParentSize(),
//            color = Color.Blue,
//            strokeWidth = strokeWidth,
//            trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
//            strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
//        )
//
//        Text(
//            text = label,
//            style = MaterialTheme.typography.bodyMedium,
//            color = Color.Black,
//            fontWeight = FontWeight.Bold
//        )
//    }
//}
////
////🔍 Key Notes:
////
////The wavePath creates a sine-wave shape on top of the fill rectangle.
////
////infiniteTransition animates the wave phase continuously.
////
////The wave is constrained inside the circular path using clipPath.
////
////✅ Optional Tweaks
////
////You can tune these for different styles:
////
////amplitude = 6f – how tall the wave is.
////
////waveLength = innerDiameter / 1.5f – how wide each wave is.
////
////Adjust tween duration for faster/slower wave motion.
////
////Let me know if you'd like a smoother wave (Bezier curves) or gradient effects too!
@Composable
fun CustomCircularProgressIndicator(
    progress: Float, // 0f to 1f
    modifier: Modifier = Modifier,
    size: Dp = 100.dp,
    strokeWidth: Dp = 8.dp,
    label: String = "${(progress * 100).toInt()}%"
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
                val waveTop = totalSize - stroke - (innerDiameter * progress)
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
            progress = { progress },
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

//✅ Visual Output
//
//This creates a filled inner surface with a wavy top, animated like fluid inside a circle.
//🔧 Parameters You Can Tune
//Parameter	Effect
//amplitude	Height of the wave peaks
//wavelength	Distance between wave crests
//step	Resolution of wave drawing (smaller = smoother)
//progress	How full the wave should rise
//
//Would you like to:
//
//Add a second wave layer with a different speed (to mimic water interference)?
//
//Apply a gradient to the wave fill?
//
//Let me know — we can keep evolving this!