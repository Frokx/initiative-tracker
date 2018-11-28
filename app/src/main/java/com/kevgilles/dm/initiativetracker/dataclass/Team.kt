package com.kevgilles.dm.initiativetracker.dataclass

import androidx.room.*

@Entity(foreignKeys = [
    ForeignKey(entity = Campaign::class,
        parentColumns = [Campaign.CAMPAIGN_ID],
        childColumns = [Team.CAMPAIGN_ID_FK])],
    indices = [
        Index(Team.CAMPAIGN_ID_FK)
    ])
data class Team (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = TEAM_ID)
    var id: Long = 0,

    @ColumnInfo(name = TEAM_NAME)
    var name: String,

    // Foreign Key
    @ColumnInfo(name = CAMPAIGN_ID_FK)
    var campaignId: Long
) {
    companion object {
        const val TEAM_ID: String = "team_id"
        const val TEAM_NAME: String = "team_name"
        const val CAMPAIGN_ID_FK: String = "campaign_id_fk"
    }
}