package com.mscarla2.ghostfinder.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(character: Character)

    @Query("DELETE FROM character_table")
    fun deleteAll()

    @Delete
    fun deleteCharacter(character: Character)

    @Query("SELECT * FROM character_table LIMIT 1")
    fun getAnyCharacter(): Array<Character>

    @Query("SELECT * FROM character_table ORDER BY character_id DESC")
    fun getAllCharacters(): LiveData<List<Character>>

}