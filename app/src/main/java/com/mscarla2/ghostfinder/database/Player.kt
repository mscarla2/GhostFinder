package com.mscarla2.ghostfinder.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player_table")
data class Player(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "player_id")
    var id: Long = 0L,

    @ColumnInfo(name = "player_name")
    var name: String = "",

    @ColumnInfo(name = "player_level")
    var level: Int = 1,

    @ColumnInfo(name = "player_sword")
    var sword: String = "",

    @ColumnInfo(name = "player_hp")
    var hp: Int = 15,

    @ColumnInfo(name = "player_pow")
    var pow: Int = 0,

    @ColumnInfo(name = "player_def")
    var def: Int = 0,

    @ColumnInfo(name = "player_agi")
    var agi: Int = 0
    )
