package com.cfa.cda.catapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cfa.cda.catapp.ui.theme.LocalCatColors

private val EXTENSIONS = listOf("jpg", "png", "gif")

@Composable
fun CatBreedImage(
    imageId: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    imageHeight: Dp = 220.dp
) {
    var extIndex by remember(imageId) { mutableIntStateOf(0) }
    val allFailed = extIndex >= EXTENSIONS.size
    val colors = LocalCatColors.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(imageHeight)
            .background(colors.imagePlaceholder),
        contentAlignment = Alignment.Center
    ) {
        if (imageId == null || allFailed) {
            Icon(
                imageVector = Icons.Outlined.Pets,
                contentDescription = contentDescription,
                tint = colors.primary.copy(alpha = 0.4f),
                modifier = Modifier.height(48.dp)
            )
        } else {
            val url = "https://cdn2.thecatapi.com/images/$imageId.${EXTENSIONS[extIndex]}"
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(url)
                    .build(),
                contentDescription = contentDescription,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize(),
                onError = { extIndex++ }
            )
        }
    }
}