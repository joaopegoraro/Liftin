package xyz.joaophp.liftin.di.usecases.images

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import xyz.joaophp.liftin.usecases.images.CreateImageUseCase
import xyz.joaophp.liftin.usecases.images.CreateImageUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
interface CreateImageUseCaseModule {

    @Binds
    fun bindCreateImageUseCase(
        createImageUseCaseImpl: CreateImageUseCaseImpl
    ): CreateImageUseCase
}