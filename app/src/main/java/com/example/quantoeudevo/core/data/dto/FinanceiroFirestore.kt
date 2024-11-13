package com.example.quantoeudevo.core.data.dto

import com.example.quantoeudevo.core.data.model.Usuario

data class FinanceiroFirestore(
    val criador: Usuario? = null,
    val outroUsuario: Usuario? = null,
    val dataCriacao: Long? = null
)