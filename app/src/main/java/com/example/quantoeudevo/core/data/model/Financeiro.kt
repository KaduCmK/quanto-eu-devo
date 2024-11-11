package com.example.quantoeudevo.core.data.model

data class Financeiro(
    val id: String,
    val criador: Usuario,
    val pagador: Usuario,
    val saldo: List<Emprestimo>,
    val dataCriacao: Long
)
