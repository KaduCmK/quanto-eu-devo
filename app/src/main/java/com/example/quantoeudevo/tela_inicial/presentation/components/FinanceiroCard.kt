package com.example.quantoeudevo.tela_inicial.presentation.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quantoeudevo.R
import com.example.quantoeudevo.core.data.model.Emprestimo
import com.example.quantoeudevo.core.data.model.Financeiro
import com.example.quantoeudevo.core.data.model.Usuario
import com.example.quantoeudevo.ui.theme.QuantoEuDevoTheme
import java.math.BigDecimal

@Composable
fun FinanceiroCard(
    modifier: Modifier = Modifier,
    usuario: Usuario,
    financeiro: Financeiro,
    onAddCredito: () -> Unit,
    onAddDebito: () -> Unit
) {
    val total = financeiro.saldo.sumOf {
        when (it) {
            is Emprestimo.Credito -> it.valor
            is Emprestimo.Debito -> -it.valor
        }
    }

    val cor: Color by animateColorAsState(
        targetValue = if (total > BigDecimal.ZERO) Color(0xff0c2b0e)
        else Color(0xff661511)
    )

    Column(
        modifier = Modifier.width(128.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            onClick = onAddCredito,
            colors = ButtonDefaults.buttonColors()
                .copy(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(128.dp),
            colors = CardDefaults.elevatedCardColors().copy(containerColor = cor)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (financeiro.criador.uid == usuario.uid) financeiro.outroUsuario.displayName
                        ?: ""
                    else financeiro.criador.displayName ?: "",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.LightGray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(
                        "R$ ",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.LightGray
                    )
                    AnimatedContent(
                        targetState = total,
                        transitionSpec = {
                            if (targetState > initialState) {
                                slideInVertically { h -> h } + fadeIn() togetherWith
                                        slideOutVertically { h -> -h } + fadeOut()
                            } else {
                                slideInVertically { h -> -h } + fadeIn() togetherWith
                                        slideOutVertically { h -> h } + fadeOut()
                            }.using(SizeTransform(clip = false))
                        },
                        label = stringResource(R.string.valor_total)
                    ) { targetCount ->
                        Text(
                            "$targetCount",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.LightGray
                        )
                    }
                }
            }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            onClick = onAddDebito,
            colors = ButtonDefaults.buttonColors()
                .copy(containerColor = MaterialTheme.colorScheme.errorContainer)
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Light")
@Composable
private fun FinanceiroCardPreview() {
    QuantoEuDevoTheme {
        FinanceiroCard(
            onAddCredito = {},
            onAddDebito = {},
            usuario = Usuario("0", "", "K", ""),
            financeiro = Financeiro(
                "00",
                Usuario("0", "", "K", ""),
                Usuario("1", "", "L", ""),
                listOf(
                    Emprestimo.Credito(
                        "3",
                        Usuario("0", "", "K", ""),
                        BigDecimal(1000.0), "Salário",
                        0,
                        Usuario("1", "", "L", "")

                    ),
                    Emprestimo.Debito(
                        "2",
                        Usuario("0", "", "K", ""),
                        BigDecimal(1000.0), "Salário",
                        0,
                        Usuario("1", "", "L", "")

                    ),
                    Emprestimo.Credito(
                        "",
                        Usuario("0", "", "K", ""),
                        BigDecimal(1000.0), "Salário",
                        0,
                        Usuario("1", "", "L", "")

                    ),
                ),
                0
            )
        )
    }
}