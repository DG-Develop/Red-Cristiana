package com.david.redcristianauno.data.local

import androidx.room.*
import com.david.redcristianauno.domain.models.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM userTable WHERE id = :userId")
    suspend fun getUserById(userId: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)
}