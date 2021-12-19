package xyz.joaophp.liftin.di.usecases.workouts

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import xyz.joaophp.liftin.usecases.workouts.DeleteWorkoutUseCase
import xyz.joaophp.liftin.usecases.workouts.DeleteWorkoutUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
interface DeleteWorkoutUseCaseModule {

    @Binds
    fun bindDeleteWorkoutUseCase(
        deleteWorkoutUseCaseImpl: DeleteWorkoutUseCaseImpl
    ): DeleteWorkoutUseCase
}