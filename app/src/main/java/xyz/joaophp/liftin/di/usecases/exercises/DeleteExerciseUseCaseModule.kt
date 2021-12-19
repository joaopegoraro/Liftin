package xyz.joaophp.liftin.di.usecases.exercises

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import xyz.joaophp.liftin.usecases.exercises.DeleteExerciseUseCase
import xyz.joaophp.liftin.usecases.exercises.DeleteExerciseUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
interface DeleteExerciseUseCaseModule {

    @Binds
    fun bindDeleteExerciseUseCase(
        deleteExerciseUseCaseImpl: DeleteExerciseUseCaseImpl
    ): DeleteExerciseUseCase
}