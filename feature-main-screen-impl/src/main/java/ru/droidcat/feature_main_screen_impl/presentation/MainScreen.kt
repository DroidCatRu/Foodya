package ru.droidcat.feature_main_screen_impl.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.droidcat.core_ui.R
import ru.droidcat.core_ui.components.buttons.FoodyaFilledButton
import ru.droidcat.core_ui.components.buttons.FoodyaOutlinedButton
import ru.droidcat.feature_main_screen_impl.ExpireFood

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel()
) {

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val state = viewModel.screenState.collectAsState()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text("Добрый день, ${state.value.userName}")
                },
                windowInsets = WindowInsets.systemBars
                    .only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal),
                scrollBehavior = scrollBehavior,
                actions = {
                    IconButton(
                        onClick = { /*TODO*/ }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.settings),
                            contentDescription = "Settings button"
                        )
                    }
                }
            )
        },
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding() + 24.dp,
                bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding() + 56.dp,
                start = WindowInsets.systemBars.asPaddingValues()
                    .calculateStartPadding(LocalLayoutDirection.current),
                end = WindowInsets.systemBars.asPaddingValues()
                    .calculateEndPadding(LocalLayoutDirection.current)
            ),
            verticalArrangement = spacedBy(16.dp)
        ) {

            // Expires soon
            item {
                ExpireBlock(
                    listOf(
                        ExpireFood("Курица", "Испортится через 1 день"),
                        ExpireFood("Молоко", "Испортится через 3 дня"),
                        ExpireFood("Сыр", "Испортится через 5 дней")
                    )
                )
            }

            // Water balance
            item {
                WaterBlock(
                    waterIntake = state.value.hydration?.intake ?: 0f,
                    waterGoal = state.value.hydration?.goal ?: 0f,
                    onAddClick = { viewModel.addWater() },
                    onDecreaseClick = { viewModel.decreaseWater() }
                )
            }

            // Recommended recipes
            item {
                Text(
                    modifier = Modifier
                        .padding(
                            horizontal = horizontal_padding
                        ),
                    text = "Рекомендуемые рецепты:",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            // Recipe cards
            items(20) {
                Text(
                    modifier = Modifier
                        .padding(
                            horizontal = horizontal_padding
                        ),
                    text = "Карточки рецептов",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpireBlock(
    food: List<ExpireFood>,
    onCardClick: (() -> Unit)? = null
) {
    Column(
        verticalArrangement = spacedBy(8.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = horizontal_padding),
            text = "Продукты скоро испортятся:",
            style = MaterialTheme.typography.titleLarge
        )
        LazyRow(
            contentPadding = PaddingValues(
                horizontal = horizontal_padding
            ),
            horizontalArrangement = spacedBy(16.dp)
        ) {
            items(food) { product ->
                ElevatedCard(
                    onClick = {
                        onCardClick?.invoke()
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .padding(
                                horizontal = 16.dp,
                                vertical = 12.dp
                            )
                    ) {
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = product.expDate,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WaterBlock(
    waterIntake: Float,
    waterGoal: Float,
    onAddClick: (() -> Unit)? = null,
    onDecreaseClick: (() -> Unit)? = null
) {
    Column(
        verticalArrangement = spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(
                    horizontal = horizontal_padding
                )
                .fillMaxWidth(),
            text = "Водный баланс:",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            modifier = Modifier
                .padding(
                    horizontal = horizontal_padding
                ),
            text = "${waterIntake}л/${waterGoal}л",
            style = MaterialTheme.typography.headlineLarge
        )
        Row(
            modifier = Modifier
                .padding(
                    horizontal = horizontal_padding
                )
                .fillMaxWidth(),
            horizontalArrangement = spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FoodyaOutlinedButton(
                modifier = Modifier.weight(0.5f),
                onClick = { onDecreaseClick?.invoke() }
            ) {
                Text("Убавить")
            }
            FoodyaFilledButton(
                modifier = Modifier.weight(0.5f),
                onClick = { onAddClick?.invoke() }
            ) {
                Text("Добавить")
            }
        }
    }
}

private val horizontal_padding = 16.dp