package xyz.joaophp.liftin.di.services

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import xyz.joaophp.liftin.data.services.database.DatabaseService
import xyz.joaophp.liftin.data.services.database.DatabaseServiceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseServiceModule {

    @Binds
    abstract fun bindDatabaseService(
        databaseServiceImpl: DatabaseServiceImpl
    ): DatabaseService
}