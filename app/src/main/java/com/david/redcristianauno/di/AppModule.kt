package com.david.redcristianauno.di

import android.content.Context
import androidx.room.Room
import com.david.redcristianauno.application.AppConstants.DATABASE_NAME
import com.david.redcristianauno.data.local.AppDatabase
import com.david.redcristianauno.data.network.FirebaseService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideRoomInstance(@ApplicationContext context: Context) = Room
        .databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .build() /* Conexion con Room */

    @Singleton
    @Provides
    fun provideTragosDao(db: AppDatabase) = db.userDao()

    @Singleton
    @Provides
    fun provideFirestore() = FirebaseService()
}