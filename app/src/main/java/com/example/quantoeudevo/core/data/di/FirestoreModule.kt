package com.example.quantoeudevo.core.data.di

import com.example.quantoeudevo.auth.data.di.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirestoreModule {

    @Provides
    @Singleton
    fun provideFirestoreService(authService: AuthService): FinanceiroService =
        FinanceiroService(authService)

    @Provides
    @Singleton
    fun provideUsuariosService(): UsuariosService =
        UsuariosService()
}