package com.example.quantoeudevo.core.data.model

data class Financeiro(
    val id: String,
    val criador: Usuario,
    val outroUsuario: Usuario,
    val saldo: List<Emprestimo>,
    val dataCriacao: Long
)
