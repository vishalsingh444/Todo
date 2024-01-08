package com.vishalsingh444888.todo.di

import android.app.Application
import androidx.room.Room
import com.vishalsingh444888.todo.data.TodoDatabase
import com.vishalsingh444888.todo.data.TodoRepository
import com.vishalsingh444888.todo.data.TodoRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTodoDatabase(app: Application): TodoDatabase {
        return Room.databaseBuilder(
            app,
            TodoDatabase::class.java,
            name = "todo_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTodoRepository(db:TodoDatabase): TodoRepository {
        return TodoRepositoryImp(db.dao)
    }
}