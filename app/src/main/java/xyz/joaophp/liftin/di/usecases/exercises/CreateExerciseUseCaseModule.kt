package xyz.joaophp.liftin.di.usecases.exercises

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import xyz.joaophp.liftin.usecases.exercises.CreateExerciseUseCase
import xyz.joaophp.liftin.usecases.exercises.CreateExerciseUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
interface CreateExerciseUseCaseModule {

    @Binds
    fun bindCreateExerciseUseCase(
        createExerciseUseCaseImpl: CreateExerciseUseCaseImpl
    ): CreateExerciseUseCase
}