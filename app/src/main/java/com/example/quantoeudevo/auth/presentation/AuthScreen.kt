package com.example.quantoeudevo.auth.presentation

import android.content.Context
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.quantoeudevo.TelaInicialScreen
import com.example.quantoeudevo.auth.data.di.AuthService
import com.example.quantoeudevo.auth.data.model.AuthUiEvent
import com.example.quantoeudevo.auth.data.model.AuthUiState
import com.example.quantoeudevo.core.data.di.UsuariosService
import com.example.quantoeudevo.ui.theme.QuantoEuDevoTheme
import javax.inject.Inject

@Composable
fun AuthScreenRoot(
    modifier: Modifier = Modifier,
    authService: AuthService,
    navController: NavController
) {
    val viewModel: AuthViewModel = hiltViewModel()
    AuthScreen(
        modifier = modifier,
        authService = authService,
        navController = navController,
        uiState = viewModel.uiState.collectAsState().value,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    authService: AuthService,
    navController: NavController,
    uiState: AuthUiState,
    onEvent: (AuthUiEvent) -> Unit
) {
    Surface {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text("Fazer Login", style = MaterialTheme.typography.displayMedium)
            Button(onClick = { onEvent(AuthUiEvent.OnGoogleSignIn(authService)) }) {
                Icon(imageVector = Icons.AutoMirrored.Default.Login, contentDescription = null)
                Text("Login com Google")
            }
            when (uiState) {
                is AuthUiState.Ready -> {}
                is AuthUiState.Loading -> Text("Carregando...")
                is AuthUiState.Error -> Text("Erro: ${(uiState).message}")
                is AuthUiState.LoggedIn ->
                    LaunchedEffect(key1 = Unit) {
                        navController.navigate(TelaInicialScreen)
                    }


            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AuthScreenPreview() {
    QuantoEuDevoTheme {
        AuthScreen(navController = rememberNavController(), authService = AuthService(
            UsuariosService(), LocalContext.current
        ), uiState = AuthUiState.Ready) { }
    }
}