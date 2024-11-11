package com.example.quantoeudevo.tela_inicial.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.quantoeudevo.R
import com.example.quantoeudevo.ui.theme.QuantoEuDevoTheme

@Composable
fun TelaInicialTopBar(modifier: Modifier = Modifier, photoUrl: String?, onLogout: () -> Unit = {}) {
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
        AsyncImage(
            modifier = Modifier.clip(CircleShape).size(40.dp).clickable { onLogout() },
            model = ImageRequest.Builder(LocalContext.current)
                .data(photoUrl)
                .placeholder(R.drawable.account_circle_24dp_e8eaed_fill1_wght400_grad0_opsz24)
                .crossfade(true)
                .build(),
            contentDescription = "Profile picture"
        )
    }
}

@Preview
@Composable
private fun TopbarPreview() {
    QuantoEuDevoTheme {
        Surface {
            TelaInicialTopBar(photoUrl = null)
        }
    }
}