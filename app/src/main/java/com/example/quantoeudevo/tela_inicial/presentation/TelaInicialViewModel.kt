package com.example.quantoeudevo.tela_inicial.presentation

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quantoeudevo.auth.data.di.AuthService
import com.example.quantoeudevo.core.data.di.FinanceiroService
import com.example.quantoeudevo.core.data.di.UsuariosService
import com.example.quantoeudevo.core.data.dto.FinanceiroDto
import com.example.quantoeudevo.core.data.model.Usuario
import com.example.quantoeudevo.tela_inicial.data.model.TelaInicialUiEvent
import com.example.quantoeudevo.tela_inicial.data.model.TelaInicialUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TelaInicialViewModel @Inject constructor(
    private val authService: AuthService,
    private val financeiroService: FinanceiroService,
    private val usuariosService: UsuariosService,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState: MutableStateFlow<TelaInicialUiState> =
        MutableStateFlow(TelaInicialUiState.Loaded(null, emptyList()))
    val uiState: StateFlow<TelaInicialUiState> = _uiState.asStateFlow()

    fun onEvent(uiEvent: TelaInicialUiEvent) {
        when (uiEvent) {
            is TelaInicialUiEvent.OnCheckLoggedIn -> {
                viewModelScope.launch {
                    _uiState.emit(TelaInicialUiState.Loading)

                    if (authService.getSignedInUser() == null) {
                        _uiState.value = TelaInicialUiState.Unauthorized
                        Toast.makeText(context, "Unauthorized", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        _uiState.emit(TelaInicialUiState.Loaded(
                            Usuario(authService.getSignedInUser()!!),
                            financeiroService.getFinanceiros()
                        ))
                        Toast.makeText(
                            context,
                            "Signed in as ${authService.getSignedInUser()?.displayName}",
                            Toast.LENGTH_SHORT
                        ).show()
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
                            financeiros = financeiroService.getFinanceiros()
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

            }

            is TelaInicialUiEvent.OnLogout -> {
                viewModelScope.launch {
                    authService.signOut()
                    _uiState.value = TelaInicialUiState.Unauthorized
                    Toast.makeText(context, "Signed out", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}