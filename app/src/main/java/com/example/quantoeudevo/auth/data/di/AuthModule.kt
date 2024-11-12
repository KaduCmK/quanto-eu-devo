package com.example.quantoeudevo.auth.data.di

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModel
import com.example.quantoeudevo.core.data.di.UsuariosService
import com.example.quantoeudevo.core.data.model.Usuario
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
object AuthModule {

    @Provides
    fun provideAuthClient(usuarioService: UsuariosService, @ActivityContext context: Context) =
        AuthService(usuarioService, context)
}