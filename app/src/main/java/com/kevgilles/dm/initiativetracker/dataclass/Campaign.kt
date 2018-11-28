package com.kevgilles.dm.initiativetracker.dataclass

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Campaign(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = CAMPAIGN_ID)
    val id: Long = 0,

    @ColumnInfo(name = CAMPAIGN_NAME)
    val name: String,

    @ColumnInfo(name = CAMPAIGN_GAME)
    val game: String

) {
    companion object {
        const val CAMPAIGN_ID: String = "campaign_id"
        const val CAMPAIGN_NAME: String = "campaign_name"
        const val CAMPAIGN_GAME: String = "campaign_game"
    }
}