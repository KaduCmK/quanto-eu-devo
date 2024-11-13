package com.example.quantoeudevo.detalhes_financeiro.data

import com.example.quantoeudevo.auth.data.di.AuthService

sealed interface DetalhesFinanceiroUiEvent {
    data class OnGetFinanceiro(val authService: AuthService, val id: String) : DetalhesFinanceiroUiEvent
}