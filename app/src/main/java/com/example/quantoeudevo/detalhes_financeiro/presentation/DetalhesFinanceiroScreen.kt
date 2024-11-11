package com.example.quantoeudevo.detalhes_financeiro.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quantoeudevo.core.data.model.Emprestimo
import com.example.quantoeudevo.core.data.model.Financeiro
import com.example.quantoeudevo.core.data.model.Usuario
import com.example.quantoeudevo.detalhes_financeiro.presentation.components.DetalhesFinanceiroList
import com.example.quantoeudevo.ui.theme.QuantoEuDevoTheme
import java.time.LocalDateTime

@Composable
fun DetalhesFinanceiroScreenRoot(modifier: Modifier = Modifier, id: String) {
    val viewModel: DetalhesFinanceiroViewModel = hiltViewModel()
    DetalhesFinanceiroScreen(
        modifier = modifier,
        financeiro = Financeiro(
            "00",
            Usuario("0", "", "K", ""),
            Usuario("1", "", "L", ""),
            emptyList(),
            LocalDateTime.now().toEpochSecond(null)
        )
    )
}

@Composable
fun DetalhesFinanceiroScreen(modifier: Modifier = Modifier, financeiro: Financeiro) {
    val total = financeiro.saldo.sumOf {
        when (it) {
            is Emprestimo.Credito -> it.valor
            is Emprestimo.Debito -> -it.valor
        }
    }

    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Column(
                modifier = Modifier
                    .height(256.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    "Seu saldo com ${financeiro.criador.displayName}",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = if (total > 0) "${financeiro.criador.displayName} te deve R$ $total"
                    else if (total < 0) "Você deve R$ ${-total} a ${financeiro.criador.displayName}"
                    else "Vocês estão quites!",
                    style = MaterialTheme.typography.titleMedium
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = {}) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        Text("Novo crédito")
                    }
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors()
                            .copy(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.error
                            )
                    ) {
                        Icon(imageVector = Icons.Default.Remove, contentDescription = null)
                        Text("Nova dívida")
                    }
                }
            }
            HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp))
            DetalhesFinanceiroList(lista = financeiro.saldo)
        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DetalhesFinanceiroScreenPreview() {
    QuantoEuDevoTheme {
        DetalhesFinanceiroScreen(
            financeiro = Financeiro(
                "00",
                Usuario("0","", "K", ""),
                Usuario("1","", "L", ""),
                listOf(
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

                    ),
                    Emprestimo.Credito(
                        Usuario("0", "", "K", ""),
                        1000.0, "Salário",
                        LocalDateTime.now().toEpochSecond(null),
                        Usuario("1", "", "L", "")

                    ),
                ),
                LocalDateTime.now().toEpochSecond(null)
            )
        )
    }
}