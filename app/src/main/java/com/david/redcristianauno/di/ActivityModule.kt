package com.david.redcristianauno.di

import com.david.redcristianauno.data.local.LocalDataSource
import com.david.redcristianauno.data.local.LocalDataSourceImpl
import com.david.redcristianauno.data.remote.RemoteChurchDataSource
import com.david.redcristianauno.data.remote.RemoteChurchDataSourceImpl
import com.david.redcristianauno.data.remote.RemoteUserDataSource
import com.david.redcristianauno.data.remote.RemoteUserDataSourceImpl
import com.david.redcristianauno.data.repository.ChurchRepository
import com.david.redcristianauno.data.repository.ChurchRepositoryImpl
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
    abstract fun bindUserRemoteDataSourceImpl(remoteDataSourceImpl: RemoteUserDataSourceImpl): RemoteUserDataSource

    @Binds
    abstract fun bindUserRepositoryImpl(userRepositoryImpl: UserRepositoryImpl): UserRepository


    @Binds
    abstract fun bindChurchRemoteDataSourceImpl(remoteChurchDataSourceImpl: RemoteChurchDataSourceImpl): RemoteChurchDataSource

    @Binds
    abstract fun bindChurchRepositoryImpl(churchRepositoryImpl: ChurchRepositoryImpl): ChurchRepository
}