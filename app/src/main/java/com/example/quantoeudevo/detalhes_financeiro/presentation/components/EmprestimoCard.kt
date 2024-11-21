package com.example.quantoeudevo.detalhes_financeiro.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quantoeudevo.core.data.model.Emprestimo
import com.example.quantoeudevo.core.data.model.Usuario
import com.example.quantoeudevo.ui.theme.QuantoEuDevoTheme
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
fun EmprestimoCard(modifier: Modifier = Modifier, emprestimo: Emprestimo) {
    val data = LocalDateTime.ofEpochSecond(emprestimo.timestamp, 0, ZoneOffset.UTC)
    val localDate = data.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val localTime = data.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))

    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors()
            .copy(
                containerColor =
                if (emprestimo is Emprestimo.Credito) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.errorContainer
            )
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (emprestimo) {
                        is Emprestimo.Credito -> "Crédito"
                        is Emprestimo.Debito -> "Dívida"
                    },
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "$localDate às $localTime",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.surfaceTint,
                    fontStyle = FontStyle.Italic
                )
                Text(
                    text = "R$ ${emprestimo.valor}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = if (emprestimo is Emprestimo.Credito)
                        MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.error
                )
            }
            Text(
                text = emprestimo.descricao.ifEmpty { "Sem descrição" },
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic
            )
        }
    }
}

@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Light")
@Composable
private fun EmprestimoCardPreview() {
    QuantoEuDevoTheme {
        EmprestimoCard(
            emprestimo = Emprestimo.Credito(
                "2",
                Usuario("0", "", "K", ""),
                BigDecimal(1000.0), "",
                1514686946,
                Usuario("1", "", "L", "")

            )
        )
    }
}