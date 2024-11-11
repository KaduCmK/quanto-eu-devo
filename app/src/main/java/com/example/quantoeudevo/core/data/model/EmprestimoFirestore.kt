package com.example.quantoeudevo.core.data.model

data class EmprestimoFirestore(
    val cedente: Usuario,
    val recebedor: Usuario,
    val valor: Double,
    val dataHora: Long,
    val descricao: String,
    val tipoTransacao: String // "CREDITO" ou "DEBITO"
)

