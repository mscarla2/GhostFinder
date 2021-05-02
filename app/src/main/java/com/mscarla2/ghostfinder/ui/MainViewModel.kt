package com.mscarla2.ghostfinder.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.mscarla2.ghostfinder.database.Player
import com.mscarla2.ghostfinder.database.PlayerRepository

class MainViewModel(app: Application) : AndroidViewModel(app) {

    init {
        PlayerRepository.initialize(app)
    }

    private val playerRepository = PlayerRepository.get()
    val players = playerRepository.getAllPlayers()

    fun insert(player: Player) {
        playerRepository.insert(player)
    }

    fun update(player: Player){
        playerRepository.update(player)
    }
    fun deletePlayer(player: Player) {
        playerRepository.deletePlayer(player)
    }
    fun getPlayer(id: Long){
        playerRepository.getPlayer(id)
    }
}