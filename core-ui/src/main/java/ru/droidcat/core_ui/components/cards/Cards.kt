package ru.droidcat.core_ui.components.cards

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlin.math.floor
import kotlin.math.roundToLong

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeCard(
    recipeName: String,
    servings: Int,
    weight: Float,
    modifier: Modifier = Modifier,
    recipeImageUrl: String? = null,
    onClick: (() -> Unit)? = null
) {
    OutlinedCard(
        modifier = modifier,
        onClick = { onClick?.invoke() }
    ) {
        if (recipeImageUrl != null) {
            GlideImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
                imageModel = recipeImageUrl,
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    top = 20.dp,
                    end = 20.dp,
                    bottom = 24.dp
                ),
            verticalArrangement = spacedBy(8.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = recipeName,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Порций: $servings",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Вес: ${floor(weight.toDouble()) / 1000.0}кг",
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}