package xyz.joaophp.liftin.di.repositories

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import xyz.joaophp.liftin.data.repositories.image.ImageRepository
import xyz.joaophp.liftin.data.repositories.image.ImageRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
interface ImageRepositoryModule {

    @Binds
    fun bindImageRepository(
        imageRepositoryImpl: ImageRepositoryImpl
    ): ImageRepository
}