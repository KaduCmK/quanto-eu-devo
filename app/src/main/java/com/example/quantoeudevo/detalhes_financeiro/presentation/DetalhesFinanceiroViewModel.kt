package com.example.quantoeudevo.detalhes_financeiro.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quantoeudevo.core.data.di.FinanceiroService
import com.example.quantoeudevo.core.data.dto.EmprestimoFirestore
import com.example.quantoeudevo.core.data.model.TipoEmprestimo
import com.example.quantoeudevo.core.data.model.Usuario
import com.example.quantoeudevo.detalhes_financeiro.data.DetalhesFinanceiroUiEvent
import com.example.quantoeudevo.detalhes_financeiro.data.DetalhesFinanceiroUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

@HiltViewModel
class DetalhesFinanceiroViewModel @Inject constructor(
    private val financeiroService: FinanceiroService
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<DetalhesFinanceiroUiState>(DetalhesFinanceiroUiState.Loading(null))
    val uiState: StateFlow<DetalhesFinanceiroUiState> = _uiState.asStateFlow()

    fun onEvent(uiEvent: DetalhesFinanceiroUiEvent) {
        when (uiEvent) {
            is DetalhesFinanceiroUiEvent.OnSetUsuario -> {
                viewModelScope.launch {
                    _uiState.emit(DetalhesFinanceiroUiState.Loading(uiEvent.user))
                }
            }

            is DetalhesFinanceiroUiEvent.OnGetFinanceiro -> {
                viewModelScope.launch {
                    _uiState.emit(
                        DetalhesFinanceiroUiState.Loaded(
                            currentUser = uiEvent.currentUser,
                            financeiro = financeiroService.getFinanceiroPorId(
                                uiEvent.currentUser,
                                uiEvent.id
                            )
                        )
                    )
                }
            }

            is DetalhesFinanceiroUiEvent.OnAddEmprestimo -> {
                viewModelScope.launch {
                    val outroUsuario =
                        if (uiEvent.financeiro.criador.uid == uiEvent.currentUser.uid)
                            uiEvent.financeiro.outroUsuario
                        else uiEvent.financeiro.criador

                    val emprestimo =
                        if (uiEvent.tipoEmprestimo is TipoEmprestimo.Credito)
                            EmprestimoFirestore(
                                cedente = Usuario(
                                    (_uiState.value as DetalhesFinanceiroUiState.Loaded).currentUser
                                ),
                                recebedor = outroUsuario,
                                valor = uiEvent.valor.toString(),
                                descricao = uiEvent.descricao,
                                dataHora = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                            )
                        else EmprestimoFirestore(
                            cedente = outroUsuario,
                            recebedor = Usuario(
                                (_uiState.value as DetalhesFinanceiroUiState.Loaded).currentUser
                            ),
                            valor = uiEvent.valor.toString(),
                            descricao = uiEvent.descricao,
                            dataHora = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                        )

                    financeiroService.addEmprestimoToFinanceiro(uiEvent.financeiro.id, emprestimo)
                    _uiState.update {
                        (it as DetalhesFinanceiroUiState.Loaded).copy(
                            financeiro = financeiroService.getFinanceiroPorId(
                                uiEvent.currentUser,
                                uiEvent.financeiro.id
                            )
                        )
                    }
                }
            }
        }
    }
}