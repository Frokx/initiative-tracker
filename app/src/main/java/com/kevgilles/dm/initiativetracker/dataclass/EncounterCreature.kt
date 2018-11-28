package com.kevgilles.dm.initiativetracker.dataclass

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(foreignKeys = [
    ForeignKey(entity = Creature::class,
        parentColumns = [Creature.CREATURE_ID],
        childColumns = [EncounterCreature.CREATURE_ID_FK],
        onDelete = CASCADE),
    ForeignKey(entity = Encounter::class,
        parentColumns = [Encounter.ENCOUNTER_ID],
        childColumns = [EncounterCreature.ENCOUNTER_ID_FK],
        onDelete = CASCADE)],
    indices = [
        Index(EncounterCreature.CREATURE_ID_FK),
        Index(EncounterCreature.ENCOUNTER_ID_FK)
    ])
data class EncounterCreature(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ENCOUNTER_CREATURE_ID)
    val id: Long = 0,

    // Foreign Key
    @ColumnInfo(name = ENCOUNTER_ID_FK)
    val encounterId: Long = 0,
    // Foreign Key
    @ColumnInfo(name = CREATURE_ID_FK)
    val creatureId: Long = 0
) {
    companion object {
        const val ENCOUNTER_CREATURE_ID: String = "encounter_creature_id"
        const val CREATURE_ID_FK: String = "creature_id_fk"
        const val ENCOUNTER_ID_FK: String = "encounter_id_fk"
    }
}