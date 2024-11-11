package com.example.quantoeudevo.detalhes_financeiro.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quantoeudevo.core.data.model.Emprestimo
import com.example.quantoeudevo.core.data.model.Usuario
import com.example.quantoeudevo.ui.theme.QuantoEuDevoTheme
import java.time.LocalDateTime

@Composable
fun DetalhesFinanceiroList(modifier: Modifier = Modifier, lista: List<Emprestimo>) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(lista) {
            Card(
                colors = CardDefaults.cardColors()
                    .copy(
                        containerColor =
                        if (it is Emprestimo.Credito) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.errorContainer
                    )
            ) {
                Row(
                    modifier = Modifier
                        .padding(vertical = 16.dp, horizontal = 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    when (it) {
                        is Emprestimo.Credito -> {
                            Text(
                                text = "Crédito: ${it.descricao}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }

                        is Emprestimo.Debito -> {
                            Text(
                                text = "Dívida: ${it.descricao}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    Text(
                        text = "R$ ${it.valor}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = if (it is Emprestimo.Credito)
                            MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DetalhesFinanceiroListPreview() {
    QuantoEuDevoTheme {
        Surface {
            DetalhesFinanceiroList(
                lista = listOf(
                    Emprestimo.Credito(
                        Usuario("0", "", "K", ""),
                        1000.0, "Salário",
                        LocalDateTime.now().toEpochSecond(null),
                        Usuario("1", "", "L", "")

                    ),
                    Emprestimo.Debito(
                        Usuario("0", "", "K", ""),
                        1000.0, "Salário",
                        LocalDateTime.now().toEpochSecond(null),
                        Usuario("1", "", "L", "")

                    )
                )
            )
        }
    }
}