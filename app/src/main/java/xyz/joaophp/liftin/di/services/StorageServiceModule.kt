package xyz.joaophp.liftin.di.services

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import xyz.joaophp.liftin.data.services.storage.StorageService
import xyz.joaophp.liftin.data.services.storage.StorageServiceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageServiceModule {

    @Binds
    abstract fun bindStorageService(
        storageServiceImpl: StorageServiceImpl
    ): StorageService
}