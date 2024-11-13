package com.example.quantoeudevo.detalhes_financeiro.data

import com.example.quantoeudevo.auth.data.di.AuthService

sealed interface DetalhesFinanceiroUiEvent {
    data class OnGetFinanceiro(val id: String) : DetalhesFinanceiroUiEvent
}