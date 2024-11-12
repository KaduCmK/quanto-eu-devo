package com.example.quantoeudevo.tela_inicial.presentation.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.quantoeudevo.R
import com.example.quantoeudevo.core.data.model.Usuario
import com.example.quantoeudevo.ui.theme.QuantoEuDevoTheme

@Composable
fun AddFinanceiroDialog(
    modifier: Modifier = Modifier,
    usuarios: List<Usuario>,
    onDismiss: () -> Unit,
    onConfirm: (Usuario) -> Unit,
    searchUsers: (String) -> Unit
) {
    var loading by remember { mutableStateOf(false) }
    var nome by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(420.dp),
            shape = RoundedCornerShape(8.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text("Selecione o remetente", style = MaterialTheme.typography.titleLarge)
                OutlinedTextField(
                    value = nome,
                    onValueChange = {
                        nome = it
                        if (it.length >= 4) {
                            searchUsers(it)
                        }
                    },
                    label = { Text("Procure pelo nome") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (loading)
                        item { CircularProgressIndicator() }
                    else
                        items(usuarios) { user ->
                            ElevatedCard(
                                onClick = { onConfirm(user) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    AsyncImage(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .size(36.dp),
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(user.photoUrl)
                                            .crossfade(true)
                                            .placeholder(R.drawable.account_circle_24dp_e8eaed_fill1_wght400_grad0_opsz24)
                                            .build(),
                                        contentDescription = null,
                                    )
                                    Text(
                                        user.displayName!!,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }
                }
            }

        }
    }
}

@Preview
@Composable
private fun AddFinanceiroDialogPreview() {
    QuantoEuDevoTheme {
        AddFinanceiroDialog(
            onDismiss = {},
            onConfirm = {},
            usuarios = emptyList(),
            searchUsers = { _ ->
                listOf(
                    Usuario("1", "João", ""),
                    Usuario("2", "Maria", ""),
                    Usuario("3", "José", "")
                )
            })
    }
}