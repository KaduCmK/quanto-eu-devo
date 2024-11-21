package com.example.quantoeudevo.detalhes_financeiro.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
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
import com.example.quantoeudevo.core.data.model.Financeiro
import com.example.quantoeudevo.core.data.model.TipoEmprestimo
import com.example.quantoeudevo.detalhes_financeiro.data.DetalhesFinanceiroUiEvent
import com.example.quantoeudevo.detalhes_financeiro.data.DetalhesFinanceiroUiState
import com.example.quantoeudevo.detalhes_financeiro.presentation.components.DetalhesFinanceiroList
import com.example.quantoeudevo.detalhes_financeiro.presentation.components.DetalhesFinanceiroTopSection
import com.example.quantoeudevo.detalhes_financeiro.presentation.components.QuitarDialog
import com.example.quantoeudevo.tela_inicial.presentation.components.dialog.AddEmprestimoDialog
import com.example.quantoeudevo.ui.theme.QuantoEuDevoTheme
import com.google.firebase.auth.FirebaseUser
import java.math.BigDecimal

@Composable
fun DetalhesFinanceiroScreenRoot(
    modifier: Modifier = Modifier,
    usuario: FirebaseUser?,
    id: String
) {
    val viewModel: DetalhesFinanceiroViewModel = hiltViewModel()
    LaunchedEffect(key1 = Unit) {
        viewModel.onEvent(
            DetalhesFinanceiroUiEvent.OnGetFinanceiro(usuario!!, id)
        )
    }
    DetalhesFinanceiroScreen(
        modifier = modifier,
        uiState = viewModel.uiState.collectAsState().value,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun DetalhesFinanceiroScreen(
    modifier: Modifier = Modifier,
    uiState: DetalhesFinanceiroUiState,
    onEvent: (DetalhesFinanceiroUiEvent) -> Unit
) {
    var emprestimoDialog by remember { mutableStateOf<Pair<Financeiro, TipoEmprestimo>?>(null) }
    var quitarDialog by remember { mutableStateOf<BigDecimal?>(null) }

    emprestimoDialog?.let {
        AddEmprestimoDialog(
            tipoEmprestimo = it.second,
            onDismiss = { emprestimoDialog = null },
            onConfirm = { valor, descricao ->
                onEvent(
                    DetalhesFinanceiroUiEvent.OnAddEmprestimo(
                        uiState.currentUser!!,
                        it.first,
                        it.second,
                        valor,
                        descricao
                    )
                )
                emprestimoDialog = null
            }
        )
    }
    quitarDialog?.let {
        QuitarDialog(
            valor = it,
            onDismiss = { quitarDialog = null },
            onConfirm = {
                onEvent(
                    DetalhesFinanceiroUiEvent.OnAddEmprestimo(
                        (uiState as DetalhesFinanceiroUiState.Loaded).currentUser,
                        uiState.financeiro,
                        if (it > BigDecimal.ZERO) TipoEmprestimo.Debito else TipoEmprestimo.Credito,
                        it,
                        "Quitação"
                    )
                )
                quitarDialog = null
            }
        )
    }

    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .height(256.dp)
                .padding(top = 48.dp)
                .padding(horizontal = 5.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            when (uiState) {
                is DetalhesFinanceiroUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is DetalhesFinanceiroUiState.Loaded -> {
                    DetalhesFinanceiroTopSection(
                        uiState = uiState,
                        onEmprestimoDialog = { emprestimoDialog = it },
                        onQuitardialog = { quitarDialog = it })
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp))
                    DetalhesFinanceiroList(lista = uiState.financeiro.saldo)
                }
            }
        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DetalhesFinanceiroScreenPreview() {
    QuantoEuDevoTheme {
        DetalhesFinanceiroScreen(
            uiState = DetalhesFinanceiroUiState.Loading(null),
            onEvent = {},
        )
    }
}