package com.example.quantoeudevo.tela_inicial.data.model

sealed interface TelaInicialUiEvent {
    data object OnFinanceiroClick : TelaInicialUiEvent
    data object onLongClick : TelaInicialUiEvent
}