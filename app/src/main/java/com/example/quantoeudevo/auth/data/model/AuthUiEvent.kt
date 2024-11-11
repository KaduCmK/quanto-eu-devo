package com.example.quantoeudevo.auth.data.model

sealed interface AuthUiEvent {
    data object OnGoogleSignIn : AuthUiEvent
}