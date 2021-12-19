package xyz.joaophp.liftin.di.usecases.auth

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import xyz.joaophp.liftin.usecases.auth.LoginUseCase
import xyz.joaophp.liftin.usecases.auth.LoginUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
interface LoginUseCaseModule {

    @Binds
    fun bindLoginUseCase(
        loginUseCaseImpl: LoginUseCaseImpl
    ): LoginUseCase
}