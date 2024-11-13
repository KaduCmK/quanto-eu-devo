package com.example.quantoeudevo.tela_inicial.data.model

import com.example.quantoeudevo.auth.data.di.AuthService
import com.example.quantoeudevo.core.data.model.Financeiro
import com.example.quantoeudevo.core.data.model.TipoEmprestimo
import com.example.quantoeudevo.core.data.model.Usuario
import java.math.BigDecimal

sealed interface TelaInicialUiEvent {
    data class OnCheckLoggedIn(val authService: AuthService) : TelaInicialUiEvent
    data class OnLogout(val authService: AuthService) : TelaInicialUiEvent
    data class onSearchUsers(val query: String) : TelaInicialUiEvent
    data class OnAddFinanceiro(val authService: AuthService, val outroUsuario: Usuario) : TelaInicialUiEvent
    data class OnAddEmprestimo(
        val authService: AuthService,
        val financeiro: Financeiro,
        val tipoEmprestimo: TipoEmprestimo,
        val valor: BigDecimal,
        val descricao: String
    ) : TelaInicialUiEvent

    data object OnClick : TelaInicialUiEvent
    data class OnLongClick(val financeiro: Financeiro) : TelaInicialUiEvent
}