package com.kevgilles.dm.initiativetracker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kevgilles.dm.initiativetracker.dataclass.*

@Database(entities = [Campaign::class, Team::class, Character::class, Encounter::class, Creature::class, EncounterCreature::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun campaignDao(): CampaignDao
    abstract fun teamDao(): TeamDao
    abstract fun characterDao(): CharacterDao
    abstract fun encounterDao(): EncounterDao
    abstract fun creatureDao(): CreatureDao
    abstract fun encounterCreatureDao(): EncounterCreatureDao

}