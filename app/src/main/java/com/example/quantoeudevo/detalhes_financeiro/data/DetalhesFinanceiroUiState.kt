package com.example.quantoeudevo.detalhes_financeiro.data

import com.example.quantoeudevo.core.data.model.Financeiro
import com.google.firebase.auth.FirebaseUser

sealed class DetalhesFinanceiroUiState {
    abstract val currentUser: FirebaseUser?

    data class Loading(override val currentUser: FirebaseUser?) : DetalhesFinanceiroUiState()
    data class Loaded(override val currentUser: FirebaseUser, val financeiro: Financeiro) :
        DetalhesFinanceiroUiState()
}