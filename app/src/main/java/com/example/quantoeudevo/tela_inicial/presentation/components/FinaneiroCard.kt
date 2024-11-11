package com.example.quantoeudevo.tela_inicial.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quantoeudevo.core.model.Credito
import com.example.quantoeudevo.core.model.Divida
import com.example.quantoeudevo.core.model.Financeiro
import com.example.quantoeudevo.ui.theme.QuantoEuDevoTheme

@Composable
fun FinanceiroCard(modifier: Modifier = Modifier, financeiro: Financeiro, onClick: () -> Unit) {
    val total = financeiro.saldo.sumOf {
        when (it) {
            is Credito -> it.valor
            is Divida -> -it.valor
        }
    }

    Column(
        modifier = modifier.width(128.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            onClick = onClick,
            colors = ButtonDefaults.buttonColors()
                .copy(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp),
            onClick = onClick,
            colors = CardDefaults.elevatedCardColors()
                .copy(
                    containerColor = if (total > 0)
                        Color(0xff0c2b0e)
                    else Color(0xff661511)
                )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "R$ $total",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(financeiro.nome, style = MaterialTheme.typography.titleSmall)
            }
        }
        Button(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            onClick = onClick,
            colors = ButtonDefaults.buttonColors()
                .copy(containerColor = MaterialTheme.colorScheme.errorContainer)
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FinanceiroCardPreview() {
    QuantoEuDevoTheme {
        FinanceiroCard(
            onClick = {},
            financeiro = Financeiro(
                nome = "Lucas", saldo = listOf(
                    Credito("1", 10.0, "ABC")
                )
            )
        )
    }
}