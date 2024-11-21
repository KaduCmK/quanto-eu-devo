package com.example.quantoeudevo.detalhes_financeiro.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.quantoeudevo.ui.theme.QuantoEuDevoTheme
import java.math.BigDecimal

@Composable
fun QuitarDialog(
    modifier: Modifier = Modifier,
    valor: BigDecimal,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Confirmar")
            }
        },
        title = {
            Text("Quitar contas")
        },
        text = {
            Text("Será gerado uma conta no valor de R$ $valor. Deseja continuar?")
        }
    )
}

@Preview
@Composable
private fun QuitarDialogPreview() {
    QuantoEuDevoTheme {
        QuitarDialog(
            valor = BigDecimal(4.50), onDismiss = {}, onConfirm = {}
        )
    }
}