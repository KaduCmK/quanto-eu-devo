package com.example.quantoeudevo.core.model

sealed class Emprestimo {
    abstract val id: String
    abstract val valor: Double
    abstract val descricao: String
}

data class Credito(
    override val id: String,
    override val valor: Double,
    override val descricao: String
) : Emprestimo()

data class Divida(
    override val id: String,
    override val valor: Double,
    override val descricao: String
) : Emprestimo()