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
import com.example.quantoeudevo.core.data.model.Emprestimo
import com.example.quantoeudevo.core.data.model.Financeiro
import com.example.quantoeudevo.core.data.model.Usuario
import com.example.quantoeudevo.ui.theme.QuantoEuDevoTheme

@Composable
fun FinanceiroCard(modifier: Modifier = Modifier, financeiro: Financeiro) {
    val total = financeiro.saldo.sumOf {
        when (it) {
            is Emprestimo.Credito -> it.valor
            is Emprestimo.Debito -> -it.valor
        }
    }

    Column(
        modifier = Modifier.width(128.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            onClick = {},
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
            modifier = modifier
                .fillMaxWidth()
                .height(128.dp),
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
                Text(financeiro.criador.displayName ?: "", style = MaterialTheme.typography.titleSmall)
            }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            onClick = {},
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
            financeiro = Financeiro(
                "00",
                Usuario("0", "", "K", ""),
                Usuario("1", "", "L", ""),
                listOf(
                    Emprestimo.Credito(
                        Usuario("0", "","K", ""),
                        1000.0, "Salário",
                        0,
                        Usuario("1", "","L", "")

                    ),
                    Emprestimo.Debito(
                        Usuario("0","", "K", ""),
                        1000.0, "Salário",
                        0,
                        Usuario("1", "","L", "")

                    ),
                    Emprestimo.Credito(
                        Usuario("0","", "K", ""),
                        1000.0, "Salário",
                        0,
                        Usuario("1", "","L", "")

                    ),
                ),
                0
            )
        )
    }
}