package xyz.joaophp.liftin.di.repositories

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import xyz.joaophp.liftin.data.repositories.workout.WorkoutRepository
import xyz.joaophp.liftin.data.repositories.workout.WorkoutRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
interface WorkoutRepositoryModule {

    @Binds
    fun bindWorkoutRepository(
        workoutRepositoryImpl: WorkoutRepositoryImpl
    ): WorkoutRepository
}