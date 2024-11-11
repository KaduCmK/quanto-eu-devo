package com.example.quantoeudevo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.quantoeudevo.auth.presentation.AuthScreenRoot
import com.example.quantoeudevo.detalhes_financeiro.presentation.DetalhesFinanceiroScreenRoot
import com.example.quantoeudevo.tela_inicial.presentation.TelaInicialScreenRoot
import com.example.quantoeudevo.ui.theme.QuantoEuDevoTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            QuantoEuDevoTheme {
               NavHost(navController = navController, startDestination = TelaInicialScreen) {
                   composable<AuthScreen> {
                       AuthScreenRoot(navController = navController)
                   }

                    composable<TelaInicialScreen> {
                        TelaInicialScreenRoot(
                            navController = navController
                        )
                    }

                   composable<DetalhesFinanceiroScreen> {
                       val args = it.toRoute<DetalhesFinanceiroScreen>()
                       DetalhesFinanceiroScreenRoot(id = args.id)
                   }
                }
            }
        }
    }
}

@Serializable
object AuthScreen

@Serializable
object TelaInicialScreen

@Serializable
data class DetalhesFinanceiroScreen(val id: String)