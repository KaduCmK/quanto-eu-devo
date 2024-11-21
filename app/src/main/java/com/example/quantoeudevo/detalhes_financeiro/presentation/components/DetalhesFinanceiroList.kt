package com.example.quantoeudevo.detalhes_financeiro.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quantoeudevo.core.data.model.Emprestimo
import com.example.quantoeudevo.core.data.model.Usuario
import com.example.quantoeudevo.ui.theme.QuantoEuDevoTheme
import java.math.BigDecimal

@Composable
fun DetalhesFinanceiroList(modifier: Modifier = Modifier, lista: List<Emprestimo>) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(lista) {
            EmprestimoCard(emprestimo = it)
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DetalhesFinanceiroListPreview() {
    QuantoEuDevoTheme {
        Surface {
            DetalhesFinanceiroList(
                lista = listOf(
                    Emprestimo.Credito(
                        "2",
                        Usuario("0", "", "K", ""),
                        BigDecimal(1000.0), "Salário",
                        0,
                        Usuario("1", "", "L", "")

                    ),
                    Emprestimo.Debito(
                        "",
                        Usuario("0", "", "K", ""),
                        BigDecimal(1000.0), "Salário",
                        0,
                        Usuario("1", "", "L", "")

                    )
                )
            )
        }
    }
}