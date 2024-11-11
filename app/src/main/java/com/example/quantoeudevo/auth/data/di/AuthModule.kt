package com.example.quantoeudevo.auth.data.di

import android.content.Context
import com.example.quantoeudevo.core.data.di.UsuariosService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthClient(@ApplicationContext context: Context, usuariosService: UsuariosService): AuthService =
        AuthService(context, usuariosService)
}