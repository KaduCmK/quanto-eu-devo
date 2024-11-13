package com.example.quantoeudevo.core.data.model

sealed class TipoEmprestimo {
    data object Credito : TipoEmprestimo()
    data object Debito : TipoEmprestimo()
}