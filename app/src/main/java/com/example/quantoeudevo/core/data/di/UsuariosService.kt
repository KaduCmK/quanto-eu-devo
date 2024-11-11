package com.example.quantoeudevo.core.data.di

import android.util.Log
import com.example.quantoeudevo.core.data.model.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UsuariosService @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()

    suspend fun setUsuario(usuario: Usuario) {
        try {
            db.collection("users")
                .document(usuario.uid!!)
                .set(usuario)
                .await()

            Log.d("UsuariosService", "Usuario set")
        }
        catch (e: Exception) {
            Log.e("UsuariosService", "Error setting usuario", e)
        }
    }

    suspend fun findUsuario(str: String): List<Usuario> {
        return try {
            val usuarios = db.collection("users")
                .whereGreaterThanOrEqualTo("displayName", str)
                .whereLessThanOrEqualTo("displayName", str + "\uf7ff")
                .get()
                .await()

            usuarios.mapNotNull { user ->
                user.toObject(Usuario::class.java)
            }
        }
        catch (e: Exception) {
            Log.e("UsuariosService", "Error getting usuarios", e)
            emptyList()
        }
    }
}