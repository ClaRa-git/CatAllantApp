package com.cfa.cda.catapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cfa.cda.catapp.ui.theme.LocalCatColors

@Composable
fun CharacterBar(
    label: String,
    value: Int,
    maxValue: Int = 5,
    modifier: Modifier = Modifier
) {
    val colors = LocalCatColors.current

    Column(modifier = modifier.fillMaxWidth()) {
        Text(text = label, fontSize = 13.sp, color = colors.primary)
        androidx.compose.foundation.layout.Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(colors.border.copy(alpha = 0.4f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = (value.toFloat() / maxValue).coerceIn(0f, 1f))
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(colors.primary)
            )
        }
    }
}