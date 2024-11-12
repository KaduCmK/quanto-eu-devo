package com.example.quantoeudevo.tela_inicial.data.model

import androidx.credentials.CredentialManager
import com.example.quantoeudevo.auth.data.di.AuthService
import com.example.quantoeudevo.core.data.model.Usuario

sealed interface TelaInicialUiEvent {
    data class OnCheckLoggedIn(val authService: AuthService) : TelaInicialUiEvent
    data class OnLogout(val authService: AuthService) : TelaInicialUiEvent
    data class onSearchUsers(val query: String) : TelaInicialUiEvent
    data class OnAddFinanceiro(val authService: AuthService) : TelaInicialUiEvent
    data object OnClick : TelaInicialUiEvent
    data object OnLongClick : TelaInicialUiEvent
}