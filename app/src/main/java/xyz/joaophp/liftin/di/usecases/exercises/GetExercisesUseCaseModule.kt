package xyz.joaophp.liftin.di.usecases.exercises

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import xyz.joaophp.liftin.usecases.exercises.GetExercisesUseCase
import xyz.joaophp.liftin.usecases.exercises.GetExercisesUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
interface GetExercisesUseCaseModule {

    @Binds
    fun bindGetExerciseUseCase(
        getExercisesUseCaseImpl: GetExercisesUseCaseImpl
    ): GetExercisesUseCase
}