package com.example.quantoeudevo.core.data.di

import android.util.Log
import com.example.quantoeudevo.auth.data.di.AuthService
import com.example.quantoeudevo.core.data.dto.FinanceiroFirestore
import com.example.quantoeudevo.core.data.model.Emprestimo
import com.example.quantoeudevo.core.data.dto.EmprestimoFirestore
import com.example.quantoeudevo.core.data.model.Financeiro
import com.example.quantoeudevo.core.data.model.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
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
                val dto = doc.toObject(FinanceiroFirestore::class.java)
                Log.d("FirestoreService", "dto: $dto")

                val listEmprestimos = doc.reference.collection("emprestimos").get()
                    .await().documents.mapNotNull { emp ->
                        val empDto = emp.toObject(EmprestimoFirestore::class.java)
                        if (empDto?.cedente?.uid == authService.getSignedInUser()?.uid)
                            Emprestimo.Credito(
                                id = emp.id,
                                cedente = empDto?.recebedor!!,
                                outroUsuario = empDto.cedente!!,
                                valor = empDto.valor!!.toBigDecimal(),
                                timestamp = empDto.dataHora!!,
                                descricao = empDto.descricao!!
                            )
                        else if (empDto?.recebedor?.uid == authService.getSignedInUser()?.uid)
                            Emprestimo.Debito(
                                id = emp.id,
                                outroUsuario = empDto?.cedente!!,
                                recebedor = empDto.recebedor!!,
                                valor = empDto.valor!!.toBigDecimal(),
                                timestamp = empDto.dataHora!!,
                                descricao = empDto.descricao!!
                            )
                        else null
                    }

                Log.d("FirestoreService", "dto: $dto")
                Financeiro(
                    id = doc.id,
                    criador = dto!!.criador!!,
                    outroUsuario = dto.outroUsuario!!,
                    saldo = listEmprestimos,
                    dataCriacao = dto.dataCriacao!!
                )
            }
            val outroUsuarioList = outroUsuarioQuery.documents.mapNotNull { doc ->
                val dto = doc.toObject(FinanceiroFirestore::class.java)

                val listEmprestimos = doc.reference.collection("emprestimos").get()
                    .await().documents.mapNotNull { emp ->
                        val empDto = emp.toObject(EmprestimoFirestore::class.java)
                        if (empDto?.cedente?.uid == authService.getSignedInUser()?.uid)
                            Emprestimo.Credito(
                                id = emp.id,
                                cedente = empDto?.recebedor!!,
                                outroUsuario = empDto.cedente!!,
                                valor = empDto.valor!!.toBigDecimal(),
                                timestamp = empDto.dataHora!!,
                                descricao = empDto.descricao!!
                            )
                        else if (empDto?.recebedor?.uid == authService.getSignedInUser()?.uid)
                            Emprestimo.Debito(
                                id = emp.id,
                                outroUsuario = empDto?.cedente!!,
                                recebedor = empDto.recebedor!!,
                                valor = empDto.valor!!.toBigDecimal(),
                                timestamp = empDto.dataHora!!,
                                descricao = empDto.descricao!!
                            )
                        else null
                    }

                Financeiro(
                    id = doc.id,
                    criador = dto!!.criador as Usuario,
                    outroUsuario = dto.outroUsuario!!,
                    saldo = listEmprestimos,
                    dataCriacao = dto.dataCriacao!!
                )
            }

            criadorList + outroUsuarioList
        } catch (e: Exception) {
            Log.e("FirestoreService", "Error getting financeiros", e)
            emptyList()
        }
    }

    suspend fun getFinanceiroPorId(id: String): Financeiro {
        val financeiroRef = db.collection("financeiros")
            .document(id)
            .get()
            .await()
        val dto = financeiroRef.toObject(FinanceiroFirestore::class.java)

        return Financeiro(
            id = financeiroRef.id,
            criador = dto!!.criador!!,
            outroUsuario = dto.outroUsuario!!,
            saldo = emptyList(),
            dataCriacao = dto.dataCriacao!!
        )
    }

    suspend fun deleteFinanceiro(id: String): Result<Unit> {
        return try {
            val financeiroRef = db.collection("financeiros").document(id)

            financeiroRef.delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

//        suspend fun getEmprestimosFromFinanceiro(financeiroId: String): List<Emprestimo> {
//            val emprestimos = mutableListOf<Emprestimo>()
//
//            val snapshot = db.collection("financeiros")
//                .document(financeiroId)
//                .collection("emprestimos")
//                .get()
//                .await()
//
//            snapshot.documents.forEach { doc ->
//                val emprestimoFirestore = doc.toObject(EmprestimoFirestore::class.java)
//                emprestimoFirestore?.let { empDto ->
//                    val emprestimo = if (empDto.cedente?.uid == authService.getSignedInUser()?.uid)
//                        Emprestimo.Credito(
//                            id = doc.id,
//                            cedente = empDto.recebedor!!,
//                            outroUsuario = empDto.cedente!!,
//                            valor = empDto.valor!!.toBigDecimal(),
//                            timestamp = empDto.dataHora!!,
//                            descricao = empDto.descricao!!
//                        )
//                    else if (empDto.recebedor?.uid == authService.getSignedInUser()?.uid)
//                        Emprestimo.Debito(
//                            id = doc.id,
//                            outroUsuario = empDto.cedente!!,
//                            recebedor = empDto.recebedor!!,
//                            valor = empDto.valor!!.toBigDecimal(),
//                            timestamp = empDto.dataHora!!,
//                            descricao = empDto.descricao!!
//                        )
//                    else null
//
//                    emprestimos.add(emprestimo!!)
//                }
//            }
//
//            return emprestimos
//        }

    suspend fun addFinanceiro(financeiro: FinanceiroFirestore): Result<Unit> {
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
        emprestimo: EmprestimoFirestore
    ): Result<Unit> {

        return try {
            val emprestimosRef = db.collection("financeiros")
                .document(financeiroId)
                .collection("emprestimos")
                .document()
            emprestimosRef.set(emprestimo).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}