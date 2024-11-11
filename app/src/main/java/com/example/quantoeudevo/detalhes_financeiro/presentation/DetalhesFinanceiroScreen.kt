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
import com.example.quantoeudevo.core.model.Credito
import com.example.quantoeudevo.core.model.Divida
import com.example.quantoeudevo.core.model.Financeiro
import com.example.quantoeudevo.detalhes_financeiro.presentation.components.DetalhesFinanceiroList
import com.example.quantoeudevo.ui.theme.QuantoEuDevoTheme

@Composable
fun DetalhesFinanceiroScreenRoot(modifier: Modifier = Modifier, id: String) {
    val viewModel: DetalhesFinanceiroViewModel = hiltViewModel()
    DetalhesFinanceiroScreen(modifier = modifier, financeiro = Financeiro("Lucas", emptyList()))
}

@Composable
fun DetalhesFinanceiroScreen(modifier: Modifier = Modifier, financeiro: Financeiro) {
    val total = financeiro.saldo.sumOf {
        when (it) {
            is Credito -> it.valor
            is Divida -> -it.valor
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
                    "Seu saldo com ${financeiro.nome}",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = if (total > 0) "${financeiro.nome} te deve R$ $total"
                    else if (total < 0) "Você deve R$ ${-total} a ${financeiro.nome}"
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
                "Lucas", listOf(
                    Credito("", 1000.0, "Salário"),
                    Divida("", 500.0, "Aluguel"),
                    Credito("", 200.0, "Freela"),
                )
            )
        )
    }
}