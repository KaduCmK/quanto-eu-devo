package com.example.quantoeudevo.detalhes_financeiro.data

import com.example.quantoeudevo.core.data.model.Financeiro

sealed class DetalhesFinanceiroUiState {
    data object Loading : DetalhesFinanceiroUiState()
    data class Loaded(val financeiro: Financeiro) : DetalhesFinanceiroUiState()
}