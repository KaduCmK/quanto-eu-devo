package com.example.quantoeudevo.core.data.dto

import com.example.quantoeudevo.core.data.model.Usuario

data class EmprestimoFirestore(
    val cedente: Usuario? = null,
    val recebedor: Usuario? = null,
    val valor: String? = null,
    val dataHora: Long? = null,
    val descricao: String? = null,
    val tipoTransacao: String? = null // "CREDITO" ou "DEBITO"
)

