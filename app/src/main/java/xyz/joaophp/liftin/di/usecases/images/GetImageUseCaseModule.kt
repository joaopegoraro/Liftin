package xyz.joaophp.liftin.di.usecases.images

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import xyz.joaophp.liftin.usecases.images.GetImageUseCase
import xyz.joaophp.liftin.usecases.images.GetImageUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
interface GetImageUseCaseModule {

    @Binds
    fun bindGetImageUseCase(
        getImageUseCaseImpl: GetImageUseCaseImpl
    ): GetImageUseCase
}