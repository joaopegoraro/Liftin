package xyz.joaophp.liftin.di.usecases.auth

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import xyz.joaophp.liftin.usecases.auth.GetUserUseCase
import xyz.joaophp.liftin.usecases.auth.GetUserUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
interface GetUserUseCaseModule {

    @Binds
    fun bindGetUserUseCase(
        getUserUseCaseImpl: GetUserUseCaseImpl
    ): GetUserUseCase
}