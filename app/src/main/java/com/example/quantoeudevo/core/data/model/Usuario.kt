package com.example.quantoeudevo.core.data.model

import com.google.firebase.auth.FirebaseUser

data class Usuario(
    val uid: String? = null,
    val photoUrl: String? = null,
    val displayName: String? = null,
    val email: String? = null
) {
    constructor(usuarioFirebase: FirebaseUser) : this(
        uid = usuarioFirebase.uid,
        photoUrl = usuarioFirebase.photoUrl.toString(),
        displayName = usuarioFirebase.displayName ?: "",
        email = usuarioFirebase.email ?: ""
    )
}