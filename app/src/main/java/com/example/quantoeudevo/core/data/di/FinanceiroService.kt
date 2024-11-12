package com.example.quantoeudevo.core.data.di

import android.util.Log
import com.example.quantoeudevo.auth.data.di.AuthService
import com.example.quantoeudevo.core.data.dto.FinanceiroDto
import com.example.quantoeudevo.core.data.model.Emprestimo
import com.example.quantoeudevo.core.data.model.EmprestimoFirestore
import com.example.quantoeudevo.core.data.model.Financeiro
import com.example.quantoeudevo.core.data.model.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FinanceiroService @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getFinanceiros(authService: AuthService): List<Financeiro> {
        return try {
            val criadorQuery = db.collection("financeiros")
                .whereEqualTo("criador.uid", authService.getSignedInUser()?.uid)
                .get()
                .await()
            val outroUsuarioQuery = db.collection("financeiros")
                .whereEqualTo("outroUsuario.uid", authService.getSignedInUser()?.uid)
                .get()
                .await()

            val criadorList = criadorQuery.documents.mapNotNull { doc ->
                val dto = doc.toObject(FinanceiroDto::class.java)
                Financeiro(
                    id = doc.id,
                    criador = dto!!.criador!!,
                    pagador = dto.pagador!!,
                    saldo = dto.saldo!!,
                    dataCriacao = dto.dataCriacao!!
                )
            }
            val outroUsuarioList = outroUsuarioQuery.documents.mapNotNull { doc ->
                val dto = doc.toObject(FinanceiroDto::class.java)
                Financeiro(
                    id = doc.id,
                    criador = dto!!.criador as Usuario,
                    pagador = dto.pagador!!,
                    saldo = dto.saldo!!,
                    dataCriacao = dto.dataCriacao!!
                )
            }

            criadorList + outroUsuarioList
        }
        catch (e: Exception) {
            Log.e("FirestoreService", "Error getting financeiros", e)
            emptyList()
        }
    }

    suspend fun getEmprestimosFromFinanceiro(financeiroId: String): List<Emprestimo> {
        val emprestimos = mutableListOf<Emprestimo>()

        val snapshot = db.collection("financeiros")
            .document(financeiroId)
            .collection("emprestimos")
            .get()
            .await()

        snapshot.documents.forEach { doc ->
            val emprestimoFirestore = doc.toObject(EmprestimoFirestore::class.java)
            emprestimoFirestore?.let {
                val emprestimo = when (it.tipoTransacao) {
                    "CREDITO" -> Emprestimo.Credito(
                        cedente = it.cedente,
                        outroUsuario = it.recebedor,
                        valor = it.valor,
                        timestamp = it.dataHora,
                        descricao = it.descricao
                    )
                    "DEBITO" -> Emprestimo.Debito(
                        recebedor = it.recebedor,
                        outroUsuario = it.cedente,
                        valor = it.valor,
                        timestamp = it.dataHora,
                        descricao = it.descricao
                    )
                    else -> throw IllegalArgumentException("Tipo de transação desconhecido")
                }
                emprestimos.add(emprestimo)
            }
        }

        return emprestimos
    }

    suspend fun addFinanceiro(financeiro: FinanceiroDto): Result<Unit> {
        return try {
            val financeiroRef = db.collection("financeiros").document()

            financeiroRef.set(financeiro).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addEmprestimoToFinanceiro(
        financeiroId: String,
        emprestimo: Emprestimo
    ): Result<Unit> {
        val emprestimoFirestore = when (emprestimo) {
            is Emprestimo.Credito -> EmprestimoFirestore(
                cedente = emprestimo.cedente,
                recebedor = emprestimo.outroUsuario,
                valor = emprestimo.valor,
                dataHora = emprestimo.timestamp,
                descricao = emprestimo.descricao,
                tipoTransacao = "CREDITO"
            )
            is Emprestimo.Debito -> EmprestimoFirestore(
                cedente = emprestimo.outroUsuario,
                recebedor = emprestimo.recebedor,
                valor = emprestimo.valor,
                dataHora = emprestimo.timestamp,
                descricao = emprestimo.descricao,
                tipoTransacao = "DEBITO"
            )
        }

        return try {
            val emprestimosRef = db.collection("financeiros")
                .document(financeiroId)
                .collection("emprestimos")
                .document()
            emprestimosRef.set(emprestimoFirestore).await()

            Result.success(Unit)
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }
}