package com.example.quantoeudevo.auth.data.model

import com.example.quantoeudevo.auth.data.di.AuthService

sealed interface AuthUiEvent {
    data class OnGoogleSignIn(val authService: AuthService) : AuthUiEvent
}