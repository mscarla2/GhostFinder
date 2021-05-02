package com.mscarla2.ghostfinder.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(player: Player)

    @Update
    fun update(player: Player)

    @Query("DELETE FROM player_table")
    fun deleteAll()

    @Delete
    fun deletePlayer(player: Player)

    @Query("SELECT * FROM player_table LIMIT 1")
    fun getAnyPlayer(): Array<Player>

    @Query("SELECT * FROM player_table ORDER BY player_id DESC")
    fun getAllPlayers(): LiveData<List<Player>>

    @Query("SELECT * FROM player_table WHERE player_id = :id")
    fun getPlayer(id: Long): Player
}