package org.darthacheron.fitbe.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.darthacheron.fitbe.components.HalfCircleProgressIndicator

@Composable
fun HomeView(homeViewModel: HomeViewModel) {
    LaunchedEffect(Unit) {
        homeViewModel.updateTopBarConfig()
    }
    val pagerState = rememberPagerState(pageCount = { 1 })

    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth()
            ) {
                val targetBeverage by homeViewModel.targetBeverage.collectAsState()
                val totalBeverage by homeViewModel.totalBeverage.collectAsState()

                val progress = if (targetBeverage != null && targetBeverage!! > 0) {
                    (totalBeverage.toFloat() / targetBeverage!!).coerceIn(0f, 1f)
                } else {
                    1f
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    HalfCircleProgressIndicator(
                        progress = progress,
                        modifier = Modifier.fillMaxSize()
                    )
                    Text(text = "$totalBeverage / ${targetBeverage ?: "-"} ml")
                }
            }
        }
    }
}