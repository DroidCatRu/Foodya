package ru.droidcat.feature_products_impl.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen() {
    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                icon = {
                    Icon(Icons.Filled.Add,"")
                },
                text = {
                    Text("Добавить")
                },
                modifier = Modifier
                    .padding(
                        bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding() + 80.dp
                    ),
                onClick = {  },
            )
        }
    ) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Adaptive(160.dp),
            state = rememberLazyGridState(),
            verticalArrangement = spacedBy(8.dp),
            horizontalArrangement = spacedBy(8.dp),
            contentPadding = PaddingValues(
                top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding() + 16.dp,
                bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding() + 80.dp,
                start = 16.dp,
                end = 16.dp
            )
        ) {
            items(10) { index ->
                ProductCard(
                    productName = productNames[index],
                    productGroup = productGroups[index],
                    productExpireTime = productExpireTime[index],
                    productExpiresSoon = productExpiresSoon[index]
                )
            }
        }
    }
}



@Composable
fun ProductCard(
    productName: String,
    productGroup: String,
    productExpireTime: String,
    productExpiresSoon: Boolean,
    onClick: (() -> Unit)? = null,
) {
    OutlinedCard {
        Column(
            modifier = Modifier.padding(
                all = 8.dp
            ),
            verticalArrangement = spacedBy(8.dp)
        ) {
            Text(
                text = productName,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = productGroup,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.height(55.dp)
            )

            Text(
                color =
                if (productExpiresSoon)
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.onSurface,
                text = "Годен до: ${productExpireTime}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

val productNames = arrayOf("Курица", "Молоко", "Сыр", "Творог", "Помидоры", "Огурцы", "Яйца", "Гречка", "Колбаса", "Масло")
val productGroups = arrayOf("Мясо", "Молочный продукт", "Молочный продукт", "Молочный продукт", "Овощи", "Овощи", "Животный продукт", "Крупы", "Мясо", "Масла и жиры")
val productExpireTime = arrayOf("2022-09-21", "2022-09-23", "2022-09-25", "2022-09-30", "2022-09-30", "2022-09-30", "2022-09-30", "2022-09-30", "2022-09-30", "2022-09-30")
val productExpiresSoon = arrayOf(true, true, true, false, false, false, false, false, false, false)