package com.example.quantoeudevo.auth.presentation

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quantoeudevo.auth.data.di.AuthService
import com.example.quantoeudevo.auth.data.model.AuthUiEvent
import com.example.quantoeudevo.auth.data.model.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
) : ViewModel() {
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Ready)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onEvent(uiEvent: AuthUiEvent) {
        when (uiEvent) {
            is AuthUiEvent.OnGoogleSignIn -> {
                viewModelScope.launch {
                    _uiState.emit(AuthUiState.Loading)

                    val currentUser = uiEvent.authService.getSignedInUser()
                    if (currentUser != null) {
//                        Toast.makeText(
//                            context,
//                            "Signed in as ${currentUser.displayName}",
//                            Toast.LENGTH_SHORT
//                        ).show()
                        _uiState.emit(
                            AuthUiState.LoggedIn(
                                uiEvent.authService.getSignedInUser()?.displayName ?: ""
                            )
                        )
                        return@launch
                    }

                    uiEvent.authService.googleSignIn().collect { result ->
                        // Handle the result
                        result.fold(
                            onSuccess = { authResult ->
                                _uiState.emit(AuthUiState.LoggedIn(authResult.user?.uid ?: ""))
                            },
                            onFailure = { exception ->
                                _uiState.emit(
                                    AuthUiState.Error(
                                        exception.message ?: "An error occurred"
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}