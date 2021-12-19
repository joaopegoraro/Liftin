package xyz.joaophp.liftin.di.usecases.workouts

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import xyz.joaophp.liftin.usecases.workouts.GetWorkoutsUseCase
import xyz.joaophp.liftin.usecases.workouts.GetWorkoutsUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
interface GetWorkoutsUseCaseModule {

    @Binds
    fun bindGetWorkoutsUseCase(
        getWorkoutsUseCaseImpl: GetWorkoutsUseCaseImpl
    ): GetWorkoutsUseCase
}