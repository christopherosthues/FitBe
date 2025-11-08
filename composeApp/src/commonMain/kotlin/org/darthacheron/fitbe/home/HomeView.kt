package org.darthacheron.fitbe.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.darthacheron.fitbe.home.summary.BeveragesSummary
import org.darthacheron.fitbe.home.summary.BodyWeightSummary
import org.darthacheron.fitbe.home.summary.StepsSummary

@Composable
fun HomeView(homeViewModel: HomeViewModel) {
    LaunchedEffect(Unit) {
        homeViewModel.updateTopBarConfig()
    }
    val pagerState = rememberPagerState(pageCount = { 4 })
    val coroutineScope = rememberCoroutineScope()


    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                when (page) {
                    0 -> BeveragesSummary()
                    1 -> StepsSummary()
                    2 -> BodyWeightSummary()
                    3 -> Text(text = "And another $page")
//                    1 -> SleepSummary()
//                    2 -> StepsSummary()
//                    3 -> BodyWeightSummary()
                }
            }

            PagerIndicator(
                pageCount = pagerState.pageCount,
                currentPageIndex = pagerState.currentPage,
                onIndicatorClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(page = it)
                    }
                }
            )
        }
    }
}

@Composable
fun PagerIndicator(pageCount: Int, currentPageIndex: Int, modifier: Modifier = Modifier, onIndicatorClick : (Int) -> Unit = {}) {
    Box(modifier = Modifier.wrapContentHeight()) {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pageCount) { iteration ->
                val color = if (currentPageIndex == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                Box(
                    modifier = modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(16.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onIndicatorClick(iteration)
                        }
                )
            }
        }
    }
}
