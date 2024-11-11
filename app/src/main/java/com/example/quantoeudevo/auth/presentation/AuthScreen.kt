package com.example.quantoeudevo.auth.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quantoeudevo.auth.data.model.AuthUiState
import com.example.quantoeudevo.ui.theme.QuantoEuDevoTheme

@Composable
fun AuthScreenRoot(modifier: Modifier = Modifier) {
    val viewModel: AuthViewModel = hiltViewModel()
    AuthScreen(
        modifier = modifier,
        uiState = viewModel.uiState.collectAsState().value,
        onLogin = viewModel::handleGoogleSignIn
    )
}

@Composable
fun AuthScreen(modifier: Modifier = Modifier, uiState: AuthUiState, onLogin: () -> Unit) {
    LaunchedEffect(key1 = Unit) {
        onLogin()
    }

    Surface {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text("Fazer Login", style = MaterialTheme.typography.displayMedium)
            Button(onClick = onLogin) {
                Icon(imageVector = Icons.AutoMirrored.Default.Login, contentDescription = null)
                Text("Login com Google")
            }
            when (uiState) {
                is AuthUiState.Ready -> { }
                is AuthUiState.Loading -> Text("Carregando...")
                is AuthUiState.Error -> Text("Erro: ${(uiState).message}")
                is AuthUiState.Success -> Text("Logado como ${uiState.email}")
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AuthScreenPreview() {
    QuantoEuDevoTheme {
        AuthScreen(uiState = AuthUiState.Ready) { }
    }
}