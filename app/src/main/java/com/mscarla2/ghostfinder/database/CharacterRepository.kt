package com.mscarla2.ghostfinder.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.util.concurrent.Executors

class CharacterRepository private constructor(context: Context) {

    private val database: CharacterDatabase = Room.databaseBuilder(
        context.applicationContext,
        CharacterDatabase::class.java,
        "character_database"
    ).build()

    private val characterDao = database.characterDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getAllCharacters(): LiveData<List<Character>> = characterDao.getAllCharacters()

    fun insert(character: Character) {
        executor.execute {
            characterDao.insert(character)
        }
    }

    fun deleteCharacter(character: Character) {
        executor.execute {
            characterDao.deleteCharacter(character)
        }
    }

    companion object {
        private var INSTANCE: CharacterRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CharacterRepository(context)
            }
        }

        fun get(): CharacterRepository {
            return INSTANCE
                ?: throw java.lang.IllegalStateException("CharacterRepository must be initialized.")
        }
    }
}
