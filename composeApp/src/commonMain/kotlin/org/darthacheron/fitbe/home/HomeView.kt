package org.darthacheron.fitbe.home

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

@Composable
fun CustomCircularProgressIndicator(
    progress: Float, // 0f to 1f
    modifier: Modifier = Modifier,
    size: Dp = 100.dp,
    strokeWidth: Dp = 8.dp,
    label: String = "${(progress * 100).toInt()}%"
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        // Background fill indicator (bottom to top)
//        Canvas(modifier = Modifier.matchParentSize()) {
//            val height = size.toPx()
//            val fillHeight = height * progress
//
//            drawRect(
//                color = Color.LightGray.copy(alpha = 0.3f),
//                topLeft = Offset.Zero,
//                size = Size(size.toPx(), size.toPx())
//            )
//
//            drawRect(
//                color = Color.Cyan,
//                topLeft = Offset(0f, size.toPx() - fillHeight),
//                size = Size(size.toPx(), fillHeight)
//            )
//        }
//        Canvas(modifier = Modifier.matchParentSize()) {
//            val canvasSize = size.toPx()
//            val radius = canvasSize / 2f
//            val center = Offset(radius, radius)
//
//            // Use clipPath to constrain fill to a circle
//            val path = Path().apply {
//                addOval(Rect(center = center, radius = radius))
//            }
//
//            clipPath(path) {
//                val fillHeight = canvasSize * progress
//                drawRect(
//                    color = Color.Cyan,
//                    topLeft = Offset(0f, canvasSize - fillHeight),
//                    size = Size(canvasSize, fillHeight)
//                )
//            }
//        }
        Canvas(modifier = Modifier.matchParentSize()) {
            val totalSize = size.toPx()
            val stroke = strokeWidth.toPx()

            // Shrink the circle to avoid stroke overlap
            val innerDiameter = totalSize - stroke * 2
            val radius = innerDiameter / 2f
            val center = Offset(totalSize / 2f, totalSize / 2f)

            // Create path for inner circle
            val path = Path().apply {
                addOval(Rect(center = center, radius = radius))
            }

            clipPath(path) {
                val fillHeight = innerDiameter * progress

                // Offset drawing area by stroke so it's inside the outer stroke
                drawRect(
                    color = Color.Cyan,
                    topLeft = Offset(stroke, totalSize - stroke - fillHeight),
                    size = Size(innerDiameter, fillHeight)
                )
            }
        }

        // Circular Progress Indicator
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.matchParentSize(),
            color = Color.Blue,
            strokeWidth = strokeWidth,
            trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
            strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
        )

        // Center Text
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}