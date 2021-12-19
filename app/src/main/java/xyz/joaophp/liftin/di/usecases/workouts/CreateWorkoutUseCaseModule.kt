package xyz.joaophp.liftin.di.usecases.workouts

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import xyz.joaophp.liftin.usecases.workouts.CreateWorkoutUseCase
import xyz.joaophp.liftin.usecases.workouts.CreateWorkoutUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
interface CreateWorkoutUseCaseModule {

    @Binds
    fun bindCreateWorkoutUseCase(
        createWorkoutUseCaseImpl: CreateWorkoutUseCaseImpl
    ): CreateWorkoutUseCase
}