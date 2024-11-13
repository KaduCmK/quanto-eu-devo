package com.example.quantoeudevo.core.data.model

import java.math.BigDecimal


sealed class Emprestimo {
    abstract val id: String
    abstract val valor: BigDecimal
    abstract val descricao: String
    abstract val timestamp: Long
    abstract val outroUsuario: Usuario

    data class Credito(
        override val id: String,
        val cedente: Usuario,
        override val valor: BigDecimal,
        override val descricao: String,
        override val timestamp: Long,
        override val outroUsuario: Usuario
    ) : Emprestimo()

    data class Debito(
        override val id: String,
        val recebedor: Usuario,
        override val valor: BigDecimal,
        override val descricao: String,
        override val timestamp: Long,
        override val outroUsuario: Usuario
    ) : Emprestimo()
}