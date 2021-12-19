package xyz.joaophp.liftin.di.usecases.auth

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import xyz.joaophp.liftin.usecases.auth.RegisterUseCase
import xyz.joaophp.liftin.usecases.auth.RegisterUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
interface RegisterUseCaseModule {

    @Binds
    fun bindRegisterUseCase(
        registerUseCaseImpl: RegisterUseCaseImpl
    ): RegisterUseCase
}