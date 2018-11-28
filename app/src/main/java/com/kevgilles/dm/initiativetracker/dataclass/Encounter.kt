package com.kevgilles.dm.initiativetracker.dataclass

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(foreignKeys = [
    ForeignKey(entity = Campaign::class,
        parentColumns = [Campaign.CAMPAIGN_ID],
        childColumns = [Encounter.CAMPAIGN_ID_FK],
        onDelete = CASCADE)],
    indices = [
        Index(Encounter.CAMPAIGN_ID_FK)
    ])
data class Encounter(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ENCOUNTER_ID)
    val id: Long = 0,

    @ColumnInfo(name = ENCOUNTER_NAME)
    val name: String,

    @ColumnInfo(name = CAMPAIGN_ID_FK)
    val campaignId: Long

) {
    companion object {
        const val ENCOUNTER_ID: String = "encounter_id"
        const val ENCOUNTER_NAME: String = "encounter_name"
        const val CAMPAIGN_ID_FK: String = "campaign_id_fk"
    }
}