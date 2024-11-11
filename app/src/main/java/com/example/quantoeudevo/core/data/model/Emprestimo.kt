package com.example.quantoeudevo.core.data.model

sealed class Emprestimo {
    abstract val valor: Double
    abstract val descricao: String
    abstract val timestamp: Long
    abstract val outroUsuario: Usuario

    data class Credito(
        val cedente: Usuario,
        override val valor: Double,
        override val descricao: String,
        override val timestamp: Long,
        override val outroUsuario: Usuario
    ) : Emprestimo()

    data class Debito(
        val recebedor: Usuario,
        override val valor: Double,
        override val descricao: String,
        override val timestamp: Long,
        override val outroUsuario: Usuario
    ) : Emprestimo()
}