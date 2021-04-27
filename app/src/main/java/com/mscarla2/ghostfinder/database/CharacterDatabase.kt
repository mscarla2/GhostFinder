package com.mscarla2.ghostfinder.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Character::class], version = 1, exportSchema = true)
abstract class CharacterDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}