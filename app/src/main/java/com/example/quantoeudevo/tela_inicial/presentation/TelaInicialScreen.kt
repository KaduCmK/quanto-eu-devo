package com.example.quantoeudevo.tela_inicial.presentation

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.quantoeudevo.AuthScreen
import com.example.quantoeudevo.auth.data.di.AuthService
import com.example.quantoeudevo.core.data.di.UsuariosService
import com.example.quantoeudevo.core.data.model.Emprestimo
import com.example.quantoeudevo.core.data.model.Financeiro
import com.example.quantoeudevo.core.data.model.Usuario
import com.example.quantoeudevo.tela_inicial.data.model.TelaInicialUiEvent
import com.example.quantoeudevo.tela_inicial.data.model.TelaInicialUiState
import com.example.quantoeudevo.tela_inicial.presentation.components.FinanceiroCard
import com.example.quantoeudevo.tela_inicial.presentation.components.TelaInicialTopBar
import com.example.quantoeudevo.tela_inicial.presentation.components.dialog.AddFinanceiroDialog
import com.example.quantoeudevo.ui.theme.QuantoEuDevoTheme

@Composable
fun TelaInicialScreenRoot(modifier: Modifier = Modifier, authService: AuthService, navController: NavController) {
    val viewModel = hiltViewModel<TelaInicialViewModel>()
    TelaInicialScreen(
        modifier = modifier,
        authService = authService,
        navController = navController,
        uiState = viewModel.uiState.collectAsState().value,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun TelaInicialScreen(
    modifier: Modifier = Modifier,
    authService: AuthService,
    navController: NavController,
    uiState: TelaInicialUiState,
    onEvent: (TelaInicialUiEvent) -> Unit,
) {
    var dialog by remember { mutableStateOf(false) }
    val haptics = LocalHapticFeedback.current

    LaunchedEffect(key1 = Unit) {
        onEvent(TelaInicialUiEvent.OnCheckLoggedIn(authService))
    }

    if (dialog) {
        AddFinanceiroDialog(
            modifier = Modifier,
            usuarios = if (uiState is TelaInicialUiState.Loaded) uiState.usuarios else emptyList(),
            onDismiss = { dialog = false },
            onConfirm = {
                onEvent(TelaInicialUiEvent.OnAddFinanceiro(authService))
                dialog = false
            },
            searchUsers = { onEvent(TelaInicialUiEvent.onSearchUsers(it)) })
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TelaInicialTopBar(
                photoUrl =
                if (uiState is TelaInicialUiState.Loaded) uiState.usuario?.photoUrl else null,
                onLogout = {
                    onEvent(TelaInicialUiEvent.OnLogout(authService))
                })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { dialog = true }) {
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
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    uiState.financeiros.forEach {
                        FinanceiroCard(
                            modifier = Modifier.combinedClickable(
                                onClick = { onEvent(TelaInicialUiEvent.OnClick) },
                                onLongClick = {
                                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onEvent(TelaInicialUiEvent.OnLongClick(it))
                                }
                            ),
                            financeiro = it,
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
                emptyList(),
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
            authService = AuthService(UsuariosService(), LocalContext.current),
            onEvent = {},
            navController = rememberNavController()
        )
    }
}