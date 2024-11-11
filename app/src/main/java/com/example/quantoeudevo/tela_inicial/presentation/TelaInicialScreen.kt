package com.example.quantoeudevo.tela_inicial.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.quantoeudevo.AuthScreen
import com.example.quantoeudevo.core.data.model.Emprestimo
import com.example.quantoeudevo.core.data.model.Financeiro
import com.example.quantoeudevo.core.data.model.Usuario
import com.example.quantoeudevo.tela_inicial.data.model.TelaInicialUiEvent
import com.example.quantoeudevo.tela_inicial.data.model.TelaInicialUiState
import com.example.quantoeudevo.tela_inicial.presentation.components.FinanceiroCard
import com.example.quantoeudevo.tela_inicial.presentation.components.TelaInicialTopBar
import com.example.quantoeudevo.ui.theme.QuantoEuDevoTheme

@Composable
fun TelaInicialScreenRoot(modifier: Modifier = Modifier, navController: NavController) {
    val viewModel = hiltViewModel<TelaInicialViewModel>()
    TelaInicialScreen(
        modifier = modifier,
        navController = navController,
        uiState = viewModel.uiState.collectAsState().value,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TelaInicialScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    uiState: TelaInicialUiState,
    onEvent: (TelaInicialUiEvent) -> Unit,
) {
    var dialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        onEvent(TelaInicialUiEvent.OnCheckLoggedIn)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TelaInicialTopBar(
                photoUrl =
                if (uiState is TelaInicialUiState.Loaded) uiState.usuario?.photoUrl else null,
                onLogout = {
                    onEvent(TelaInicialUiEvent.OnLogout)
                })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(TelaInicialUiEvent.OnAddFinanceiro) }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
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
                            onClick = { onEvent(TelaInicialUiEvent.OnClick) }
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

            is TelaInicialUiState.Unauthorized ->
                LaunchedEffect(key1 = Unit) {
                    navController.navigate(AuthScreen)
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
                null,
                listOf(
                    Financeiro(
                        "00",
                        Usuario("0", "", "K", ""),
                        Usuario("1", "", "L", ""),
                        listOf(
                            Emprestimo.Credito(
                                Usuario("0", "", "K", ""),
                                1000.0, "Salário",
                                0L,
                                Usuario("1", "", "L", "")

                            ),
                            Emprestimo.Debito(
                                Usuario("0", "", "K", ""),
                                1000.0, "Salário",
                                0L,
                                Usuario("1", "", "L", "")

                            ),
                            Emprestimo.Credito(
                                Usuario("0", "", "K", ""),
                                1000.0, "Salário",
                                0L,
                                Usuario("1", "", "L", "")

                            ),
                        ),
                        0L
                    )
                )
            ),
            onEvent = {},
            navController = rememberNavController()
        )
    }
}