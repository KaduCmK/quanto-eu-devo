package com.example.quantoeudevo.tela_inicial.presentation

import androidx.lifecycle.ViewModel
import com.example.quantoeudevo.tela_inicial.data.model.TelaInicialUiEvent
import com.example.quantoeudevo.tela_inicial.data.model.TelaInicialUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class TelaInicialViewModel @Inject constructor() : ViewModel() {

    private val _uiState: MutableStateFlow<TelaInicialUiState> =
        MutableStateFlow(TelaInicialUiState.Loading)
    val uiState: StateFlow<TelaInicialUiState> = _uiState

    fun onEvent(uiEvent: TelaInicialUiEvent) {
        when (uiEvent) {
            is TelaInicialUiEvent.OnClick -> {

            }

            is TelaInicialUiEvent.OnLongClick -> {

            }
        }

    }
}