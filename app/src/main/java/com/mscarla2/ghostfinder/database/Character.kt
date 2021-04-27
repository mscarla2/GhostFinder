package com.mscarla2.ghostfinder.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character_table")
data class Character(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "character_id")
    var id: Long = 0L,

    @ColumnInfo(name = "character_name")
    var name: String = "",

    @ColumnInfo(name = "character_level")
    var level: Int = 0,

    @ColumnInfo(name = "character_sword")
    var sword: String = "",

    @ColumnInfo(name = "character_hp")
    var hp: String = "",

    @ColumnInfo(name = "character_pow")
    var pow: String = "",

    @ColumnInfo(name = "character_def")
    var def: String = "",

    @ColumnInfo(name = "character_agi")
    var agi: String = "",


    )
