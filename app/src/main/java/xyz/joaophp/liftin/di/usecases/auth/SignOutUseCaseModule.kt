package xyz.joaophp.liftin.di.usecases.auth

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import xyz.joaophp.liftin.usecases.auth.SignOutUseCase
import xyz.joaophp.liftin.usecases.auth.SignOutUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
interface SignOutUseCaseModule {

    @Binds
    fun bindSignOutUseCase(
        signOutUseCaseImpl: SignOutUseCaseImpl
    ): SignOutUseCase
}