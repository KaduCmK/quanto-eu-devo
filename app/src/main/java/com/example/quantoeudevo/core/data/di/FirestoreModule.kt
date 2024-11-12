package com.example.quantoeudevo.core.data.di

import com.example.quantoeudevo.auth.data.di.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirestoreModule {

    @Provides
    fun provideFirestoreService(): FinanceiroService =
        FinanceiroService()

    @Provides
    fun provideUsuariosService(): UsuariosService =
        UsuariosService()
}