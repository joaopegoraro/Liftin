package xyz.joaophp.liftin.di.repositories

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import xyz.joaophp.liftin.data.repositories.exercise.ExerciseRepository
import xyz.joaophp.liftin.data.repositories.exercise.ExerciseRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
interface ExerciseRepositoryModule {

    @Binds
    fun bindExerciseRepository(
        exerciseRepositoryImpl: ExerciseRepositoryImpl
    ): ExerciseRepository
}