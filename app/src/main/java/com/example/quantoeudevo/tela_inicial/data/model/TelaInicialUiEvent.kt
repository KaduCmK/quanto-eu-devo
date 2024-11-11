package com.example.quantoeudevo.tela_inicial.data.model

sealed interface TelaInicialUiEvent {
    data object OnClick : TelaInicialUiEvent
    data object OnLongClick : TelaInicialUiEvent
}