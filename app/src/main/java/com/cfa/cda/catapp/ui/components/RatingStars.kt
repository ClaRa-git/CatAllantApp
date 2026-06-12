package com.cfa.cda.catapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cfa.cda.catapp.ui.theme.LocalCatColors
import androidx.compose.foundation.layout.size

@Composable
fun RatingStars(
    rating: Float,
    onRatingChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalCatColors.current

    Row(modifier = modifier) {
        for (i in 1..5) {
            Icon(
                imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.StarBorder,
                contentDescription = "Note $i",
                tint = colors.ratingStar,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onRatingChange(i.toFloat()) }
            )
        }
    }
}