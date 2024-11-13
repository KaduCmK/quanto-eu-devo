package com.example.quantoeudevo.detalhes_financeiro.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quantoeudevo.core.data.di.FinanceiroService
import com.example.quantoeudevo.core.data.model.Financeiro
import com.example.quantoeudevo.core.data.model.Usuario
import com.example.quantoeudevo.detalhes_financeiro.data.DetalhesFinanceiroUiEvent
import com.example.quantoeudevo.detalhes_financeiro.data.DetalhesFinanceiroUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetalhesFinanceiroViewModel @Inject constructor(
    private val financeiroService: FinanceiroService
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<DetalhesFinanceiroUiState>(DetalhesFinanceiroUiState.Loading)
    val uiState: StateFlow<DetalhesFinanceiroUiState> = _uiState.asStateFlow()

    fun onEvent(uiEvent: DetalhesFinanceiroUiEvent) {
        when (uiEvent) {
            is DetalhesFinanceiroUiEvent.OnGetFinanceiro -> {
                viewModelScope.launch {
//                    _uiState.emit(DetalhesFinanceiroUiState.Loading)

                    _uiState.emit(
                        DetalhesFinanceiroUiState.Loaded(
                            financeiro = financeiroService.getFinanceiroPorId(uiEvent.id)
                        )
                    )
                }
            }
        }
    }
}