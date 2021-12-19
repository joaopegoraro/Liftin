package xyz.joaophp.liftin.di.services

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import xyz.joaophp.liftin.data.services.auth.AuthService
import xyz.joaophp.liftin.data.services.auth.AuthServiceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthServiceModule {

    @Binds
    abstract fun bindAuthService(
        authServiceImpl: AuthServiceImpl
    ): AuthService
}