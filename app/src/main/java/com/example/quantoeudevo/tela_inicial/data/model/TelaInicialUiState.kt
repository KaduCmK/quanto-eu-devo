package com.example.quantoeudevo.tela_inicial.data.model

import com.example.quantoeudevo.core.data.model.Financeiro
import com.example.quantoeudevo.core.data.model.Usuario

sealed class TelaInicialUiState {
    data object Unauthorized : TelaInicialUiState()
    data object Loading : TelaInicialUiState()
    data class Loaded(val usuario: Usuario?, val financeiros: List<Financeiro>) : TelaInicialUiState()
    data class Error(val error: String) : TelaInicialUiState()
}