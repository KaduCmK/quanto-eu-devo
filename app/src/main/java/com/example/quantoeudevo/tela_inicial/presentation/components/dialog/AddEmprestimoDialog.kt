package com.example.quantoeudevo.tela_inicial.presentation.components.dialog

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.quantoeudevo.core.data.model.TipoEmprestimo
import com.example.quantoeudevo.ui.theme.QuantoEuDevoTheme
import java.math.BigDecimal

@Composable
fun AddEmprestimoDialog(
    modifier: Modifier = Modifier,
    tipoEmprestimo: TipoEmprestimo,
    onDismiss: () -> Unit,
    onConfirm: (BigDecimal, String) -> Unit
) {
    val config = LocalConfiguration.current

    var valor by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier
                .width((config.screenWidthDp * 0.7f).dp)
        ) {
            Column(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Novo ${if (tipoEmprestimo is TipoEmprestimo.Credito) "Crédito" else "Débito"}",
                    style = MaterialTheme.typography.titleMedium
                )
                OutlinedTextField(
                    value = valor,
                    onValueChange = { valor = it.replace(",", ".") },
                    prefix = { Text(text = if (tipoEmprestimo is TipoEmprestimo.Credito) "+ R$" else "- R$") },
                    label = { Text("Valor") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
                OutlinedTextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    label = { Text("Descrição") },

                    )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }
                    TextButton(onClick = { onConfirm(valor.toBigDecimal(), descricao) }, enabled = valor.isNotEmpty()) {
                        Text("Adicionar")
                    }
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AddEmprestimoDialogPreview() {
    QuantoEuDevoTheme {
        AddEmprestimoDialog(
            tipoEmprestimo = TipoEmprestimo.Credito,
            onDismiss = {},
            onConfirm = { _, _ -> })
    }
}