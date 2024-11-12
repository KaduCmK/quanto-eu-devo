package com.example.quantoeudevo.tela_inicial.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.Coil
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.quantoeudevo.ui.theme.QuantoEuDevoTheme

@Composable
fun TelaInicialTopBar(modifier: Modifier = Modifier, photoUrl: String?, onLogout: () -> Unit = {}) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(photoUrl)
            .size(Size.ORIGINAL)
            .crossfade(true)
            .build()
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 48.dp, bottom = 32.dp, start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Tela Inicial",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.SemiBold,
        )
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = CircleShape
                )
                .clickable { onLogout() }, contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.size(40.dp),
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
            if (painter.state !is AsyncImagePainter.State.Success) {
                Icon(
                    modifier = Modifier.size(40.dp),
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
//            }

            }
        }
    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TopbarPreview() {
    QuantoEuDevoTheme {
        Surface {
            TelaInicialTopBar(photoUrl = null)
        }
    }
}