package com.example.quantoeudevo.tela_inicial.data.model

import com.example.quantoeudevo.core.model.Financeiro

sealed class TelaInicialUiState {
    data object Loading : TelaInicialUiState()
    data class Loaded(val financeiros: List<Financeiro>) : TelaInicialUiState()
    data class Error(val error: String) : TelaInicialUiState()
}