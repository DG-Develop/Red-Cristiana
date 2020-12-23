package com.david.redcristianauno.di

import com.david.redcristianauno.data.local.LocalDataSource
import com.david.redcristianauno.data.local.LocalDataSourceImpl
import com.david.redcristianauno.data.remote.RemoteDataSource
import com.david.redcristianauno.data.remote.RemoteDataSourceImpl
import com.david.redcristianauno.data.repository.UserRepository
import com.david.redcristianauno.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ActivityModule {

    @Binds
    abstract fun bindLocalDataSourceImpl(localDataSourceImpl: LocalDataSourceImpl): LocalDataSource

    @Binds
    abstract fun bindRemoteDataSourceImpl(remoteDataSourceImpl: RemoteDataSourceImpl): RemoteDataSource

    @Binds
    abstract fun bindUserRepositoryImpl(userRepositoryImpl: UserRepositoryImpl): UserRepository
}