package com.cfa.cda.catapp.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import com.cfa.cda.catapp.ui.theme.LocalCatColors

@Composable
fun CatLogo(modifier: Modifier = Modifier) {
    val colors = LocalCatColors.current

    Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(color = colors.primary, fontStyle = FontStyle.Italic)) {
                append("Le ")
            }
            append("\uD83D\uDC08")
            withStyle(SpanStyle(color = colors.primary, fontStyle = FontStyle.Italic)) {
                append(" allant")
            }
        },
        style = MaterialTheme.typography.titleLarge,
        modifier = modifier
    )
}