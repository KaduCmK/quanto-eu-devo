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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quantoeudevo.auth.data.di.AuthService
import com.example.quantoeudevo.core.data.model.Emprestimo
import com.example.quantoeudevo.core.data.model.Financeiro
import com.example.quantoeudevo.core.data.model.Usuario
import com.example.quantoeudevo.detalhes_financeiro.data.DetalhesFinanceiroUiEvent
import com.example.quantoeudevo.detalhes_financeiro.data.DetalhesFinanceiroUiState
import com.example.quantoeudevo.detalhes_financeiro.presentation.components.DetalhesFinanceiroList
import com.example.quantoeudevo.ui.theme.QuantoEuDevoTheme
import java.math.BigDecimal
import java.time.LocalDateTime

@Composable
fun DetalhesFinanceiroScreenRoot(modifier: Modifier = Modifier, authService: AuthService, id: String) {
    val viewModel: DetalhesFinanceiroViewModel = hiltViewModel()
    DetalhesFinanceiroScreen(
        modifier = modifier,
        authService = authService,
        id = id,
        uiState = viewModel.uiState.collectAsState().value,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun DetalhesFinanceiroScreen(
    modifier: Modifier = Modifier,
    authService: AuthService,
    id: String = "",
    uiState: DetalhesFinanceiroUiState,
    onEvent: (DetalhesFinanceiroUiEvent) -> Unit
) {

    LaunchedEffect(key1 = Unit) { onEvent(DetalhesFinanceiroUiEvent.OnGetFinanceiro(authService, id)) }

    when (uiState) {
        is DetalhesFinanceiroUiState.Loading -> {
            CircularProgressIndicator()
        }

        is DetalhesFinanceiroUiState.Loaded -> {
            val total = uiState.financeiro.saldo.sumOf {
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
                            "Seu saldo com ${uiState.financeiro.outroUsuario.displayName}",
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = if (total > BigDecimal.ZERO) "${uiState.financeiro.criador.displayName} te deve R$ $total"
                            else if (total < BigDecimal.ZERO) "Você deve R$ ${-total} a ${uiState.financeiro.criador.displayName}"
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
                    DetalhesFinanceiroList(lista = uiState.financeiro.saldo)
                }
            }
        }
    }
}
//
//@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//private fun DetalhesFinanceiroScreenPreview() {
//    QuantoEuDevoTheme {
//        DetalhesFinanceiroScreen(
//            uiState = DetalhesFinanceiroUiState.Loading,
//            authService = authS,
//            onEvent = {},
//        )
//    }
//}