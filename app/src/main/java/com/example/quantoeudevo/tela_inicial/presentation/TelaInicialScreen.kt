package com.example.quantoeudevo.tela_inicial.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quantoeudevo.core.model.Credito
import com.example.quantoeudevo.core.model.Divida
import com.example.quantoeudevo.core.model.Financeiro
import com.example.quantoeudevo.tela_inicial.data.model.TelaInicialUiEvent
import com.example.quantoeudevo.tela_inicial.data.model.TelaInicialUiState
import com.example.quantoeudevo.tela_inicial.presentation.components.FinanceiroCard
import com.example.quantoeudevo.ui.theme.QuantoEuDevoTheme

@Composable
fun TelaInicialScreenRoot(modifier: Modifier = Modifier, onNavigate: (String) -> Unit) {
    val viewModel = hiltViewModel<TelaInicialViewModel>()
    TelaInicialScreen(
        modifier = modifier,
        uiState = viewModel.uiState.collectAsState().value,
        onEvent = viewModel::onEvent,
        onNavigate = onNavigate
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TelaInicialScreen(
    modifier: Modifier = Modifier,
    uiState: TelaInicialUiState,
    onEvent: (TelaInicialUiEvent) -> Unit,
    onNavigate: (String) -> Unit
) {
    Scaffold(
        topBar = {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "Tela Inicial",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    ) { innerPadding ->
        when (uiState) {
            TelaInicialUiState.Loading -> {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }

            is TelaInicialUiState.Loaded -> {
                FlowRow(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    uiState.financeiros.forEach {
                        FinanceiroCard(
                            modifier = Modifier.padding(8.dp),
                            financeiro = it,
                            onClick = { onNavigate(it.nome) }
                        )
                    }
                }
            }

            is TelaInicialUiState.Error -> {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Erro: ${uiState.error}")
                }
            }
        }
    }
}

@Preview(name = "Dark Mode", showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Light Mode", showSystemUi = true)
@Composable
fun TelaInicialScreenPreview() {
    QuantoEuDevoTheme {
        TelaInicialScreen(
            uiState = TelaInicialUiState.Loaded(
                listOf(
                    Financeiro("Lucas", listOf(
                        Credito("0", 10.0, "Itaipava"),
                        Divida("1", 5.0, "Guaravita")
                    )),
                    Financeiro("Elias", listOf(
                        Credito("2", 10.0, "Itaipava"),
                        Divida("2", 5.0, "Guaravita")
                    )),
                    Financeiro("Kadu", listOf(
                        Divida("4", 6.90, "Uber"),
                        Credito("5", 3.00, "Bandejao")
                    ))
                )
            ),
            onEvent = {},
            onNavigate = {}
        )
    }
}