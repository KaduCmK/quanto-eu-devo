package com.example.quantoeudevo.core.data.dto

import com.example.quantoeudevo.core.data.model.Emprestimo
import com.example.quantoeudevo.core.data.model.Usuario

data class FinanceiroDto(
    val criador: Usuario? = null,
    val pagador: Usuario? = null,
    val saldo: List<Emprestimo>? = null,
    val dataCriacao: Long? = null
)