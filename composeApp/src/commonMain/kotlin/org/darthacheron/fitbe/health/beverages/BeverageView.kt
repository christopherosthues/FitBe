package org.darthacheron.fitbe.health.beverages

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.add_beverage
import org.darthacheron.fitbe.components.DropdownSelection
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.PI
import kotlin.math.sin

@Preview
@Composable
fun BeverageView(modifier: Modifier, beverageViewModel: BeverageViewModel) {
    val beverages by beverageViewModel.todayIntake.collectAsState()
    val total = beverages.sumOf { it.unit.toMilliliter(it.amount) }
    val goal = 2000

    var showDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        stickyHeader {
            Column(
                modifier = Modifier.padding(top = 100.dp, start = 16.dp, end = 16.dp, bottom = 16.dp).fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomCircularProgressIndicator(progress = { total / goal.toFloat() })

                TextButton(
                    onClick = { showDialog = true },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.White,
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = stringResource(Res.string.add_beverage))
                }
            }
        }
        beverages.forEach {
            item {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    Icon(
                        painterResource(it.unit.iconResource()),
                        contentDescription = null,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                        Text(text = it.localizedString())
                }
            }
        }
    }

    if (showDialog) {
        AddBeverageDialog(
            initialAmount = 0,
            onSet = { amount, unit, beverage ->
                beverageViewModel.addBeverage(amount, unit, beverage)
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
fun CustomCircularProgressIndicator(
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

@Composable
fun AddBeverageDialog(
    initialAmount: Int?,
    onSet: (Int, FluidUnit, String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedUnit by remember { mutableStateOf(FluidUnit.Milliliter) }
    var amountText by remember { mutableStateOf((initialAmount ?: "").toString()) }
    var beverage by remember { mutableStateOf("") }
    val unitOptions = FluidUnit.entries

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set Daily Beverage Intake") },
        text = {
            Column {
                TextField(value = beverage, onValueChange = { beverage = it })

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )

                Spacer(Modifier.height(8.dp))

                DropdownSelection(
                    initialState = false,
                    items = unitOptions,
                    title = "Choose a unit",
                    itemContent = { item, onClick ->
                        DropdownMenuItem(
                            text = {
                                Text(item.localizedString(amountText.toIntOrNull() ?: 0))
                            },
                            leadingIcon = { Icon(painterResource(item.iconResource()), contentDescription = null) },
                            onClick = onClick
                        )
                    },
                    itemToString = {
                        it.localizedString(amountText.toIntOrNull() ?: 0)
                    },
                    onItemSelected = {
                        selectedUnit = unitOptions[it]
                    }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSet(amountText.toInt(), selectedUnit, beverage)
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
