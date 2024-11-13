package com.example.quantoeudevo.tela_inicial.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quantoeudevo.core.data.di.FinanceiroService
import com.example.quantoeudevo.core.data.di.UsuariosService
import com.example.quantoeudevo.core.data.dto.FinanceiroDto
import com.example.quantoeudevo.core.data.model.Usuario
import com.example.quantoeudevo.tela_inicial.data.model.TelaInicialUiEvent
import com.example.quantoeudevo.tela_inicial.data.model.TelaInicialUiState
import dagger.hilt.android.lifecycle.HiltViewModel
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
                    }
                    else {
                        _uiState.emit(TelaInicialUiState.Loaded(
                            Usuario(uiEvent.authService.getSignedInUser()!!),
                            emptyList(),
                            financeiroService.getFinanceiros(uiEvent.authService)
                        ))
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
                    val financeiro = FinanceiroDto(
                        criador = (_uiState.value as TelaInicialUiState.Loaded).usuario!!,
                        pagador = (_uiState.value as TelaInicialUiState.Loaded).usuario!!,
                        saldo = emptyList(),
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