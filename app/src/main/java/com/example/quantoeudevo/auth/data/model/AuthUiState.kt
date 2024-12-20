package com.example.quantoeudevo.auth.data.model

sealed class AuthUiState {
    data object Ready : AuthUiState()
    data object Loading : AuthUiState()
    data class LoggedIn(val email: String) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}