package com.example.quantoeudevo.tela_inicial.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quantoeudevo.core.data.di.FinanceiroService
import com.example.quantoeudevo.core.data.di.UsuariosService
import com.example.quantoeudevo.core.data.dto.EmprestimoFirestore
import com.example.quantoeudevo.core.data.dto.FinanceiroFirestore
import com.example.quantoeudevo.core.data.model.Emprestimo
import com.example.quantoeudevo.core.data.model.TipoEmprestimo
import com.example.quantoeudevo.core.data.model.Usuario
import com.example.quantoeudevo.tela_inicial.data.model.TelaInicialUiEvent
import com.example.quantoeudevo.tela_inicial.data.model.TelaInicialUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TelaInicialViewModel @Inject constructor(
    private val financeiroService: FinanceiroService,
    private val usuariosService: UsuariosService,
) : ViewModel() {

    private val _uiState: MutableStateFlow<TelaInicialUiState> =
        MutableStateFlow(TelaInicialUiState.Loaded(null, emptyList(), emptyList()))
    val uiState: StateFlow<TelaInicialUiState> = _uiState.asStateFlow()

    fun onEvent(uiEvent: TelaInicialUiEvent) {
        when (uiEvent) {
            is TelaInicialUiEvent.OnCheckLoggedIn -> {
                viewModelScope.launch {
                    _uiState.emit(TelaInicialUiState.Loading)

                    if (uiEvent.authService.getSignedInUser() == null) {
                        _uiState.value = TelaInicialUiState.Unauthorized
//                        Toast.makeText(context, "Unauthorized", Toast.LENGTH_SHORT).show()
                    } else {
                        _uiState.emit(
                            TelaInicialUiState.Loaded(
                                usuario = Usuario(uiEvent.authService.getSignedInUser()!!),
                                usuarios = emptyList(),
                                financeiros = financeiroService.getFinanceiros(uiEvent.authService)
                            )
                        )
                    }
                }
            }

            is TelaInicialUiEvent.onSearchUsers -> {
                viewModelScope.launch {
                    val usuarios = usuariosService.findUsuario(uiEvent.query)
                    Log.d("TelaInicialViewModel", "Usuarios: $usuarios")
                    _uiState.update {
                        (it as TelaInicialUiState.Loaded).copy(usuarios = usuarios)
                    }
                }
            }

            is TelaInicialUiEvent.OnAddFinanceiro -> {
                viewModelScope.launch {
                    val financeiro = FinanceiroFirestore(
                        criador = (_uiState.value as TelaInicialUiState.Loaded).usuario!!,
                        outroUsuario = uiEvent.outroUsuario,
                        dataCriacao = System.currentTimeMillis()
                    )
                    financeiroService.addFinanceiro(financeiro)
                    _uiState.update {
                        (it as TelaInicialUiState.Loaded).copy(
                            financeiros = financeiroService.getFinanceiros(uiEvent.authService)
                        )
                    }
                }
            }

            is TelaInicialUiEvent.OnAddEmprestimo -> {
                viewModelScope.launch {
                    val outroUsuario = if (uiEvent.financeiro.criador.uid == uiEvent.authService.getSignedInUser()?.uid)
                        uiEvent.financeiro.outroUsuario
                    else uiEvent.financeiro.criador
                    val emprestimo =
                        if (uiEvent.tipoEmprestimo is TipoEmprestimo.Credito)
                            EmprestimoFirestore(
                                cedente = (_uiState.value as TelaInicialUiState.Loaded).usuario!!,
                                recebedor = outroUsuario,
                                valor = uiEvent.valor.toString(),
                                descricao = uiEvent.descricao,
                                dataHora = System.currentTimeMillis(),
                                tipoTransacao = "CREDITO"
                            )
                        else EmprestimoFirestore(
                            cedente = outroUsuario,
                            recebedor = (_uiState.value as TelaInicialUiState.Loaded).usuario!!,
                            valor = uiEvent.valor.toString(),
                            descricao = uiEvent.descricao,
                            dataHora = System.currentTimeMillis(),
                            tipoTransacao = "DEBITO"
                        )

                    financeiroService.addEmprestimoToFinanceiro(uiEvent.financeiro.id, emprestimo)
                    _uiState.update {
                        (it as TelaInicialUiState.Loaded).copy(
                            financeiros = financeiroService.getFinanceiros(uiEvent.authService)
                        )
                    }
                }
            }

            is TelaInicialUiEvent.OnClick -> {
                viewModelScope.launch {
                    val usuarios = usuariosService.findUsuario("C")

                    Log.d("TelaInicialViewModel", "Usuarios: $usuarios")
                }
            }

            is TelaInicialUiEvent.OnLongClick -> {
                viewModelScope.launch {
//                    _uiState.emit(TelaInicialUiState.Loading)
                    financeiroService.deleteFinanceiro(uiEvent.financeiro.id)
                    _uiState.update {
                        (it as TelaInicialUiState.Loaded).copy(
                            financeiros = (_uiState.value as TelaInicialUiState.Loaded)
                                .financeiros
                                .filter { f -> f.id != uiEvent.financeiro.id }
                        )
                    }
                }
            }

            is TelaInicialUiEvent.OnLogout -> {
                viewModelScope.launch {
                    uiEvent.authService.signOut()
                    _uiState.value = TelaInicialUiState.Unauthorized
                }
            }
        }

    }
}