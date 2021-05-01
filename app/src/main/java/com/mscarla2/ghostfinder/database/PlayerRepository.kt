package com.mscarla2.ghostfinder.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.util.concurrent.Executors

class PlayerRepository private constructor(context: Context) {

    private val database: PlayerDatabase = Room.databaseBuilder(
        context.applicationContext,
        PlayerDatabase::class.java,
        "player_database"
    ).build()

    private val playerDao = database.playerDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getAllPlayers(): LiveData<List<Player>> = playerDao.getAllPlayers()

    fun getPlayer(id: Long): Player = playerDao.getPlayer(id)
    fun insert(player: Player) {
        executor.execute {
            playerDao.insert(player)
        }
    }
    fun deletePlayer(player: Player) {
        executor.execute {
            playerDao.deletePlayer(player)
        }
    }

    companion object {
        private var INSTANCE: PlayerRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = PlayerRepository(context)
            }
        }

        fun get(): PlayerRepository {
            return INSTANCE
                ?: throw java.lang.IllegalStateException("PlayerRepository must be initialized.")
        }
    }
}
