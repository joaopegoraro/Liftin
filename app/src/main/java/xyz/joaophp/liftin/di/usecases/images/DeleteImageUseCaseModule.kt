package xyz.joaophp.liftin.di.usecases.images

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import xyz.joaophp.liftin.usecases.images.DeleteImageUseCase
import xyz.joaophp.liftin.usecases.images.DeleteImageUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
interface DeleteImageUseCaseModule {

    @Binds
    fun bindDeleteImageUseCase(
        deleteImageUseCaseImpl: DeleteImageUseCaseImpl
    ): DeleteImageUseCase
}