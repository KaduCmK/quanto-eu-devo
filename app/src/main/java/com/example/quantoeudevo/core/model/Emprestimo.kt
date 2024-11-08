package com.example.quantoeudevo.core.model

sealed class Emprestimo(open val valor: Double) {
    data class Divida(override val valor: Double, val descricao: String) : Emprestimo(valor)
    data class Credito(override val valor:Double, val descricao: String) : Emprestimo(valor)
}