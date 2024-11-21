package com.example.quantoeudevo.detalhes_financeiro.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.quantoeudevo.core.data.model.Emprestimo
import com.example.quantoeudevo.core.data.model.Financeiro
import com.example.quantoeudevo.core.data.model.TipoEmprestimo
import com.example.quantoeudevo.detalhes_financeiro.data.DetalhesFinanceiroUiState
import java.math.BigDecimal

@Composable
fun DetalhesFinanceiroTopSection(
    modifier: Modifier = Modifier,
    uiState: DetalhesFinanceiroUiState.Loaded,
    onEmprestimoDialog: (Pair<Financeiro, TipoEmprestimo>) -> Unit,
    onQuitardialog: (BigDecimal) -> Unit
) {
    val total = uiState.financeiro.saldo.sumOf {
        when (it) {
            is Emprestimo.Credito -> it.valor
            is Emprestimo.Debito -> -it.valor
        }
    }
    val outroUsuario =
        if (uiState.financeiro.criador.uid == uiState.currentUser.uid)
            uiState.financeiro.outroUsuario
        else uiState.financeiro.criador

    Column(modifier = modifier) {
        Text(
            "Seu saldo com ${outroUsuario.displayName}",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
        Text(
            text = if (total > BigDecimal.ZERO) "${outroUsuario.displayName} te deve R$ $total"
            else if (total < BigDecimal.ZERO) "Você deve R$ ${-total} a ${outroUsuario.displayName}"
            else "Vocês estão quites!",
            style = MaterialTheme.typography.titleMedium
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { onEmprestimoDialog(uiState.financeiro to TipoEmprestimo.Debito) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(imageVector = Icons.Default.Remove, contentDescription = null)
            }
            Button(onClick = { onQuitardialog(total) }) {
                Icon(Icons.Default.Balance, contentDescription = null)
                Text("Quitar contas")
            }
            Button(
                onClick = { onEmprestimoDialog(uiState.financeiro to TipoEmprestimo.Credito) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xff0c2b0e),
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    }
}