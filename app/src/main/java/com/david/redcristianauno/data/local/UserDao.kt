package com.david.redcristianauno.data.local

import androidx.room.*
import com.david.redcristianauno.domain.models.User
import com.david.redcristianauno.domain.models.UserEntity
import com.david.redcristianauno.vo.Resource
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM userTable WHERE id = :userId")
    suspend fun getUserById(userId: String): UserEntity?

    @Query("SELECT * FROM userTable WHERE id = :userId")
    fun getUserByIdAsFlow(userId: String): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)
}