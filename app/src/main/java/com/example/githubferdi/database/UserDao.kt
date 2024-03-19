// UserDao.kt
package com.example.githubferdi.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun addToFavorite(userFavorite: UserFav)

    @Query("DELETE FROM favorite_table WHERE id = :id")
    suspend fun removeUserFromFavorite(id: Int)

    @Query("SELECT * FROM favorite_table")
    fun getFavoriteUsers(): LiveData<List<UserFav>>

    @Query("SELECT count(*) FROM favorite_table WHERE id = :id")
    suspend fun checkUserFavorite(id: Int): Int


}
