package com.example.quantoeudevo.tela_inicial.data.model

sealed interface TelaInicialUiEvent {
    data object OnCheckLoggedIn : TelaInicialUiEvent
    data object OnLogout : TelaInicialUiEvent
    data object OnAddFinanceiro : TelaInicialUiEvent
    data object OnClick : TelaInicialUiEvent
    data object OnLongClick : TelaInicialUiEvent
}