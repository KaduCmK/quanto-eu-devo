package com.example.quantoeudevo.detalhes_financeiro.data

import com.example.quantoeudevo.core.data.model.Financeiro
import com.example.quantoeudevo.core.data.model.TipoEmprestimo
import com.google.firebase.auth.FirebaseUser
import java.math.BigDecimal

sealed interface DetalhesFinanceiroUiEvent {
    data class OnSetUsuario(val user: FirebaseUser) : DetalhesFinanceiroUiEvent
    data class OnGetFinanceiro(val currentUser: FirebaseUser, val id: String) :
        DetalhesFinanceiroUiEvent

    data class OnAddEmprestimo(
        val currentUser: FirebaseUser,
        val financeiro: Financeiro,
        val tipoEmprestimo: TipoEmprestimo,
        val valor: BigDecimal,
        val descricao: String
    ) :
        DetalhesFinanceiroUiEvent
}